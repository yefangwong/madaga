# 📋 Cornelius csp-portal-web 系統分析與架構自洽性稽核報告
## 專案模組：Cornelius Service Platform (CSP) & csp-portal-web (csp-web-portal)

> **文件狀態**：正式架構審查報告 (Official Architecture Audit Report)  
> **稽核日期**：2026-10-03  
> **報告編號**：AUDIT-CSP-PORTAL-20261003-V1  
> **主持架構師**：資深軟體架構師 (Lead Software Architect)  
> **稽核目標範圍**：
> - 知識庫需求與規格庫：`BRD.md`, `PATCH_PLAN.md`, `specs/FS_S1_N01 ~ FS_S1_N03`
> - 實體代碼倉庫：`/Users/yefangwong/Documents/GitHub.nosync/madaga`（`csp/csp-portal-web`, `csp/csp-service`, `csp/csp-domain-jpa`, `csc/nlp2sql-app`）
> **遵循原則**：遵守 ADR-0005 需求雙向寫回原則、ADR-0006 實體倉庫 SSOT 預檢原則、Alibaba P3C 規約與開源潔淨室 (Clean-Room) 架構標準。

---

## 🧭 一、 執行摘要 (Executive Summary)

本報告針對 Cornelius CSP 運營入口平台模組（`csp-portal-web`，亦稱 `csp-web-portal`）即將執行的**技術文件檢索 (Lucene 9)**、**文字化 ERD 白名單 (Textual ERD)**、**私域術語畫布 (TermAlign Studio)**、**Text-to-SQL 雙軌防線** 與 **RBAC 權限體系升級**等核心功能，進行「需求規格（Spec/BRD）」、「後端架構（Controller/Service/Entity）」、「資料模型（DB Schema）」與「前端畫面（Thymeleaf/Vue/CSS）」四維三位一體的極限交叉審查。

### 綜合評級：⚠️ 存在重大架構斷層與執行期崩潰隱患 (Blocked / High Risk)

經過實體代碼與資料庫 DDL 探針比對，發現 **3 項致命硬傷 (Blockers)**、**5 項規格斷層 (Gaps)** 與 **2 項資料模型未閉環缺陷**。若未在工單執行前完成規格校準與代碼縫合，將導致前端頁面 JavaScript 執行期拋錯崩潰、安全防護完全被繞過、以及生產環境並發連線串線與記憶體外洩。

```text
┌────────────────────────────────────────────────────────────────────────┐
│                        四維架構交叉稽核結果總覽                          │
├──────────────────┬──────────────────┬──────────────────┬───────────────┤
│ 1. 前端畫面與互動 │ 2. 路由與安全控制 │ 3. 業務與並發狀態 │ 4. 資料庫 DDL │
│  (UI / Templates)│  (Spring Security│  (Concurrency/BL)│  (Schema/Data)│
├──────────────────┼──────────────────┼──────────────────┼───────────────┤
│ ❌ doQueryBySQL 缺失│ ❌ 路由完全脫鉤   │ ❌ 單租戶硬編碼  │ ❌ 多角色約束  │
│ ❌ 4大幽靈模板不存在│   (/manage/emp vs│    (Key: "1")   │    衝突未定義 │
│ ❌ 搜尋框 DOM ID 缺 │    /employee/**) │ ❌ 全域無界 Map  │ ❌ 術語/白名單│
│ ❌ SQL 結果無處渲染 │ ❌ 規則無法命中  │ ❌ Metaspace 洩漏│    無持久化表 │
└──────────────────┴──────────────────┴──────────────────┴───────────────┘
```

---

## 🚨 二、 致命硬傷清單 (Blockers：流程中斷、安全失效與執行期崩潰)

