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
- [ ] **Task 0.5: Gitleaks 憑證掃描門禁與敏感資訊洩漏治理 (待研議)**
  - **問題背景**：Azure Pipelines CI 觸發 `gitleaks detect` 掃描時檢出 155 項 Leaks（掃描 27 筆 Commit 歷史）。主因為包含已刪除之舊歷史（`csc/nlp2sql-app/src/config/apiConfig.js` 舊 Key 佔 20 次）以及專案中目前 5 處現存硬編碼（`application.properties`、`check.sh`、`SecurityInterceptor.java`、`RestAuthenticationEntryPoint.java`、`V1OcheckinTest.java` 各佔 27 次）。
  - **待研議方向**：
    1. **CI 門禁範圍收斂**：調整 `azure-pipelines.yml` 之 Gitleaks 參數，限制為 `--no-git`（僅掃當前工作目錄）或指定分支比對（`--log-opts`），防止歷史 commit 無限阻斷 CI。
    2. **現存代碼金鑰外置化**：清理 `application.properties`、`check.sh` 等 5 處現存檔案中的硬編碼 Key，改由環境變數注入。
    3. **Git 歷史清洗評估**：評估是否需使用 `git-filter-repo` 抹除 Git 歷史中的洩漏節點。
    4. **白名單與基線建立**：配置 `.gitleaks.toml` 或 `.gitleaksignore` 定義已失效 Key 或測試 Mock 資料之 Baseline。

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

### 🏛️ Phase 7: 經典五大方塊工作台與 AI 穿梭框 SQL 組裝/Test 門禁交付動線 (BRD-CSP-UI-001, BRD-CSP-RPT-001)
- [ ] **Task 7.1: 左側經典五大方塊工作台改造 (`layout/default.html` & `dashboard.css`)**
  - 重構側邊欄為寬度 85px 之垂直工作台，由上至下渲染 Domain、Knowledge (核心主舞台)、Database (可選外掛)、Configuration (開箱即用預設)、Test (剛性驗證與交付門禁) 五大方塊按鈕（上 3/4 向量 Icon ＋ 下 1/4 Montserrat 標籤）。
  - 實作 Hover 浮起微光與 Active 左側 4px 亮條指示，響應延遲 $\le 16\text{ms}$。
- [ ] **Task 7.2: Knowledge 模組：預設 Embedding 空間直視與即時 Token / Term 映射畫布 (`knowledge/canvas.html`)**
  - 系統預設開箱直視當前領域之 Embedding Space（結合 `Configuration` 預設地端算力與預設 `bge-small-zh-v1.5` 模型）。
  - 支援使用者在面板即時新增自訂 Token / Term（如「生魚片」、「鮭魚」、「二硫化鉬」），後端即時推論向量並在 2D 畫布上浮現泡泡坐標，支援引力拉動與凸優化微調。
- [ ] **Task 7.3: Database 模組：六大連線要素與 `[ 🛠️ Build SQL ]` 穿梭框精靈 (`datasource/config.html` & `sql/builder.html`)**
  - Database 設為可選外掛：提供 JDBC 六大要素連線探針（Type, Host, Port, DB Name, User, Password）與「⚡ 測試連線」。
  - 介面提供 `[ 🛠️ Build SQL ]` 按鈕，點擊後觸發三步穿梭框精靈（Step 1 表穿梭 ➔ Step 2 欄位與過濾 ➔ Step 3 計算排序），任務**嚴格止於安全組裝純淨 SQL AST**，並自動導引至 **【Test】方塊**。
- [ ] **Task 7.4: Test 模組：AST 剛性門禁審查與 Excel/RPA 解鎖交付機制 (`test/guard.html` & TDD)**
  - 承接 Database 產出之 SQL，執行 AST 5 道防線審查、慢查安全限制與 Dry Run 執行（注入 `LIMIT 10` 呈現高擬真網格）。
  - **綠燈解鎖機制**：審查 100% 通過獲得綠燈後，始正式解鎖 `[ 📥 匯出 Excel ]` (SXSSFWorkbook 串流防爆) 與 `[ 🤖 拋轉 RPA 自動化排程 ]`；未通過強制鎖定。
  - 單元測試：`ReportAstCompilerTest` (驗證 AST 解析與 SQL 防注入) 與 `ReportExportStreamTest`。

---

### 🖥️ Phase 8: 中小企業地端算力訂閱託管與多階異構（NVIDIA+AMD+Apple Silicon）硬體池化治理 (BRD-CSP-RES-002)
- [ ] **Task 8.1: 多階異構（NVIDIA+AMD+Apple Metal）顯存池化適配器 (`LocalHeterogeneousDriver` 升級) (TDD)**
  - 封裝 Mac mini M1 (Metal) 與 PC 端 RTX 3070 (CUDA) + RX 6600 XT (Vulkan) 跨晶片顯存自動註冊與狀態遙測。
  - 單元測試：`HeterogeneousComputePoolTest`（驗證異構顯存動態併網與單卡顯存耗盡時之平滑溢出，覆蓋率 $\ge 80\%$）。
