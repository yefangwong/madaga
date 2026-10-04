# 📋 商業需求文件 (Business Requirements Document - BRD)
## 專案名稱：Cornelius Service Platform (CSP) & csp-portal-web 運營入口平台

> **文件版本**：v1.0.0  
> **發布日期**：2026-10-03  
> **維護單位**：Madaga CSP 架構與產品小組  
> **規範標準**：遵守 ADR-0005 (需求管理中心與雙向寫回原則) 與敏捷 User Story / 量化驗收標準 (AC) 規範  
> **實體代碼倉庫錨定**：`/Users/yefangwong/Documents/GitHub.nosync/madaga/csp`

---

## 📌 1. 專案背景與目標 (Project Context & Objectives)

Cornelius Service Platform (`CSP`) 之入口模組 `csp-portal-web` 提供企業後台管理、AI 算力調度、服務流程編排與權限隔離之運營介面。為了落實企業級 RBAC (Role-Based Access Control) 與零信任安全架構，系統需提供完備的角色與權限治理機制，支援靈活的模組權限分配、用戶多角色綁定，以及現代化直觀的前端視覺化呈現（圓角色塊標籤 Tag）。

---

## 🎯 2. 核心利害關係人與使用者角色 (User Personas)

| 角色代碼 (Role Code) | 角色名稱 (Role Name) | 職責與使用情境 (Responsibilities & Context) |
| :--- | :--- | :--- |
| `ROLE_ADMIN` | **系統管理員** | 擁有全系統最高權限，負責全量模組的增刪改查、系統用戶授權、角色劃分、權限資源 URL 維護與日誌稽核。 |
| `ROLE_MANAGER` | **部門經理** | 負責所屬部門之員工管理與部門組織架構維護，可對員工與部門模組進行增刪改查，具備日常通用管理權限。 |
| `ROLE_EMPLOYEE` | **普通員工** | 僅能登入系統查看個人及所屬工作區域之資訊，對員工頁面僅具唯讀 (Read-Only) 查詢權限，嚴禁增刪改操作。 |

---

## 📝 3. 敏捷使用者故事與量化驗收標準 (Agile User Stories & Acceptance Criteria)