### 1. 【前端/功能崩潰】前端呼叫未定義函式 `this.doQueryBySQL`，且畫面缺乏 SQL 結果渲染容器
* **實體代碼位置**：[`csp-portal-web/.../templates/emp/show.html:241`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-portal-web/src/main/resources/templates/emp/show.html#L241)
* **現象溯源**：
  在員工首頁的 AI 助理對話懸浮框（FAB）SSE 事件監聽邏輯中：
  ```javascript
  source.onmessage = (event) => {
      if (event.data.indexOf("{") == 0) {
          let data = JSON.parse(event.data);
          if (data.content !== '') {
              if (data.UUID !== undefined) {
                  this.doQueryBySQL(data.UUID); // 🚨 未定義的孤兒方法！
              }
          }
      }
      this.scrollToBottom();
  }
  ```
  後端 [`OpenAISSEEventSourceListener:48`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-service/src/main/java/com/dhf/system/chat/listener/OpenAISSEEventSourceListener.java#L48) 在生成完 SQL 後，會送出 `[UUID]` 事件給前端。但檢索前端整個 Vue 實例的 `methods`，**`doQueryBySQL` 完全沒有被實作**！
* **致命後果**：
  1. 瀏覽器控制台立即噴出 `Uncaught TypeError: this.doQueryBySQL is not a function`，導致 Vue 內部狀態異常。
  2. 後端 [`SqlController.java:33`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-portal-web/src/main/java/com/dhf/hrsys/controller/SqlController.java#L33) 雖然暴露了 `/sql/query/{uuid}`，但前端根本沒有任何機制觸發它。
  3. **畫面完全兜不起來**：在 `emp/show.html` 中，**完全沒有用來展示 SQL 查詢結果（表格、卡片或資料清單）的 HTML 容器**。使用者在對話框提問後，即便後端生成了 SQL，使用者也永遠看不到查詢結果。

---

### 2. 【資安防禦真空】安全路由與 Controller 實體映射完全脫鉤 (Security Route Drift)
* **規格與計畫描述**：
  [`BRD.md:37, 52`](file:///Users/yefangwong/Knowledge/Projects/startup/madaga/BRD.md#L37) 與 [`PATCH_PLAN.md:80`](file:///Users/yefangwong/Knowledge/Projects/startup/madaga/PATCH_PLAN.md#L80) 聲明核心業務權限為：
  - `employee`: `/employee/**`
  - `department`: `/department/**`
  - 並指示在 `SecurityConfig.java` 依「由狹到寬」配置 `/employee/**`, `/department/**` 之 `hasAnyRole` 權限。
* **實體代碼現況**：
  - 員工管理 Controller：[`EmployeeController.java:25`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-portal-web/src/main/java/com/dhf/hrsys/controller/EmployeeController.java#L25) 映射為 `@RequestMapping("manage/emp")`。
  - 部門管理 Controller：[`DepartmentController.java:31`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-portal-web/src/main/java/com/dhf/hrsys/controller/DepartmentController.java#L31) 映射為 `@RequestMapping("manage/department")`。
  - 側邊欄導航實體連結：[`default.html:132, 138`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-portal-web/src/main/resources/templates/layout/default.html#L132) 指向 `/manage/emp/show` 與 `/manage/department/show`。
* **致命後果**：
  若工程師直接依規格在 `SecurityConfig` 寫入：
  ```java
  .requestMatchers("/employee/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
  .requestMatchers("/department/**").hasAnyRole("ADMIN", "MANAGER")
  ```
  使用者發送的實際請求 `/manage/emp/show` 與 `/manage/department/show` **將 100% 漏過這些安全防護規則**，直接掉入兜底的 `.anyRequest().authenticated()`。經理角色能肆意存取無授權端點，員工也能直接發送 POST 修改部門，造成嚴重的垂直越權資安黑洞！

---

### 3. 【並發/穩定性缺陷】單租戶硬編碼與全域無界 Map 記憶體外洩 (Concurrency Collision & Session Bleed)
* **實體代碼位置**：[`SseServiceImpl.java:67, 88, 103, 116`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-service/src/main/java/com/dhf/hrsys/service/impl/SseServiceImpl.java#L67)
* **現象溯源**：
  ```java
  // 建立 SSE 連線
  LocalCacheService.putCache("1", sseEmitter);
  
  // 取得對話歷程
  String messageContext = (String) LocalCacheService.cache.get("msg1");
  ...
  LocalCacheService.putCache("msg1", JSONUtil.toJsonStr(messages));
  ```
  後端把所有用戶的 SSE Emitter 硬編碼儲存在 Key 為 `"1"` 的快取中，把對話 Context 硬編碼在 Key 為 `"msg1"` 的快取中。
* **致命後果**：
  1. **串線與並發覆蓋 (Session Bleed)**：當使用者 A 與使用者 B 同時開啟頁面提問，使用者 B 的連線會瞬間覆蓋使用者 A 的 `SseEmitter`。OpenAI 生成的串流文字將會錯推到使用者 B 的畫面上，造成多租戶數據外洩。
  2. **記憶體洩漏 (OOM)**：[`LocalCacheService.cache`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-service/src/main/java/com/hongfang/csp/system/service/impl/LocalCacheService.java#L10) 底層為未加鎖、無容量上限且無過期淘汰（No TTL/LRU）的靜態 `HashMap`。高頻查詢下產生的大量 UUID 與 SQL 字串將永久滯留於老生代 (Old Gen)，最終引發 JVM OOM 崩潰。

---

## ⚠️ 三、 規格斷層與邏輯漏洞清單 (Gaps & Inconsistencies)

### 1. 多角色模型（1:N / M:N）與實體 DB Constraint 衝突未解
* **規格與現狀衝突**：
  - 規格書 [`BRD-CSP-SEC-004`](file:///Users/yefangwong/Knowledge/Projects/startup/madaga/BRD.md#L75) 要求支援多角色綁定，規劃建立 `sys_user_role(user_id, role_id)` 關聯表。
  - 實體 DDL [`mysql_csp.sql:140`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-domain-jpa/src/main/resources/database/csp/mysql_csp.sql#L140) 現狀為：
    ```sql
    create table if not exists sys_user (
        ...
        role_id bigint not null comment '角色id',
        ...
    );
    ```
* **架構斷層**：
  規格**未交代既有欄位 `sys_user.role_id` 的平滑過渡策略**。若直接加入中間表 `sys_user_role`，但在新增用戶時未傳入 `sys_user.role_id`，將直接引發 MySQL `Column 'role_id' cannot be null` 的約束報錯。
* **規格重工勘誤**：
  `PATCH_PLAN.md` Task 2.1 寫道「在 `sys_permission` 擴充 `url` 欄位」。但實體 DDL [`mysql_csp.sql:73`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-domain-jpa/src/main/resources/database/csp/mysql_csp.sql#L73) 中早已具備 `url varchar(200) null comment '路徑'` 欄位。顯見規格制定時未執行代碼 SSOT 預檢。

---

### 2. 前端「幽靈範本 (Ghost Templates)」完全缺失
* **規格規劃**：
  [`PATCH_PLAN.md`](file:///Users/yefangwong/Knowledge/Projects/startup/madaga/PATCH_PLAN.md) 提及以下前端視圖路徑：
  - `role/list.html`（角色標籤展示）
  - `user/list.html`（用戶標籤展示）
  - `datasource/config.html`（資料源探針與穿梭框）
  - `term/canvas.html`（2D 語意引力畫布）
* **實體現狀事實**：
  在實體代碼目錄 [`src/main/resources/templates/`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-portal-web/src/main/resources/templates) 下，**完全不存在 `role/`、`user/`、`datasource/` 與 `term/` 這四個目錄或檔案**！目前系統僅有 `auth/`、`dashboard/`、`department/`、`emp/`、`energy/` 與 `layout/`。
* **架構斷層**：
  規格書未定義新頁面的 HTML 骨架、未定義由哪個 Spring MVC Controller 方法進行轉發（View Resolver），亦未在全站導航側邊欄（`layout/default.html`）預留入口連結。

---

### 3. 全文檢索搜尋框 DOM ID 與 Modal 結構未落地
* **規格要求**：
  [`FS_S1_N01:118`](file:///Users/yefangwong/Knowledge/Projects/startup/madaga/specs/FS_S1_N01_embedded_lucene_search.md#L118) 明確指定：
  - 桌面版搜尋框綁定 ID：`#globalSearchInput`
  - 手機版搜尋框綁定 ID：`#mobileSearchInput`
  - 檢索結果彈窗：`#searchResultModal`
* **實體現狀事實**：
  檢視 [`layout/default.html:107, 121`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-portal-web/src/main/resources/templates/layout/default.html#L107)，這兩個 `<input>` 元素**完全沒有指定 `id` 屬性**，且頁面中根本沒有 `#searchResultModal` 的 DOM 骨架，亦無載入檢索用的 JavaScript。規格與畫面目前呈現完全脫節狀態。

---

### 4. Text-to-SQL Prompt 知識注入發生嚴重語意倒錯
* **實體代碼位置**：[`Synthesizer.java:42`](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-service/src/main/java/com/dhf/hrsys/service/impl/Synthesizer.java#L42)
* **現象分析**：
  ```java
  private String instructPrompt(String str) {
      return str +
          "，撰寫一個 MySQL SQL, 包含\n" +
          "table:department(id,name,number)(部門id,員工姓名,部門編號)"+ // 🚨 語意倒錯！
          "table:employee(id,age,gender,name,number,dep_id)(id,年齡,員工姓名,員工編號,部門id)，"+
          "輸出number、emp_name、dep_name\n"+
          "只要輸出SQL，不要加上說明\n";
  }
  ```
  在寫死的 Prompt 中，竟然將 `department.name`（部門名稱）標註為「**員工姓名**」！
* **致命後果**：
  LLM 接收到錯誤的 Schema 描述，會將部門名稱當作員工姓名進行 `WHERE` 條件比對或 `JOIN`，在實體環境下會頻繁輸出語意錯誤的 SQL。

---

### 5. 前後端專案分裂：雙頭馬車無防護
* **現象分析**：
  - 倉庫中的 `csc/nlp2sql-app/src/App.vue` 為獨立的 Vue 專案。其前端直接透過 Axios 向第三方或客戶端拉取 API Key，在瀏覽器呼叫已被 OpenAI 廢棄的 `text-davinci-002` 模型。
  - 入口後台 `csp-portal-web` 內部又有一套 `emp/show.html` + `QuestionController` + `SseServiceImpl` 的對話通道。
* **架構斷層**：
  前端 Vue App 完全繞過了 `csp-portal-web` 後端與規劃中的 `llm-sql-guard` AST 剛性安全門禁，金鑰暴露在用戶端，且無法共享文字化 ERD 白名單。

---

## 🗄️ 四、 資料設計閉環與欄位合理性審查

| 檢驗項目 | 規格期望 (Spec Expectation) | 實體現狀 (Physical DDL/Code) | 評估結論與隱患 (Verdict & Impact) |
| :--- | :--- | :--- | :--- |
| **資料源探針與白名單持久化** (`FS_S2_N02`) | 管理員配置 JDBC 資訊，勾選白名單資料表，序列化為文字化 ERD 供 AI 查詢 | 缺乏儲存 JDBC 連線配置與資料表勾選清單的實體表 | **未閉環 (No Persistence)**。<br>若無 `sys_datasource_config` 與 `sys_datasource_whitelist` 表，每次伺服器重啟或重新載入，探針狀態與勾選白名單即遺失，AI 無法持續取得動態 Schema。 |
| **私域術語同義詞庫** (`FS_S1_N03`) | 拖曳引力畫布後持久化至 `sys_term_synonym` | 資料庫 DDL 中無此表 | **未閉環 (Missing DDL)**。<br>規格書未定義該實體表之 DDL（如主鍵、`term_a`、`term_b`、`similarity`、`relatedness`、`status`、`version`）。 |
| **`sys_user` 欄位設計** | 支援多角色綁定與多色塊標籤展示 | `role_id` 為單一 `BIGINT NOT NULL` | **欄位衝突**。<br>改為 `sys_user_role` 多對多時，必須明確 `role_id` 欄位是保留作為預設主要角色 (Primary Role)，抑或降級為可空 (Nullable)。 |
| **SQL 執行審查防禦** (`BRD-CSP-SEC-005`) | AST 門禁審查後安全執行 | `SqlController:48` 僅執行 `s.replace(";", "")` 即直接送入 MyBatis 執行 | **極高資安風險**。<br>任何包含 `DROP TABLE`, `1=1`, `DELETE` 或無分頁慢查語句皆能長驅直入。 |

---

## 🛠️ 五、 架構師落地修訂建議清單 (Action Plan & SOP)

為落實 ADR-0005（需求寫回閉環）與系統端到端自洽，建議依下列 5 大工序進行修正：

### 步驟 1：前端 UI 呼叫與結果展示閉環 (`emp/show.html`)
1. **補齊 Vue 執行方法**：在 `emp/show.html` 的 `methods` 補上 `doQueryBySQL(uuid)` 實作：
   ```javascript
   doQueryBySQL(uuid) {
       const self = this;
       axios.get('/sql/query/' + uuid)
           .then(function(res) {
               if (res.data && res.data.length > 0) {
                   self.renderSqlTable(res.data);
               } else {
                   self.appendChatReply("查詢成功，但無符合條件的資料。");
               }
           })
           .catch(function(err) {
               self.appendChatReply("⚠️ 執行 SQL 查詢發生錯誤，請稍後再試。");
           });
   }
   ```
2. **在畫面加入結果渲染容器**：在對話彈窗氣泡內加入自適應 Bootstrap Table 樣式，將後端回傳之 `List<HashMap>` 動態渲染為欄位與資料列。

---

### 步驟 2：校準安全路由與 Controller 命名 (三擇一決策)
* **推薦方案 (破壞性最小)**：維持現有實體代碼之 `manage/emp` 與 `manage/department`，全面寫回修訂 `BRD.md`、`PATCH_PLAN.md` 中的 URL 規範：
  - 員工管理資源：`/manage/emp/**`（原寫法 `/employee/**` 作廢）
  - 部門管理資源：`/manage/department/**`（原寫法 `/department/**` 作廢）
  - 同步更新 `SecurityConfig.java`：
    ```java
    .requestMatchers("/manage/emp/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
    .requestMatchers("/manage/department/**").hasAnyRole("ADMIN", "MANAGER")
    ```

---

### 步驟 3：會話隔離與並發快取改造 (`SseServiceImpl`)
1. **動態 Session 鍵值**：廢棄 `"1"` 與 `"msg1"` 硬編碼。改為從當前請求獲取唯一識別碼（如前端生成的 `sessionId` 或 Spring Security 上下文之用戶名 `Authentication.getName()`）：
   ```java
   String sessionKey = "sse:" + sessionId;
   LocalCacheService.putCache(sessionKey, sseEmitter);
   ```
2. **升級快取容器**：以 Caffeine 快取或設定有 TTL/MaximumSize 的 `ConcurrentHashMap` 取代純 `HashMap`，設定 30 分鐘自動過期淘汰，杜絕 OOM 隱患。

---

### 步驟 4：多角色 DDL 遷移與資料庫腳本補齊
1. **新增 `sys_user_role` 中間表 DDL**：
   ```sql
   CREATE TABLE IF NOT EXISTS sys_user_role (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       user_id BIGINT NOT NULL COMMENT '用戶ID',
       role_id BIGINT NOT NULL COMMENT '角色ID',
       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
       UNIQUE KEY uq_user_role (user_id, role_id)
   ) COMMENT='用戶角色關聯表';
   ```
2. **相容變更既有表**：
   ```sql
   ALTER TABLE sys_user MODIFY COLUMN role_id BIGINT NULL COMMENT '主要角色ID(保留相容)';
   -- 將既有用戶角色關聯數據平滑灌入中間表
   INSERT IGNORE INTO sys_user_role (user_id, role_id) 
   SELECT id, role_id FROM sys_user WHERE role_id IS NOT NULL;
   ```
3. **補齊資料源與術語實體表 DDL**：
   - 建立 `sys_datasource_config` 儲存 JDBC 連線參數（密碼需 AES 加密）。
   - 建立 `sys_term_synonym` 儲存自定義術語與引力權重。

---

### 步驟 5：修訂 `Synthesizer.java` Prompt 語意
修正硬編碼 Prompt 的欄位註解，將 `table:department(id,name,number)` 正確標註為 `(部門id,部門名稱,部門編號)`，消除 LLM 幻覺根源。

---

## 📅 六、 審查元數據與核簽 (Audit Metadata & Sign-off)

- **審查日期**：2026-10-03
- **報告歸檔路徑**：
  - 知識庫單一事實源：`Knowledge/Projects/startup/madaga/AUDIT_REPORT_CSP_PORTAL_WEB_20261003.md`
  - 實體工程代碼倉庫：`madaga/docs/AUDIT_REPORT_CSP_PORTAL_WEB_20261003.md`
- **下一步行動建議**：
  本報告產出之問題已具備完整代碼坐標與修訂策略。建議依本報告結論，優先修訂 [`PATCH_PLAN.md`](file:///Users/yefangwong/Knowledge/Projects/startup/madaga/PATCH_PLAN.md) 之 Phase 1 與 Phase 2 工單任務驗收標準，確保進入代碼變更階段時零盲點開工。

---
## Sources
- `/Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-portal-web/`
- `/Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-domain-jpa/src/main/resources/database/csp/mysql_csp.sql`
- `/Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-service/src/main/java/com/dhf/hrsys/service/impl/SseServiceImpl.java`
- `/Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-service/src/main/java/com/dhf/hrsys/service/impl/Synthesizer.java`
- [[Projects/startup/madaga/BRD.md]]
- [[Projects/startup/madaga/PATCH_PLAN.md]]
- [[Projects/startup/madaga/specs/FS_S1_N01_embedded_lucene_search.md]]
- [[Projects/startup/madaga/specs/FS_S2_N02_textual_erd_whitelist.md]]
- [[Projects/startup/madaga/specs/FS_S1_N03_semantic_canvas_studio.md]]
