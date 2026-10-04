# 📋 PATCH PLAN: Cornelius csp-portal-web 檢索、Text-to-SQL 與安全治理全量工單計畫

> **專案依據**：[[Projects/startup/madaga/BRD.md|Cornelius CSP 商業需求池 (BRD)]] (涵蓋 10 大需求 ID)  
> **規格標準**：[[facts/cornelius_clean_room_unit_of_work_and_tx_submitter_pattern.md|開源潔淨室 UnitOfWork 範本]] / PureMVC 雙軌分層 / TDD 老碼農門禁  
> **實體代碼倉庫**：`/Users/yefangwong/Documents/GitHub.nosync/madaga/csp/csp-portal-web`  
> **最後更新日期**：2026-10-03  
> **狀態**：ACTIVE_MILESTONES (6 大階段)

---

## 🗺️ 階段路線圖 (Milestone Roadmap)

```text
  [ Phase 0: 基礎依賴資安弱點治理 ] ➔ Tomcat/Jackson/Axios 補丁升級與 Dependabot 75% 清障 (BRD-CSP-SEC-006)
                    │
                    ▼
  [ Phase 1: 嵌入式 Lucene 9 檢索原型 ] ➔ 技師手冊 PDF 全文檢索與高亮 (BRD-CSP-SRCH-001)
                    │
                    ▼
  [ Phase 2: RBAC 權限與圓角色塊 UI ] ➔ 3大角色分級控制與 Tag 標籤化 (BRD-CSP-SEC-001~004)
                    │
                    ▼
  [ Phase 3: 多資料庫探針與文字化 ERD ] ➔ 視覺化白名單與極速 Schema 尋路 (BRD-CSP-SRCH-002)
                    │
                    ▼
  [ Phase 4: Text-to-SQL 雙軌縱深安全防線 ] ➔ llm-sql-guard AST 門禁 + Outlines FSM (SEC-005, SRCH-004)
                    │
                    ▼
  [ Phase 5: 私域術語定義與語意引力畫布 ] ➔ 2D 四象限畫布與 Faruqui 凸優化微調 (BRD-CSP-SRCH-003)
```

---

## 🛠️ 全量工單任務拆解 (Detailed Task Breakdown)

### 🛡️ Phase 0: 基礎依賴資安弱點治理與基線修訂 (BRD-CSP-SEC-006)
- [ ] **Task 0.1: 父層 POM Tomcat 與 Jackson 安全版本躍升 (Safe Patching)**
  - 在 `csp/pom.xml` 將 `<tomcat.version>10.1.16</tomcat.version>` 升級為 `10.1.34`（消滅 33 項 Tomcat CVE）。
  - 將 `<jackson.version>2.16.0-SNAPSHOT</jackson.version>` 升級為官方正式版 `2.16.2` 或 `2.17.2`（修復反序列化繞過）。
  - 驗證 `mvn clean test` 通過，零 API 破壞性變更。
- [ ] **Task 0.2: 歷史無用依賴清理與 Tiles 隔離**
  - 自根目錄 `csp/pom.xml` 之 `<dependencyManagement>` 移除未使用之 `commons-fileupload`。
  - 保留 `csp-webframeworx-web` 之 `tiles` 依賴以防編譯錯誤，並透過 PatchVerify 註解標記為不可達 (Unreachable)。
- [ ] **Task 0.3: 前端 Axios 原型污染與 SSRF 修復**
  - 在 `csc/nlp2sql-app/` 執行 `npm update axios` 至 `1.7.4+`。
  - 驗證前端打包 `npm run build` 與 API 請求正常。
- [ ] **Task 0.4: 全量回歸測試與 Dependabot 告警清點**
  - 執行後端全量單元測試，比對 GitHub Dependabot 告警消除率 $\ge 75\%$。

---

### 🟢 Phase 1: 嵌入式 Lucene 9 技術手冊檢索原型 (BRD-CSP-SRCH-001)
- [ ] **Task 1.1: Maven 依賴引入與編譯確認**
  - 在 `csp-portal-web/pom.xml` 加入 `lucene-core` (9.9.2)、`lucene-queryparser` (9.9.2)、`lucene-analysis-common` (9.9.2)、`lucene-highlighter` (9.9.2) 與 `pdfbox` (3.0.1)。
  - 驗證 `mvn compile` 零依賴衝突。
