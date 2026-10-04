# 🧪 Madaga CSP 綜合測試計畫書 (Test Plan & Verification Strategy)

> **專案名稱**：Cornelius Service Platform (CSP) & csp-portal-web  
> **依據文件**：[[docs/BRD.md|商業需求池 (BRD)]] / [[PATCH_PLAN.md|全量工單計畫]] / [[docs/specs/FS.md|功能規格大典]]  
> **發布日期**：2026-10-03  
> **執行準則**：TDD 老碼農門禁 (Gauntlet Protocol) & Alibaba P3C 靜態安全規約  

---

## 🎯 1. 測試目標與覆蓋率硬門禁 (Quality Gates)

所有新增或重構代碼，必須通過三道剛性門禁始得合併：
1. **行覆蓋率與分支覆蓋率**：
   * 核心業務邏輯單元 (`BaseBL`, `LlmSqlGuard`, `LuceneIndexManager`, `RetrofitCalibrationEngine`)：**Line Coverage $\ge 80\%$**，**Branch Coverage $\ge 75\%$**。
2. **靜態代碼規約**：
   * 執行 `mvn p3c-pmd:check`，強制級 (Blocker) 與重大級 (Critical) 違規數必須為 **0**。
   * 嚴禁空 catch 區塊、嚴禁 `e.printStackTrace()`、嚴禁未託管之執行緒池。
3. **執行延遲門禁**：
   * 嵌入式 Lucene 檢索：$\le 15	ext{ms}$。
   * SQL AST 剛性審計：$\le 2	ext{ms}$。
   * 術語 20 步座標上升凸優化微調：$\le 50	ext{ms}$。

---

## 🧪 2. 分階段測試案例設計 (Phase-by-Phase Test Specifications)

### 🛡️ Phase 0: 基礎依賴資安弱點治理測試 (BRD-CSP-SEC-006)
- **測試目標**：驗證 Tomcat 10.1.34 與 Jackson 2.16.2 升級後，既有功能 100% 零回歸、零破壞。
- **測試指令**：
  ```bash
  mvn clean test -pl csp/csp-base,csp/csp-portal-web
  ```
- **測試用例**：
  1. `TC-SEC-01`: 驗證 Tomcat 內嵌伺服器啟動正常，Filter 與 Servlet 映射無拋錯。
  2. `TC-SEC-02`: 驗證 Jackson JSON 序列化與反序列化，測試 `@JsonIgnore` 與多態類型物件，確保資料不失真。
  3. `TC-SEC-03`: 驗證前端 `npm update axios` 後，登入鑑權與 POST 傳參正常。
  4. `TC-SEC-04`: 執行 Dependabot 掃描比對，確認 33 項 Tomcat CVE 與 27 項 Axios CVE 清除。

---

### 🟢 Phase 1: 嵌入式 Lucene 9 技術手冊檢索測試 (BRD-CSP-SRCH-001)
- **測試目標**：驗證 PDFBox 文本提取、Lucene 索引構建、Exact Term 故障碼過濾與高亮摘要。
- **測試套件**：
  * `LuceneIndexManagerTest.java` (索引管理器底層測試)
  * `DocSearchBLTest.java` (純 Java 業務單元測試)
- **測試用例**：
  1. `TC-SRCH-01`: 故障碼精確檢索：輸入 `P0115`，驗證 Top-1 命中「水溫感應器故障」，耗時 $\le 15	ext{ms}$。
  2. `TC-SRCH-02`: 零件號精確檢索：輸入 `ME223120`，驗證精準命中零件章節。
  3. `TC-SRCH-03`: 模糊全文檢索：輸入「煞車失靈」，驗證 BM25 評分排序與相關度。
  4. `TC-SRCH-04`: 上下文高亮提取：驗證輸出 HTML 包含 `<mark class="cui-highlight">` 且前後截取 60 字元。
  5. `TC-SRCH-05`: 邊界測試：輸入空字串、超長字串 (1000 字元) 或特殊字符 (`*?:\/`)，系統正常攔截無例外拋出。

---

### 🛡️ Phase 2: RBAC 權限與圓角色塊 UI 標籤測試 (BRD-CSP-SEC-001 ~ 004)
- **測試目標**：驗證 3 大核心角色分級權限攔截與前端 Thymeleaf 標籤渲染。
- **測試套件**：
  * `SecurityRbacWebTest.java` (MockMvc 整合測試)
  * `SysRoleBLTest.java` & `SysUserBLTest.java` (純單元測試)