- [ ] **Task 8.2: 跨機低頻寬管線平行 (PP) 8KB 隱藏層串流傳輸器 (`HiddenStateStreamingProxy`) (TDD)**
  - 在經由 100M/1G 網路（如 Cisco SD 208）串聯時，強制阻斷張量平行 (TP)，採用管線平行切分，跨網僅傳遞 8KB 隱藏層向量。
  - 單元測試：`PipelineParallelismStreamingTest`（驗證 8KB 向量在 100M 網路下通訊延遲 $\le 3\text{ms}$，無網路堵塞）。
- [ ] **Task 8.3: 跨網延遲補償與 SLA 流量平滑化排程 (`LatencyCompensatingRouter`) (TDD)**
  - 整合 `CapabilityRouter`，針對跨機通訊產生的 15%~25% 延遲折損實施延遲遮罩與並發槽位退避。
  - 單元測試：`LatencyCompensatingRouterTest`（驗證高並發下各租戶 TPOT 波動標準差 $\le 10\%$）。
- [ ] **Task 8.4: 五餅二魚隨插即用私有算力小盒子開箱即用套件 (`PlugAndPlayApplianceBootstrap`)**
  - 封裝針對教會同工與個人創作者之 Mini PC / 翻新 Mac mini 隨插即用套件，預載本地端點 Ollama (`127.0.0.1:11434`) 與繁中直觀 WebUI。
  - 支援本地講章、教案與私密筆記 PDF 向量檢索問答，通電插網線即可於區網開箱使用，個資與代禱信 100% 留存地端，零 Token 次數限制。

---

### 🛡️ Phase 9: 電信 OSS 資源層 (RM&O) 1+1 複合安全算力配額與帶外治理 (BRD-CSP-RES-003)
- [ ] **Task 9.1: 資源清冊實體之安全治理屬性擴充與 DDL (`tbl_res_inventory` & `ResourceInventoryEntity`) (TDD)**
  - 在 TMF SID `resource/` 領域擴充 `isolation_level` (實體隔離級別)、`watchdog_attached` (是否掛載帶外 Sentry) 與 `egress_fencing_status` (網卡外網硬阻絕狀態)。
  - 單元測試：`ResourceInventoryEntityTest` (驗證實體安全屬性讀寫、阿里 P3C 規約合規，覆蓋率 $\ge 80\%$)。
- [ ] **Task 9.2: 電信 1+1 複合安全算力配額排程器 (`CoupledSecureResourceScheduler`) (TDD)**
  - 借鏡電信 Working + Protection 雙電路保護，為高風險/金流 Agent 任務成對開通 Worker (生成) + Sentry (審查) 雙元實體節點（顯存 Air-Gap）。
  - 單元測試：`CoupledResourceSchedulerTest` (驗證雙元節點原子配對、顯存資源鎖定與槽位釋放，覆蓋率 $\ge 80\%$)。
- [ ] **Task 9.3: 帶外認知故障隔離器與 OSS/J Trouble Ticketing 整合 (`OutOfBandFaultIsolator`) (TDD)**
  - Sentry 節點判定語意越獄或異常時，$\le 10\text{ms}$ 內執行資源配額熔斷 (Kill-Switch)，並自動向 OSS/J Trouble Ticketing 派單觸發修復 Agent。
  - 單元測試：`OutOfBandFaultIsolatorTest` (驗證帶外硬熔斷速度、拓撲動態摘除與 Trouble Ticket 生成，覆蓋率 $\ge 80\%$)。
- [ ] **Task 9.4: 生產與治理雙軌用量中介計費器 (`DualTrackUsageMediator`) (TDD)**
  - 實作雙軌計量模型，精確分離 Production Token/GPU 時間與 Sentry 審查/網路隔離開銷，產出結構化計費清單。
  - 單元測試：`DualTrackUsageMediatorTest` (驗證多租戶雙軌計量彙整與成本分攤精確度，覆蓋率 $\ge 80\%$)。

---
## Sources
- [[Projects/startup/madaga/BRD.md]]
- [[Projects/startup/madaga/AUDIT_REPORT_CSP_PORTAL_WEB_20261003.md|系統分析與架構自洽性稽核報告 (2026-10-03)]]
- [[Projects/startup/madaga/specs/FS_S1_N01_embedded_lucene_search.md]]
- [[Projects/startup/madaga/specs/FS_S2_N02_textual_erd_whitelist.md]]
- [[Projects/startup/madaga/specs/FS_S1_N03_semantic_canvas_studio.md]]
- [[solutions/llm_sql_guard_outlines_dual_track_architecture.md]]
- [[facts/discovery_tree_token_semantics_and_guided_generation.md]]
- [[facts/telecom_oss_resource_layer_and_nvidia_safety_chip_isomorphism.md]]
- [[Projects/startup/madaga/WORKLOG_20261004.md]]