- [ ] **Task 1.2: 嵌入式 Lucene 索引管理器 (`LuceneIndexManager`) 實作 (TDD)**
  - 封裝 `MMapDirectory` 讀寫池、`IndexWriter` 與 `IndexSearcher`。
  - 支援 BooleanQuery（Exact Code Filter + BM25 模糊全文檢索）。
  - 整合 `Highlighter` 輸出 `<mark class="cui-highlight">` 上下文摘要。
  - 單元測試：`LuceneIndexManagerTest` (覆蓋率 $\ge 80\%$)。
- [ ] **Task 1.3: 去 Spring 污染業務單元 (`DocSearchBL`) 實作**
  - 繼承純 Java `BaseBL<SearchRequest, PageResult<SearchResultVO>>`。
  - 實作 5 大標準工序：參數抽取 ➔ 防空防注入校驗 ➔ 呼叫檢索 ➔ 封裝分頁結果 ➔ 閉環。
  - 單元測試：`DocSearchBLTest`（純 JUnit 5，毫秒級反饋）。
- [ ] **Task 1.4: REST Controller 與 Swagger/OpenAPI 端點**
  - 建立 `SearchController.java`，暴露 `GET /api/v1/search` 端點。
  - 整合 `ApiResult<PageResult<SearchResultVO>>`。
- [ ] **Task 1.5: 前端 UI 搜尋框互動與結果 Modal 渲染**
  - 在 `layout/default.html` 頂部搜尋框賦予 ID `#globalSearchInput`，手機版 `#mobileSearchInput`。
  - 撰寫輕量 JavaScript 監聽 Enter 鍵，非同步調用 `/api/v1/search`。
  - 在首頁加入 `#searchResultModal` 呈現檢索結果卡片與高亮標籤。

---

### 🛡️ Phase 2: RBAC 權限與圓角色塊 UI 標籤改造 (BRD-CSP-SEC-001 ~ 004)
- [ ] **Task 2.1: 權限資料庫實體與 SQL 字典升級**
  - 在 `sys_permission` 擴充 `url` 欄位，建立 `sys_user_role(user_id, role_id)` 關聯表。
  - 初始化 6 大權限代碼與 3 大角色關聯數據。
- [ ] **Task 2.2: Spring Security 6 角色與 URL 路由綁定**
  - 在 `SecurityConfig.java` 依「由狹到寬」配置 `/employee/**`, `/department/**`, `/sysUser/**` 之 `hasAnyRole` 權限。
  - 升級 `UserDetailsServiceImpl` 合併多角色 GrantedAuthority。
- [ ] **Task 2.3: 前端 Thymeleaf 側邊欄授權控制**
  - 引入 `thymeleaf-extras-springsecurity6`，在 `default.html` 側邊欄標註 `sec:authorize="hasRole(...)"`。
- [ ] **Task 2.4: 角色與用戶清單「圓角色塊標籤 (Tag Badge)」渲染**
  - 實作 `.tag-badge`, `.tag-admin`, `.tag-manager`, `.tag-employee` CSS 樣式 (`border-radius: 12px;`)。
  - 於 `role/list.html` 與 `user/list.html` 以色彩分級匡列所屬權限與角色。

---

### 📊 Phase 3: 多資料庫連線探針與文字化 ERD 白名單治理 (BRD-CSP-SRCH-002)
- [ ] **Task 3.1: JDBC 動態資料源探針服務 (`DatabaseMetadataProbe`)**
  - 純 Java 調用 `DatabaseMetaData.getTables()` 與 `getImportedKeys()`，獲取全庫 Table 與註解。
- [ ] **Task 3.2: 文字化 ERD 序列化引擎 (`TextualErdGenerator`) (TDD)**
  - 將勾選之白名單資料表自動轉譯為極簡 Markdown / 文字化 ERD 文本。
  - 單元測試：`TextualErdGeneratorTest` (格式與關聯正確性)。