- **測試用例**：
  1. `TC-RBAC-01`: 管理員全權限驗證：以 `ROLE_ADMIN` 請求所有端點，全數回傳 HTTP 200。
  2. `TC-RBAC-02`: 經理權限邊界：以 `ROLE_MANAGER` 請求 `/sysUser/**`，強制攔截回傳 HTTP 403。
  3. `TC-RBAC-03`: 員工唯讀限制：以 `ROLE_EMPLOYEE` 對 `/employee` 發送 POST，強制攔截回傳 HTTP 403；發送 GET 正常回傳 200。
  4. `TC-RBAC-04`: 多角色權限合併：用戶兼具經理與員工角色，權限正常取聯集。
  5. `TC-RBAC-05`: UI 標籤渲染：驗證角色列表與用戶列表 HTML 輸出包含 `class="tag-badge tag-admin"` 圓角樣式。

---

### 📊 Phase 3: 多資料庫連線探針與文字化 ERD 測試 (BRD-CSP-SRCH-002)
- **測試目標**：驗證 JDBC Metadata 抓取與文字化 ERD 緊湊序列化。
- **測試套件**：
  * `DataSourceProbeTest.java`
  * `TextualErdGeneratorTest.java`
- **測試用例**：
  1. `TC-ERD-01`: 連線探針：配置 H2/MySQL 測試連線，成功擷取全庫 Tables 與 Foreign Keys。
  2. `TC-ERD-02`: 白名單過濾：勾選 2 張表，未勾選之表強制不可見。
  3. `TC-ERD-03`: 文字化 ERD 序列化格式：驗證輸出緊湊格式包含 `[TABLE: ...]`, `[COLUMNS: ...]`, `[RELATIONSHIPS: ...]`，單表體積 $\le 5	ext{KB}$。
  4. `TC-ERD-04`: Lucene 索引寫入：文字化 ERD 寫入 Lucene，檢索查詢耗時 $\le 5	ext{ms}$。

---

### 🔒 Phase 4: Text-to-SQL 雙軌縱深安全防線測試 (BRD-CSP-SEC-005, BRD-CSP-SRCH-004)
- **測試目標**：執行期 AST 門禁 100% 阻斷破壞性與拖庫語句；Outlines FSM 語法約束。
- **測試套件**：
  * `LlmSqlGuardASTTest.java`
  * `OutlinesSqlGrammarTest.java`
- **測試用例**：
  1. `TC-SQL-01`: 破壞性語句攔截：測試 `DROP TABLE users;`, `TRUNCATE TABLE log;`, `DELETE FROM orders;`，100% 熔斷回傳 403。
  2. `TC-SQL-02`: 恆真式注入攔截：測試 `SELECT * FROM tbl WHERE 1=1;` 與 `OR 'a'='a'`，100% 阻斷。
  3. `TC-SQL-03`: 自動注入 LIMIT：測試未帶分頁之 `SELECT * FROM tbl;`，自動改寫為 `SELECT * FROM tbl LIMIT 50;`。
  4. `TC-SQL-04`: 跨權限資料表攔截：非管理員嘗試查詢 `sys_user`，AST 解析命中未授權表，強制攔截回傳 403。
  5. `TC-SQL-05`: Outlines FSM 語法約束：驗證輸出 SQL 零 Markdown 標籤、零語法殘缺。
  6. `TC-SQL-06`: 地端內網隔離與公網熔斷：配置外部公網 IP/域名發起 Text-to-SQL，驗證系統 100% 熔斷攔截並拒絕傳輸企業 Schema。

---

### 🎨 Phase 5: 私域術語定義與語意引力畫布測試 (BRD-CSP-SRCH-003)
- **測試目標**：驗證 Faruqui 20 步凸優化流形微調演算法之收斂性與相似度提升。
- **測試套件**：
  * `RetrofitCalibrationEngineTest.java`
- **測試用例**：
  1. `TC-TERM-01`: 收斂時間門禁：20 步座標上升迭代，總計算耗時必須 $\le 50	ext{ms}$。
  2. `TC-TERM-02`: 相似度拉近驗證：將「黑豆」與「氣缸床墊片」綁定同義約束 (+1.0)，微調後兩者向量餘弦值必須 $\ge 0.90$。
  3. `TC-TERM-03`: 語意排斥驗證：將無關詞彙設為排斥 (-1.0)，微調後餘弦值下降，驗證排斥引力有效性。

---

## 🚀 3. CI/CD 自動化執行流程 (Automation Pipeline)

在本地終端或 GitHub Actions 構建腳本中，執行標準全量檢驗：

```bash
# 1. 編譯與靜態規約檢查
mvn clean compile p3c-pmd:check

# 2. 執行全量單元測試並產生覆蓋率報告
mvn test jacoco:report

# 3. 檢查覆蓋率是否達標 (Line >= 80%, Branch >= 75%)
mvn jacoco:check
```