### 3.1 BRD-CSP-SEC-001: 系統模組權限字典與列表查詢 (Permission Resource Management)
* **User Story**:
  > **As a** 系統管理員 (`ROLE_ADMIN`)  
  > **I want to** 在權限管理模組的新增與編輯表單中輸入資源名稱、代碼與對應的資源 URL，並在清單中直觀檢視  
  > **So that** 系統能將後端 Spring Security 路由端點與功能權限明確綁定，便於資安審計與權限動態校驗。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-001-1] 表單欄位擴充**：權限管理介面之「新增權限」與「修改權限」表單必須包含 `name` (權限名稱)、`code` (權限代碼，如 `employee`, `department`) 與 `url` (資源 URL，例如 `/employee/**`) 必填欄位。
  2. **[AC-001-2] 清單欄位展示**：權限清單表格中必須包含「權限名稱」、「權限代碼」與「對應 URL」欄位，且 100% 正確呈現後端資料庫 `sys_permission.url` 內容。
  3. **[AC-001-3] 唯一性防呆校驗**：當管理員輸入已存在的 `code` 或重複的 `url` 提交時，後端驗證邏輯必須攔截並回傳 HTTP 400 與錯誤碼 `ERR_PERMISSION_DUPLICATE`，禁止寫入。
  4. **[AC-001-4] 初始權限字典覆蓋率**：系統初次初始化腳本中，必須具備 6 大核心權限字典：`employee` (/employee/**), `department` (/department/**), `sysUser` (/sysUser/**), `sysRole` (/sysRole/**), `sysPermission` (/sysPermission/**), `common` (/dashboard/**, /profile/**)。

---

### 3.2 BRD-CSP-SEC-002: 核心角色定義與分級權限控制 (Role Definition & Hierarchical RBAC)
* **User Story**:
  > **As a** 平台資安主管  
  > **I want to** 將系統角色嚴格劃分為管理員、經理與普通員工，並精確分配對應模組的增刪改查存取限制  
  > **So that** 杜絕垂直越權與水平越權風險，確保非授權使用者無法竄改敏感資料。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-002-1] 角色與權限映射閉環**：
     * `ROLE_ADMIN` (管理員)：映射全部 6 項權限 (`employee`, `department`, `sysUser`, `sysRole`, `sysPermission`, `common`)，具備全模組所有 HTTP Methods (GET, POST, PUT, DELETE) 之完整操作權。
     * `ROLE_MANAGER` (經理)：映射 3 項權限 (`employee`, `department`, `common`)，僅允許對員工與部門進行增刪改查，存取用戶或權限管理端點時強制攔截回傳 HTTP 403。
     * `ROLE_EMPLOYEE` (普通員工)：映射 1 項權限 (`common`)；對 `/employee/**` 端點僅能發送 GET 查詢，發送 POST/PUT/DELETE 時強制攔截回傳 HTTP 403。
  2. **[AC-002-2] API 攔截延遲門禁**：Spring Security 授權過濾器對任何越權請求的攔截判定延遲必須低於 1.5ms，並記錄稽核日誌 (Audit Log)。
  3. **[AC-002-3] 前端動態功能表隱藏**：前端 Thymeleaf 模板整合 `thymeleaf-extras-springsecurity6`，經理與員工登入後，側邊欄不得渲染其無權訪問之選單節點 (`sec:authorize="hasRole(...)"`)，防止無效點擊。

---

### 3.3 BRD-CSP-SEC-003: 角色新增綁定權限與標籤化列表展示 (Role-Permission Binding & Tag Visualization)
* **User Story**:
  > **As a** 系統管理員 (`ROLE_ADMIN`)  
  > **I want to** 在角色管理模組新增角色時勾選關聯的多項權限，並在角色清單中以醒目的圓角色塊呈現其擁有的權限  
  > **So that** 營運人員能一目了然各角色的權能範疇，減少授權排查的時間成本。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-003-1] 新增角色多選權限**：角色新增表單中提供多選核取方塊 (Checkbox)，提交時以交易原子性同步寫入 `sys_role` 與 `sys_role_permission` 中間表。
  2. **[AC-003-2] 圓角色塊標籤 UI 渲染**：角色管理清單表格的「權限」欄位中，每個關聯權限名稱必須以獨立的自適應圓角色塊（Badge / Tag）呈現。
     * CSS 規範：`display: inline-block; padding: 2px 10px; border-radius: 12px; font-size: 12px; margin-right: 4px;`。
     * 語意色調：管理員權限標籤採用靛藍/深色 (`badge-primary`)，業務權限採用藍綠/薄荷色 (`badge-info`)。
  3. **[AC-003-3] 角色清單關聯查詢效能**：查詢 100 筆角色資料及其關聯權限標籤時，SQL 必須透過 `LEFT JOIN` 一次性撈取或透過 Batch 查詢完成，禁止發生 N+1 查詢效能問題，清單總體 API 響應時間必須 $\le 50\text{ms}$。

---

### 3.4 BRD-CSP-SEC-004: 用戶新增多角色綁定與標籤化列表展示 (User Multi-Role Binding & Tag Visualization)
* **User Story**:
  > **As a** 系統管理員 (`ROLE_ADMIN`)  
  > **I want to** 在新增用戶時為其指派一個或多個角色，並在用戶清單中以圓角色塊標籤展示所有關聯角色  
  > **So that** 滿足兼職、代理或多部門職務的彈性權限指派需求，並清晰查驗用戶的所有角色。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-004-1] 支援 1 對多角色模型**：系統資料庫支援 `sys_user_role(user_id, role_id)` 多對多關聯；在新增用戶時，管理員可選擇 1 個至多個有效角色。
  2. **[AC-004-2] 圓角色塊標籤呈現**：用戶清單表格的「角色」欄位中，關聯的多個角色名稱必須以圓角色塊標籤（Tag Badge）匡列渲染：
     * `ROLE_ADMIN` 標籤：紅色/琥珀色高光標籤（醒目防呆）。
     * `ROLE_MANAGER` 標籤：深藍色標籤。
     * `ROLE_EMPLOYEE` 標籤：灰色/綠色標籤。
  3. **[AC-004-3] Security 上下文權限合併**：`UserDetailsServiceImpl` 載入用戶憑證時，必須合併該用戶名下所有角色的所有權限代碼，並賦予所有對應的 `ROLE_*` GrantedAuthority，確保多角色權限聯集（Union）生效。
  4. **[AC-004-4] 密碼單向加密存儲**：用戶新增與密碼修改時，密碼強制使用 `BCryptPasswordEncoder` (強度 10) 雜湊加密後存入 `sys_user.password`，明文密碼嚴禁落地或寫入日誌。

---

### 3.5 BRD-CSP-SRCH-001: 技師手冊與全站文件 Lucene 9 嵌入式檢索原型 (Embedded Lucene Technical Doc Search)
* **User Story**:
  > **As a** 保修廠現場技師 (`ROLE_EMPLOYEE`) 或 服務廠組長 (`ROLE_MANAGER`)  
  > **I want to** 在後台首頁 Dashboard 頂部搜尋框輸入車輛故障代碼 (DTC 如 `P0115`)、零件料號 (如 `ME223120`) 或維修關鍵字（如 `煞車`、`DPF`、`汽門室蓋`）  
  > **So that** 系統能於地端極速檢索並高亮呈現技師維修手冊與技術文件之命中章節、故障排除 SOP 與頁碼預覽，無需仰賴昂貴外部雲端服務或高硬體門檻之 Elasticsearch 叢集，並杜絕零件料號與故障碼在神經向量下漂移誤判的風險。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-SRCH-001-1] 頂部搜尋框互動綁定**：後台佈局模板 (`layout/default.html`) 頂部導航列與手機側邊欄之搜尋輸入框，必須賦予唯一 ID（`#globalSearchInput`、`#mobileSearchInput`），支援鍵入關鍵字後按下 `Enter` 鍵或點擊搜尋圖示，觸發 Ajax 發送至後端端點 `/api/v1/search`，並以彈出式 Modal (`#searchResultModal`) 或懸浮面板流暢呈現前 10 筆檢索結果。
  2. **[AC-SRCH-001-2] 嵌入式 Lucene 9 引擎整合**：後端核心模組內嵌 Apache Lucene 9.9.2 (`lucene-core`, `lucene-queryparser`, `lucene-analysis-common`, `lucene-highlighter`)，採用記憶體映射 `MMapDirectory` 進行索引分段讀寫，啟動與檢索過程零外部依賴、純地端本機執行，符合 ISO 27001 資安隔離規範。
  3. **[AC-SRCH-001-3] PDF 技術手冊索引管線**：整合 Apache PDFBox 3.0.1，支援將 `data/docs/` 或資源目錄下之車輛維修指引、零件規格書 PDF 自動解析為章節級 Documents，結構化儲存欄位：`docId` (文件識別碼), `title` (手冊名稱), `category` (車型/類別), `chapter` (章節名稱), `pageNumber` (手冊實體頁碼), `content` (正文純文字), `lastModified` (更新時間戳記)。
  4. **[AC-SRCH-001-4] 零件號與故障碼精確命中門禁**：針對標準車用故障代碼（如 `P0115`, `P0420`、`CAN-BUS`）與零件料號（如 `ME223120`），檢索權重必須支援 Exact Term Filter，保證 100% 精準命中；在 10,000 頁手冊基準下，Lucene 檢索查詢耗時門禁必須 $\le 15\text{ms}$。
  5. **[AC-SRCH-001-5] 關鍵字上下文高亮標示 (Snippet Highlighting)**：搜尋結果摘要中，命中之關鍵字必須透過 Lucene Highlighter 以 `<mark class="cui-highlight">` 醒目標籤匡列，並動態截取關鍵字前後 60 字元之完整語意片語，方便技師在手機或平板上快速瀏覽決策。
  6. **[AC-SRCH-001-6] 去 Spring 污染與 Clean-Room 規範**：檢索底層業務邏輯單元 `DocSearchBL` 必須繼承純 Java `BaseBL<SearchRequest, PageResult<SearchResultVO>>`，符合開源潔淨室規範，BL 內部零 Spring 容器導入。

---

### 3.6 BRD-CSP-SRCH-002: 多資料庫連線探針與文字化 ERD 白名單治理 (Dynamic DataSource & Textual ERD Whitelist)
* **User Story**:
  > **As a** 企業系統管理員 (`ROLE_ADMIN`) 或 資料庫架構師  
  > **I want to** 在後台介面配置外部企業資料庫連線，一鍵探針抓取所有資料表，並以穿梭框/核取清單勾選授權表，由系統自動將勾選資料表之結構、主外鍵關聯與欄位註解序列化為緊湊的「文字化 ERD (Textual ERD)」  
  > **So that** 建立嚴格的 AI 查詢白名單防線杜絕機密洩漏，並透過極簡的文字化 ERD 大幅降低檢索索引體積、提升 Text-to-SQL 動態 Schema 尋路速度與生成精準度。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-SRCH-002-1] 動態資料源連線探針**：管理介面提供 JDBC 連線配置表單（支援 MySQL, PostgreSQL, Oracle, DB2 等主流驅動），點擊「測試連線」驗證連通性；後端透過標準 `DatabaseMetaData` 自動抓取全庫資料表清單、註解 (REMARKS) 與主外鍵關聯。
  2. **[AC-SRCH-002-2] 視覺化資料表白名單勾選**：前端以穿梭框 (Transfer) 或表格呈現全庫資料表，管理員勾選要開放給 AI 查詢之特定業務表（例如 `tbl_parts`, `tbl_work_order`），其餘未勾選表（如薪資、密碼表）強制隔離不可見。
  3. **[AC-SRCH-002-3] 文字化 ERD 自動序列化 (Textual ERD Generation)**：系統自動解析所勾選資料表之 Schema、主鍵 (PK)、外鍵 (FK) 與欄位註解，序列化為極簡 Markdown / 文字化 ERD 結構，緊湊呈現 `[TABLE: ...]`, `[COLUMNS: name(PK/FK, type, remarks)]`, `[RELATIONSHIPS: A.fk >-- B.pk]`。
  4. **[AC-SRCH-002-4] 文字化 ERD 檢索索引寫入**：文字化 ERD 支援一鍵發布至 Lucene 9 索引庫，單表文字化 Schema 索引體積必須 $\le 5\text{KB}$，索引建置耗時 $\le 20\text{ms}$，檢索尋路耗時 $\le 5\text{ms}$。
  5. **[AC-SRCH-002-5] Text-to-SQL 動態 Prompt 注入閉環**：`Synthesizer` 接收使用者問題後，先自文字化 ERD 索引庫檢索出最高相關之 2~3 張授權表結構，動態替換既有 hardcoded Schema，輸入給 LLM 之 Token 消耗減少 $80\%$ 以上。

---

### 3.7 BRD-CSP-SRCH-003: 私域術語定義與語意引力畫布 (Semantic Canvas & TermAlign Studio)
* **User Story**:
  > **As a** 領域專家（如汽車保修廠組長或資深技師）  
  > **I want to** 在後台介面自行新增私域 Term（如俚語、黑話 `黑豆`、`鳥仔蓋`），並在畫面上透過單軸滑桿或二維語意象限畫布直觀定義 Term 與標準零件名詞之相似度/關聯度  
  > **So that** 系統能即時將人類隱性經驗轉化為幾何引力約束，消除神經稠密向量之餘弦混淆，讓技師使用黑話檢索手冊或查詢零件資料庫時達到 100% 精準命中。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-SRCH-003-1] 私域 Term 詞庫維護**：介面提供術語自定義清單，支援新增、修改、刪除私域詞彙及其對應之標準零件/領域代碼。
  2. **[AC-SRCH-003-2] 視覺化語意引力調整介面**：
     - 單軸模式 (1D Elastic Slider)：提供 $-1.0$ (反義/排斥) 至 $+1.0$ (絕對同義) 之連續滑桿。
     - 二維畫布模式 (2D Semantic Canvas)：提供 X 軸 (相似度 Similarity: Is-A) 與 Y 軸 (關聯度 Relatedness: Has-A/共現) 之四象限拖曳畫布，拖曳泡泡即時產生物理引力連線。
  3. **[AC-SRCH-003-3] 毫秒級凸優化流形微調 (Faruqui Retrofitting)**：前端產出之術語權重矩陣，後端調用 Faruqui 座標上升演算法進行 20 步凸優化矩陣迭代，CPU 計算耗時門禁 $\le 50\text{ms}$，即時校準本地詞向量空間與 Lucene 同義詞庫，零 GPU 重訓成本。
  4. **[AC-SRCH-003-4] 黑話檢索命中率門禁**：校準後，輸入「黑豆」對維修手冊「氣缸床墊片 (Cylinder Head Gasket)」章節之檢索排名必須為 Top-1，且語意相似度評分提升至 $\ge 0.90$。

---

### 3.8 BRD-CSP-SEC-005: Text-to-SQL 執行期 AST 剛性安全閘門 (Execution-Time AST Security Guardrail - llm-sql-guard)
* **User Story**:
  > **As a** 企業資安主管 (CISO) 或 資料庫管理員 (DBA)  
  > **I want to** 在後端 `SqlController` 執行 ChatGPT 生成之 SQL 前，以抽象語法樹 (AST) 進行 5 道剛性安全策略審計與動態重寫  
  > **So that** 徹底杜絕 DDL 破壞、拖庫注入、無分頁慢查 OOM 及橫向越權風險，確保動態查詢 100% 安全合規。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-SEC-005-1] DDL / 毀滅性 DML 零容忍攔截**：任何包含 `DROP`, `TRUNCATE`, `ALTER`, `RENAME`, `DELETE`, `GRANT` 之 SQL，語法樹檢查命中率必須為 100%，微秒級熔斷並回傳 HTTP 403 `ERR_SQL_FORBIDDEN_OPERATION`。
  2. **[AC-SEC-005-2] 恆真式注入阻斷**：檢測到 `1=1`, `'a'='a'`, `OR true` 等恆真式條件時，強制攔截並記錄安全告警，阻止非授權全表掃描。
  3. **[AC-SEC-005-3] 強制分頁注入 (Safety Limit Enforcement)**：若生成的 SELECT 語句未聲明 `LIMIT`，AST 剖析器強制自動改寫追加 `LIMIT 50`，防止大表百萬筆慢查導致後端 JVM OOM 當機。
  4. **[AC-SEC-005-4] 角色權限動態表白名單**：依據當前登入者 SecurityContext 之角色 (`ROLE_ADMIN` vs `ROLE_EMPLOYEE`)，校驗 SQL 中涉及的所有 Table。若員工角色試圖查詢管理表或薪資表，強制攔截回傳 HTTP 403。
  5. **[AC-SEC-005-5] AST 審計效能門禁**：單條 SQL 之 AST 剖析、校驗與改寫總耗時門禁必須 $\le 2\text{ms}$，不增加整體查詢延遲。

---

### 3.9 BRD-CSP-SRCH-004: 生成期有限狀態自動機語法約束 (In-flight FSM Syntax Constraint - Outlines)
* **User Story**:
  > **As a** 平台 AI 系統架構師  
  > **I want to** 在 LLM 生成 SQL 或結構化結果的推論關鍵路徑上，整合基於有限狀態自動機 (FSM) 的生成期語法約束機制  
  > **So that** 在 Token 生成期即時進行常數時間 Logits 硬遮罩 ($-\infty$)，根本杜絕 Markdown 廢話、缺失分號與方言錯誤，保證輸出之 SQL 100% 語法合法。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-SRCH-004-1] 語法狀態機離線編譯**：將 SQL EBNF 文法與目標模型分詞器詞表離線求交集，預先編譯為狀態轉移索引，線上查表時間為 $\mathcal{O}(1)$。
  2. **[AC-SRCH-004-2] Logits 硬遮罩零語法錯誤率**：在 Token 生成關鍵路徑進行硬遮罩，生成之 SQL 語法合法率必須達 100%，嚴禁輸出任何 Markdown 標籤（如 ` ```sql `）或非 SQL 解說文字。
  3. **[AC-SRCH-004-3] 生成延遲門禁**：每個 Token 遮罩運算開銷必須 $\le 0.8\text{ms}$，無正則表達式回溯拖慢 GPU/CPU 推論流。

---

---

### 3.10 BRD-CSP-SEC-006: 基礎框架與依賴資安弱點治理 (Base Framework & Dependency CVE Remediation)
* **User Story**:
  > **As a** 平台架構師與資安治理主管  
  > **I want to** 對 Cornelius CSP 之父層 POM 與前端工具鏈進行同版本線安全補丁升級 (Safe Patching)，並剔除歷史冗餘依賴與結合 PatchVerify 可達性過濾  
  > **So that** 在保證零編譯破壞 (Zero-Breaking Change) 的前提下，一舉消滅 GitHub Dependabot 160 項告警中超過 75% 的 Critical/High 漏洞，達成企業合規安全基線。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-SEC-006-1] 內嵌 Tomcat 容器補丁升級**：在 `csp/pom.xml` 將 `<tomcat.version>` 自 `10.1.16` 升級至 `10.1.34+` 安全修訂版，一舉消滅 33 項 Tomcat 相關 CVE（包含 4 項 Critical、16 項 High），且 Web 請求路由與過濾器功能 100% 保持相容。
  2. **[AC-SEC-006-2] Jackson 反序列化安全加固**：將 `<jackson.version>` 自不穩定開發版 `2.16.0-SNAPSHOT` 升級至官方穩定正式版 `2.16.2` 或 `2.17.2`，清空泛型多態反序列化繞過 (Generic Bypass) 與數字長度 DoS 漏洞。
  3. **[AC-SEC-006-3] 前端 Axios 原型污染與 SSRF 封堵**：在前端專案 `csc/nlp2sql-app` 升級 `axios` 至 `1.7.4+`，消除 27 項 Axios 漏洞，並保證既有 API 請求與攔截器功能運作正常。
  4. **[AC-SEC-006-4] 歷史依賴精準處置原則**：移除根目錄 `pom.xml` 中未被任何子模組使用之 `commons-fileupload` 依賴；針對 `csp-webframeworx-web` 既有代碼引用之 `org.apache.tiles`，嚴禁貿然刪除導致編譯報錯，強制產出 PatchVerify CPG 可達性豁免報告以證明執行期不可達。
  5. **[AC-SEC-006-5] 回歸測試與告警消除率門禁**：依賴升級與清理後，執行 `mvn clean test` 單元測試全量通過率必須為 100%，GitHub Dependabot 總告警消除率必須 $\ge 75\%$。

---

---

### 3.11 BRD-CSP-SEC-007: 企業機敏 Schema 防洩漏與地端內網 LLM 隔離推論 (Air-Gapped On-Premises LLM Inference & Schema Zero-Leakage Gate)
* **User Story**:
  > **As a** 企業資安主管 (CISO) 與 資料治理合規稽核員  
  > **I want to** 將包含企業資料表名稱 (Table Names)、欄位名稱 (Column Names)、關聯主外鍵與業務註解之文字化 ERD 提示詞，強制隔離於地端內網 (Air-Gapped / Intranet) 的本地大語言模型 (如地端 Ollama、vLLM 或 Cornelius 本地算力節點)，嚴禁發送至外部公網雲端 AI API  
  > **So that** 徹底杜絕企業核心資料資產結構、資料庫拓撲與業務架構在生成式 AI 推論過程中外洩至第三方公有雲，100% 符合 ISO 27001、GDPR 與企業營業秘密保護規範。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-SEC-007-1] 提示詞公網外發剛性熔斷 (Outbound Air-Gap Circuit Breaker)**：在任何攜帶文字化 ERD、資料表 Schema 或自訂術語之 Text-to-SQL 推論通道上，實施連線網路邊界預檢。若推論目標端點 URL 屬於外網域名或非私有 IP 網段，系統強制熔斷中斷連線，並拋出 `ERR_SECURITY_OUTBOUND_SCHEMA_LEAK_FORBIDDEN`，杜絕任何失誤配置。
  2. **[AC-SEC-007-2] 本地/內網 LLM 推論端點適配器 (Local LLM Provider Adapter)**：後端推論服務整合標準本地通訊適配器，支援業界主流地端無網推論框架：
     - 本地 Ollama API (`http://localhost:11434/api/generate` 或內網伺服器)。
     - 本地 vLLM / LocalAI 之 OpenAI-Compatible API (`http://192.168.x.x:8000/v1`)。
     - 整合專案既有之 `LocalHeterogeneousDriver` (RTX 3070 + RX 6600 XT 雙卡) 本地算力驅動。
  3. **[AC-SEC-007-3] 動態端點配置與健康心跳檢測**：在設定檔與管理介面提供 `csp.ai.llm.base-url` 配置，預設強制指向內網；系統啟動與發起推論前執行心跳健康檢查 (`/health` 或 `/api/tags`)，連通逾時時間 $\le 200\text{ms}$。
  4. **[AC-SEC-007-4] 離線推論效能與 Token 限制**：地端 LLM 生成 SQL 之每秒 Token 產出率 (TPS) 必須 $\ge 15\text{ tokens/s}$，首字響應延遲 (TTFT) 必須 $\le 1.5\text{s}$，確保在地端硬體下兼顧極致隱私與流暢互動體驗。
  5. **[AC-SEC-007-5] 地端算力之電信 OSS 資源層抽象解耦 (Compute as Telecom OSS Resource Abstraction)**：將地端推論算力（GPU 顯存、本地推論節點）嚴格類比於電信 OSS 之「通訊與頻寬資源層 (Resource Layer)」，歸入 TMF SID `resource/` 領域 (`net.yefangwong.csp.domain.resource` / `tbl_res_*`)。Text-to-SQL 業務層 (`service/`) 調用地端 LLM 時，強制透過 `IComputeResourceProxy` 與 `CapabilityRouter` 進行動態算力配額申請、節點心跳檢測與 VRAM 容量排程，嚴禁業務邏輯直接硬編碼綁定底層物理顯卡或特定伺服器。

---

### 3.12 BRD-CSP-RES-001: 電信級算力頻寬產品化自訂與邊緣硬體隨插即用擴充 (Telecom-Grade Compute Bandwidth Catalog & Edge Scale-Out)
* **User Story**:
  > **As a** 企業 IT 運營長 (COO) 與 平台資源管理員 (`ROLE_ADMIN`)  
  > **I want to** 在 CSP 後台如同挑選「電信寬頻產品 (如 100M/300M/500M 方案)」般，自由為不同租戶、部門或關鍵業務自訂「AI 算力頻寬等級 (Compute Bandwidth Tiers)」，並支援在企業內網隨插即用接入「輝達小電腦 (如 NVIDIA DGX Spark / Jetson Orin 邊緣運算盒)」進行橫向動態擴充  
  > **So that** 企業能依業務輕重緩急進行 Token 流量整形與 QoS 排程，杜絕多人併發提問時 GPU 顯存被瞬間打爆 (CUDA OOM)，並讓算力能夠像電信通訊資源一樣隨業務規模平滑成長、隨需擴展。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-RES-001-1] 算力頻寬產品化目錄 (Compute Bandwidth Catalog)**：
     - 傳承經典電信 Catalog 架構精神，採用「左樹右表 (Catalog Tree)」與版本演進治理（支援 `Ver. 001` 時間軸 `Efftv Date` 與「新增版本」防覆蓋機制）。
     - 提供可自訂特徵值 (CharacteristicSpec)，定義算力等級之核心指標：`TokenBandwidth` (例如：Bronze 20 T/s, Silver 50 T/s, Gold 100+ T/s)、`MaxConcurrentSlots` (併發推論槽位數)、`GuaranteedVramGb` (最低保證顯存)。
  2. **[AC-RES-001-2] 電信級 Token 流量整形與 QoS 優先級排隊 (Traffic Shaping & QoS Queuing)**：
     - 核心推論通道實作電信級「漏桶算法 (Leaky Bucket)」流量整形器，各租戶發送推論請求時嚴格限制在簽約的 Token 頻寬之內。
     - 突發流量進入優先級 QoS 佇列排隊平滑釋放，保證高優先級業務（如財務長即時報表）零延遲，平價或批次查詢循序排隊，GPU 顯存峰值利用率嚴格壓制在 90% 安全水位以下。
  3. **[AC-RES-001-3] 輝達小電腦 (DGX Spark) 邊緣節點 3 秒動態發現與熱插拔擴充 (Plug & Play Edge Scale-Out)**：
     - 底層推論資源層適配專案既有之 `NvidiaDgxSparkDriver` 與 `CapabilityRouter`。
     - 當內網接入新的 NVIDIA DGX Spark (128GB Unified Memory) 或 Jetson 節點時，系統心跳探針在 $\le 3\text{s}$ 內完成拓撲自動註冊與顯存池併網，當 Text-to-SQL 任務顯存需求超過單卡容量 (`taskData.getRequiredVramGb() > 16`) 時，自動零中斷路由轉移至 Spark 節點。
  4. **[AC-RES-001-4] 剛性模型驗證門禁 (Catalog Validation Gate)**：
     - 在算力方案與資源節點發布至生產環境前，管理介面提供獨立「驗證 (Validation)」按鈕，一鍵執行節點連通性 (RTT $\le 10\text{ms}$)、驅動握手與顯存水位預檢，全綠燈方可啟用生效。

---

### 3.13 BRD-CSP-UI-001: 經典五大方塊工作台：以 Knowledge 為核心主舞台、Database 為按需外掛、Test 為終端門禁 (Classic 5-Tile Architectural Studio Navigation)
* **User Story**:
  > **As a** 領域科學家、業務分析師或系統架構師  
  > **I want to** 在 CSP 入口平台左側使用經典垂直方塊（上 3/4 幾何圖示 ＋ 下 1/4 清晰文字標籤）工作台，預設直達開箱即用的 **Knowledge (核心主舞台)** 檢視 Embedding 空間與即時 Token 映射，並可按需切換 **Domain (領域精靈)**、**Database (可選外掛與 SQL 組裝)**、**Configuration (預設算力與模型)**、**Test (剛性驗證與交付門禁)** 五大核心專屬工作區  
  > **So that** 徹底擺脫傳統「以資料庫為中心」的沉重束縛，在零資料庫負擔下自由探索語意向量空間，並確保任何資料庫查詢皆經過 Test 剛性驗證後才安全交付至 Excel 或 RPA。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-UI-001-1] 經典方塊黃金比例視覺渲染**：
     - 左側導覽列寬度介於 `80px ~ 90px`，每個功能按鈕為高度約 `84px ~ 90px` 之獨立方塊 (Tile)。
     - 方塊上方 3/4 高度區域置入向量高質感圖標（選中時呈現琥珀金/薄荷綠微光）；下方 1/4 高度置入專用英文標籤（`Domain`, `Knowledge`, `Database`, `Config`, `Test`），字體採用 Montserrat，字距擴展 +0.5px。
  2. **[AC-UI-001-2] 五大專屬工作區職能歸位與分級定義**：
     - **【Knowledge】（核心主舞台，必選 Mandatory）**：系統核心基石。使用者進入工作台預設直接呈現所選領域的 Embedding 空間。使用者可隨時新增自訂 Token / Term（如「生魚片」、「鮭魚」、「二硫化鉬」），後端調用預設 Embedding 引擎即時推論，並透過降維演算法在 2D Vector Space 畫布上即時浮現節點坐標，支援引力拉動與凸優化微調。
     - **【Database】（按需外掛，可選 Optional）**：資料庫並非前置阻礙，設為可選外掛。僅在需要串接企業實體庫時接入，提供標準 JDBC 連線探針與 `[ 🛠️ Build SQL ]` 觸發按鈕。點擊後啟動穿梭框精靈，其職能**嚴格止於計算並產出純淨 SQL AST，不越權直接產出 Excel 或執行 RPA 拋轉**。
     - **【Configuration】（開箱即用預設，Default Ready）**：提供預設的地端算力（如本地 RTX 雙卡、Jetson、DGX Spark 或內網 Ollama/vLLM）與預設垂直領域高品質 Embedding 模型（如 `bge-small-zh-v1.5`）。使用者無需繁複設定即可開箱直視語意空間；同時提供模型切換、電信級算力頻寬目錄與外網熔斷狀態監控。
     - **【Domain】（領域維護）**：單純維護本工具應用的垂直領域（如生鮮食品、材料科學、車輛工程），支援領域的新增、修改、刪除與一鍵啟動切換。
     - **【Test】（剛性驗證與終極交付門禁，Gate & Delivery）**：承接 Database `Build SQL` 所產出的 SQL，在此進行 AST 剛性安全審計、防慢查、表權限白名單與 Dry Run 綠燈體檢。**唯有在 Test 方塊通過審查獲得綠燈後，才正式解鎖並執行 `[ 📥 匯出 Excel ]` 與 `[ 🤖 拋轉 RPA 自動化 ]`！**
  3. **[AC-UI-001-3] 方塊微動效與狀態回饋**：滑鼠懸停 (Hover) 時方塊以 `transition: all 0.2s ease` 呈現浮起微光；當前選中 (Active) 狀態左側帶有 4px 高光指示條，切換反應延遲必須 $\le 16\text{ms}$ (60 FPS)。

---

### 3.14 BRD-CSP-RPT-001: AI 引導式穿梭框 SQL 組裝精靈與 Test 門禁交付動線 (AI-Guided Shuttle SQL Builder & Test Gate Delivery)
* **User Story**:
  > **As a** 企業業務經辦、財務精算員或廠區服務組長  
  > **I want to** 在 Database 模組點擊 `[ 🛠️ Build SQL ]` 後，由 AI 自動在「三步穿梭框精靈」中預選資料表、預排直行欄位與預填過濾條件，並由人類透過雙向按鈕 (`-->` / `<--`) 視覺確認後產出乾淨 SQL，再傳送至 Test 方塊進行剛性安全驗證與解鎖匯出 Excel / RPA  
  > **So that** 徹底消除 LLM 直接生成 SQL 的機率漂移與隨機性災難，落實「Build SQL 專注計算組裝、Test 統一把關交付」的架構分權，杜絕未經檢驗的查詢直接衝擊產線或 ERP。

* **量化驗收標準 (Acceptance Criteria)**:
  1. **[AC-RPT-001-1] 三步穿梭框 SQL 組裝動線閉環**：
     - **Step 1 (資料表穿梭)**：左側列出白名單業務表，點擊 `-->` / `<--` 移入移出；下方依據外鍵拓撲自動呈現 Join 預覽，防範幽靈造表。
     - **Step 2 (欄位投影與謂詞過濾)**：選中欄位支援滑鼠垂直拖曳直觀排定欄位順序；下半部提供結構化 WHERE 條件卡片，資料字典欄位自動連動下拉選單。
     - **Step 3 (統計計算與分組排序)**：零代碼卡片勾選 Group By 維度與 `SUM` / `AVG` / `COUNT` 指標，並直觀配置多階 Order By；點擊「組裝 SQL」產出參數化 SQL AST，並自動導引至 **【Test】方塊**。
  2. **[AC-RPT-001-2] Test 方塊剛性審查與 Excel/RPA 解鎖機制**：
     - 產出之 SQL 傳送至 Test 方塊執行 AST 剛性安全審計（5 道防線）、防慢查、權限校驗與 Dry Run 執行（注入 `LIMIT 10` 呈現高擬真網格）。
     - **綠燈解鎖**：審查 100% 通過後，介面始正式啟用 `[ 📥 匯出 Excel ]` 與 `[ 🤖 拋轉 RPA 自動化排程 ]` 按鈕；審查未通過時強制鎖定 (Disabled) 並紅字警示。
  3. **[AC-RPT-001-3] 人機協同薄荷綠呼吸光暈提示 (Pre-fill Glow Hint)**：AI 秘書解析自然語言後，自動在精靈畫面上預選之 Table、Column 與日期範圍，必須帶有淡薄荷綠邊框呼吸光暈，明確告知人類「此為 AI 建議，請確認」，落實 Human-in-the-Loop。
  4. **[AC-RPT-001-4] 結構化 JSON AST 確定性編譯與零注入**：精靈收集之資料結構為純 JSON AST，後端由純 Java 代碼確定性編譯為 SQL，變數 100% 強制使用 JDBC `PreparedStatement` 佔位符 (`?`) 綁定，阻絕任何 SQL 注入。
  5. **[AC-RPT-001-5] Excel 串流防爆記憶體門禁**：後端匯出 Excel 時，強制採用 Apache POI `SXSSFWorkbook` (記憶體窗口 $\le 500$ 列)，百萬列大報表匯出 JVM 記憶體波動嚴格限制在 128MB 以內，防杜 OOM。

---

## 🗺️ 4. 需求追蹤矩陣 (Requirements Traceability Matrix - RTM)

遵循 ADR-0005 雙向追蹤原則，所有模組實體規格與代碼皆與本需求池錨定：

| BRD 需求 ID | 需求主題 | 模組化規格 (FS/Spec) | 實體代碼 (Physical Code) | 驗證測試套件 (Tests) |
| :--- | :--- | :--- | :--- | :--- |
| `BRD-CSP-SEC-001` | 權限資源字典與 URL 維護 | [[facts/cornelius_csp_portal_security_architecture_and_maintenance_guide.md]] | `SysPermissionController.java`<br>`permission/list.html` | `SysPermissionBLTest.java` |
| `BRD-CSP-SEC-002` | 3大核心角色與分級 RBAC | [[facts/cornelius_csp_portal_security_architecture_and_maintenance_guide.md]] | `SecurityConfig.java`<br>`UserDetailsServiceImpl.java` | `SecurityRbacWebTest.java` |
| `BRD-CSP-SEC-003` | 角色新增綁定權限與圓角色塊 | [[facts/cornelius_csp_portal_security_architecture_and_maintenance_guide.md]] | `SysRoleController.java`<br>`role/list.html` | `SysRoleBLTest.java` |
| `BRD-CSP-SEC-004` | 用戶多角色指派與圓角色塊 | [[facts/cornelius_csp_portal_security_architecture_and_maintenance_guide.md]] | `SysUserController.java`<br>`user/list.html` | `SysUserBLTest.java` |
| `BRD-CSP-SEC-005` | Text-to-SQL 執行期 AST 剛性安全門禁 | [[solutions/llm_sql_guard_outlines_dual_track_architecture.md]] | `SqlController.java`<br>`LlmSqlGuard.java` | `LlmSqlGuardASTTest.java` |
| `BRD-CSP-SEC-006` | 基礎框架與依賴資安弱點治理 (CVE 修復) | [[facts/madaga_github_dependabot_security_alerts_inventory.md]] | `csp/pom.xml`<br>`csc/nlp2sql-app/package.json` | `mvn clean test`<br>`PatchVerify Reachability Scan` |
| `BRD-CSP-SEC-007` | 企業機敏 Schema 防洩漏與地端內網 LLM 隔離推論 (OSS 算力資源層) | [[docs/specs/FS_S2_N02_textual_erd_whitelist.md]]<br>[[CSP_DEVELOPER_MANUAL.md]] | `LocalLlmClient.java`<br>`IComputeResourceProxy.java`<br>`Synthesizer.java` | `LocalLlmCircuitBreakerTest.java`<br>`CapabilityRouterTest.java` |
| `BRD-CSP-RES-001` | 電信級算力頻寬產品化自訂與邊緣硬體 (DGX Spark) 隨插即用擴充 | [[WORKLOG_20261003.md]]<br>[[CSP_DEVELOPER_MANUAL.md]] | `NvidiaDgxSparkDriver.java`<br>`CapabilityRouter.java`<br>`TokenTrafficShaper.java` | `NvidiaDgxSparkDriverTest.java`<br>`TokenTrafficShaperTest.java` |
| `BRD-CSP-UI-001` | 經典五大方塊工作台 (Knowledge核心主舞台/DB可選/Config預設/Domain/Test門禁) | [[WORKLOG_20261004.md]]<br>[[layout/default.html]] | `layout/default.html`<br>`dashboard.css` | `SidebarNavigationTest.java` |
| `BRD-CSP-RPT-001` | AI 引導式穿梭框 SQL 組裝精靈與 Test 門禁交付動線 (SQL組裝與Test解鎖) | [[docs/specs/FS_S2_N03_ai_guided_shuttle_wizard.md]] | `ReportWizardController.java`<br>`ReportAstCompiler.java`<br>`report/wizard.html` | `ReportAstCompilerTest.java`<br>`ReportExportStreamTest.java` |
| `BRD-CSP-SRCH-001` | 技師手冊 Lucene 9 嵌入式檢索原型 | [[Projects/startup/madaga/specs/FS_S1_N01_embedded_lucene_search.md]]<br>[[AI_Raw/solutions/madaga_csp_portal_lucene_pdf_search.md]] | `SearchController.java`<br>`DocSearchBL.java`<br>`LuceneIndexManager.java`<br>`layout/default.html` | `DocSearchBLTest.java`<br>`LuceneIndexManagerTest.java` |
| `BRD-CSP-SRCH-002` | 多資料庫連線探針與文字化 ERD 白名單治理 (黃金 6 要素) | [[Projects/startup/madaga/specs/FS_S2_N02_textual_erd_whitelist.md]]<br>[[solutions/madaga_nlp_to_sql_governance.md]] | `DataSourceController.java`<br>`TextualErdGenerator.java`<br>`Synthesizer.java` | `TextualErdGeneratorTest.java`<br>`DataSourceProbeTest.java` |
| `BRD-CSP-SRCH-003` | 私域術語定義與語意引力畫布 (Semantic Canvas) | [[Projects/startup/madaga/specs/FS_S1_N03_semantic_canvas_studio.md]]<br>[[facts/discovery_tree_token_semantics_and_guided_generation.md]] | `TermStudioController.java`<br>`RetrofitCalibrationEngine.java`<br>`term/canvas.html` | `RetrofitCalibrationEngineTest.java` |
| `BRD-CSP-SRCH-004` | 生成期 FSM 語法約束 (Outlines) | [[facts/willard_louf_2023_efficient_guided_generation_outlines.md]] | `OutlinesSqlGrammarEngine.java`<br>`SseServiceImpl.java` | `OutlinesSqlGrammarTest.java` |

---

## 🛡️ 5. 架構非功能性需求門禁 (Architectural Hard Gates)

1. **去 Spring 污染原則 (Zero-Spring Pollution in BL)**：所有用戶、角色、權限、全文檢索、文字化 ERD 產生器、SQL 安全檢查之底層業務邏輯 `SysUserBL`, `SysRoleBL`, `SysPermissionBL`, `DocSearchBL`, `TextualErdBL`, `SqlGuardBL` 必須繼承純 Java `BaseBL<REQ, RESP>`，禁止於 BL 內部導入 Spring `@Autowired`。
2. **開源潔淨室 (Clean Room) 命名鐵律**：全量資料庫更新必須透過 `TxSubmitter` 與 `UnitOfWork` 提交，嚴禁使用任何私有原廠代碼或名稱。
3. **老碼農測試覆蓋門禁 (Gauntlet Coverage Gate)**：所有安全、權限、檢索、文字化 ERD 與 AST 審計模組程式碼，Line Coverage 必須 $\ge 80\%$，Branch Coverage 必須 $\ge 75\%$，且 Alibaba P3C 靜態掃描零重大/強制違規。
4. **地端內網隔離推論門禁 (Air-Gapped On-Premises Inference Gate)**：凡攜帶企業資料庫 Schema、欄位名稱或文字化 ERD 之 Text-to-SQL 提示詞，**強制 100% 透過地端/內網 LLM 引擎 (Ollama / vLLM / Local Provider) 進行推論**，嚴禁跨出企業防火牆或連線外部公網 AI，杜絕營業秘密洩漏。
5. **地端算力之電信 OSS 資源化解耦門禁 (Compute as Telecom OSS Resource Gate)**：嚴禁業務層代碼直接硬寫死底層物理推論 IP 或卡號；必須將地端算力視為電信 OSS 之實體/邏輯通訊資源，強制經由 `IComputeResourceProxy` 與 `CapabilityRouter` 進行動態拓撲調度與顯存配額治理。
6. **電信級流量整形與顯存防爆門禁 (Traffic Shaping & VRAM Anti-Blowout Gate)**：推論排程器必須對各頻寬等級實施漏桶流量整形，併發請求峰值下 GPU 顯存使用率嚴禁超過 90%，保障邊緣節點（含 DGX Spark）零 OOM 當機。
7. **Human-in-the-Loop 確定性交棒門禁 (Human-in-the-Loop & Deterministic Handover Gate)**：任何欲介接至 RPA 或外部 ERP 系統之 SQL 與報表，嚴禁由 LLM 黑箱直接發送執行；強制 100% 經由穿梭框精靈畫面供人類複核確認，且由後端純 Java 編譯器產出參數化 SQL，確保零隨機性。
8. **Test 剛性驗證始解鎖交付門禁 (Test Verification Before Delivery Gate)**：任何經由 Database 模組產出之 SQL，嚴禁繞過 Test 模組直接發布產出 Excel 或執行 RPA 拋轉。強制 100% 透過 Test 方塊執行 AST 剛性安全審計、防慢查限制與 Dry Run 預演，審計結果呈現全綠燈合格狀態後，前端始得解除 `[匯出 Excel]` 與 `[拋轉 RPA]` 之鎖定狀態 (Disabled)，徹底杜絕未經審查的查詢直接衝擊產線或外部 ERP。

---
## Sources
- `/Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-portal-web/`
- [[facts/cornelius_csp_portal_security_architecture_and_maintenance_guide.md]]
- [[facts/cornelius_clean_room_unit_of_work_and_tx_submitter_pattern.md]]
- [[AI_Raw/solutions/madaga_csp_portal_lucene_pdf_search.md]]
- [[Areas/Personal/順益集團_數位轉型架構策略備忘錄.md]]
- [[facts/discovery_tree_token_semantics_and_guided_generation.md]]
- [[solutions/llm_sql_guard_outlines_dual_track_architecture.md]]
- [[facts/willard_louf_2023_efficient_guided_generation_outlines.md]]
- [[facts/spec_first_policy.md]]