- [ ] **Task 3.3: 文字化 ERD 寫入 Lucene 9 Schema 索引庫**
  - 將文字化 ERD 作為 Documents 存入 Lucene，提供毫秒級 Schema 尋路。
- [ ] **Task 3.4: 前端穿梭框白名單配置頁面 (`datasource/config.html`)**
  - 提供 JDBC 連線表單、測試連線按鈕與 Table 穿梭勾選介面。

---

### 🔒 Phase 4: Text-to-SQL 雙軌縱深安全防線 (BRD-CSP-SEC-005, BRD-CSP-SRCH-004)
- [ ] **Task 4.1: 整合自研 `llm-sql-guard` 執行期 AST 門禁 (TDD)**
  - 在 `SqlController.java` 呼叫 `generalDaoService.queryForList` 前，插入 5 道 AST 門禁審計。
  - 攔截 DDL/DELETE、阻斷 `WHERE 1=1`、強制注入 `LIMIT 50`。
  - 單元測試：`LlmSqlGuardASTTest`（模擬攻擊語句 100% 攔截）。
- [ ] **Task 4.2: 整合 Outlines FSM 生成期語法約束引擎**
  - 於 `SseServiceImpl` 介接有限狀態自動機，以 Logits 硬遮罩 ($-\infty$) 鎖定合法 SQL，杜絕 Markdown 廢話。
- [ ] **Task 4.3: 升級 `Synthesizer` 支援動態文字化 ERD 注入**
  - 廢棄寫死的 2 張表，改為先向 Lucene 查詢文字化 ERD，動態注入命中之 2~3 張授權表結構。
- [ ] **Task 4.4: 地端內網 LLM 推論適配器與外網熔斷器 (`LocalLlmClient`) (TDD) (BRD-CSP-SEC-007)**
  - 封裝 `LocalLlmClient` 支援 Ollama (`11434`) 與 vLLM 本地 OpenAI 相容推論端點。
  - 介接專案既有之 `IComputeResourceProxy` 與 `CapabilityRouter`，將地端推論算力作為電信 OSS Resource 層動態調度（顯存容量預檢與多卡排程）。
  - 實作外網熔斷攔截器（檢測推論端點 IP/域名，凡非內網/私有網段一律拒絕發送包含 Schema 之 Prompt）。
  - 單元測試：`LocalLlmCircuitBreakerTest` (驗證公網端點阻斷率 100%)。

---

### 🎨 Phase 5: 私域術語定義與語意引力畫布 (BRD-CSP-SRCH-003)
- [ ] **Task 5.1: 術語引力凸優化微調引擎 (`RetrofitCalibrationEngine`) (TDD)**
  - 實作 Faruqui 座標上升 20 步凸優化演算法，CPU 計算時間 $\le 50\text{ms}$。
  - 單元測試：`RetrofitCalibrationEngineTest` (驗證同義詞餘弦值 $\ge 0.90$)。
- [ ] **Task 5.2: 前端 2D 語意四象限拖曳畫布 (`term/canvas.html`)**
  - 繪製 X 軸 (Similarity) 與 Y 軸 (Relatedness)，支援氣泡拖曳、彈簧引力連線動效。
  - 拖曳完成即時發送 Ajax 呼叫微調端點，完成閉環。

---

### ⚡ Phase 6: 電信級算力頻寬產品化自訂與邊緣硬體隨插即用擴充 (BRD-CSP-RES-001)
- [ ] **Task 6.1: 電信級 Token 漏桶流量整形器 (`TokenTrafficShaper`) (TDD)**
  - 依據租戶/部門簽約之 `TokenBandwidth` 方案（如 Bronze 20 T/s, Silver 50 T/s, Gold 100+ T/s）進行請求平滑釋放與 QoS 優先級排隊。
  - 單元測試：`TokenTrafficShaperTest`（驗證高併發下突發流量不爆顯存，峰值顯存壓制在 90% 以內）。
- [ ] **Task 6.2: 輝達小電腦 (NVIDIA DGX Spark / Jetson) 邊緣動態發現與拓撲併網**
  - 串接 `NvidiaDgxSparkDriver.java` 與 `CapabilityRouter.java`。
  - 實作 3 秒心跳動態探針，當內網偵測到 Spark 節點接入，自動掛載至算力資源池；大顯存任務 (`>16GB`) 自動分流至 Spark 節點。
  - 單元測試：`NvidiaDgxSparkDriverTest`（模擬邊緣盒熱插拔與大任務路由轉移）。
- [ ] **Task 6.3: 經典電信 Catalog 左樹右表與版本治理介面 (`compute/catalog.html`)**
  - 實作「左樹右表」階層導覽，支援 `Ver. 001` 時間軸與「新增版本」防覆蓋。
  - 提供特徵值 (CharacteristicSpec) EAV 表格維護與上線前獨立「驗證 (Validation)」按鈕。

---

### 🏛️ Phase 7: 致敬經典五大方塊導覽台與 AI 穿梭框報表精靈動線 (BRD-CSP-UI-001, BRD-CSP-RPT-001)
- [ ] **Task 7.1: 左側經典五大方塊導覽列改造 (`layout/default.html` & `dashboard.css`)**
  - 重構側邊欄為寬度 85px 之垂直工作台，渲染 Domain、Database、Configuration、Knowledge、Test 五大方塊按鈕（上 3/4 向量 Icon ＋ 下 1/4 Montserrat 標籤）。
  - 實作 Hover 浮起微光與 Active 左側 4px 亮條指示，響應延遲 $\le 16\text{ms}$。
- [ ] **Task 7.2: Database 模組：六大連線要素與 JDBC 測試連線探針 (`datasource/config.html`)**
  - 提供 Database Type (自動帶入預設 Port)、Host Name、Port Number、Database Name、User Name、Password 輸入表單。
  - 實作「⚡ 測試連線 (Test Connection)」按鈕，純 Java 發送 `SELECT 1` 驗證連通性並回傳毫秒級延遲。
- [ ] **Task 7.3: Domain 模組：AI 引導式四步穿梭框報表精靈 (`report/wizard.html`)**
  - **Step 1 (表穿梭)**：雙向 `-->` / `<--` 選表，外鍵拓撲自動帶出 Join 預覽。
  - **Step 2 (欄位與過濾)**：垂直拖曳排定欄位順序，下方 WHERE 卡片自動連動字典檔下拉選單。
  - **Step 3 (計算排序)**：卡片勾選分組維度與 SUM / AVG / COUNT 算子及多階 Order By。
  - **Step 4 (預覽與 RPA)**：強制 `LIMIT 10` 預覽，提供 `[下載Excel]` 與 `[啟動RPA自動化排程]` 雙通道。
- [ ] **Task 7.4: 後端確定性 JSON AST 編譯器與 POI 串流防爆匯出 (TDD)**
  - 實作 `ReportAstCompiler.java`，將 JSON AST 編譯為標準 SQL，100% 採用 `PreparedStatement` 佔位符。
  - 實作 `SXSSFWorkbook` 串流寫入 (記憶體窗口 $\le 500$ 列)，防杜 JVM OOM。
  - 單元測試：`ReportAstCompilerTest` (驗證 AST 解析與 SQL 防注入) 與 `ReportExportStreamTest`。

---
## Sources
- [[Projects/startup/madaga/BRD.md]]
- [[Projects/startup/madaga/AUDIT_REPORT_CSP_PORTAL_WEB_20261003.md|系統分析與架構自洽性稽核報告 (2026-10-03)]]
- [[Projects/startup/madaga/specs/FS_S1_N01_embedded_lucene_search.md]]
- [[Projects/startup/madaga/specs/FS_S2_N02_textual_erd_whitelist.md]]
- [[Projects/startup/madaga/specs/FS_S1_N03_semantic_canvas_studio.md]]
- [[solutions/llm_sql_guard_outlines_dual_track_architecture.md]]
- [[facts/discovery_tree_token_semantics_and_guided_generation.md]]
