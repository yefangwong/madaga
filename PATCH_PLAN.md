# 📋 Madaga CSP Patch Execution Plan (PATCH_PLAN.md)

> **規則說明**：依據 [[CSP_DEVELOPER_MANUAL.md]] 1.1 鐵律，AI Agent 於修改/建立 Java 生產程式碼前，必須維護並更新此檔案。

---

## 🎯 當前進度 (Current Milestone Progress)
- [x] **`TICKET-CSP-01` [Platform Core API]**: `ApiResult<T>` (方案 A Date) 與 `PageResult<T>` (1-indexed 分頁) 驗證通過 (12/12 測試綠燈)。
- [x] **`TICKET-CSP-02` [Platform Core Context]**: `GlobalContext` 與 `DataPipeline` (Lisbon 演進傳參) 實作並驗證通過 (10/10 測試綠燈)。
- [x] **`TICKET-CSP-03` [Platform Core Logic]**: `BaseBL<RESP>` 5 大生命週期工序與 Lisbon DataPipeline 模式完全對齊。
- [x] **`TICKET-CSP-04` [Platform Core Architecture]**: PureMVC `CspFacade` 入口門面抽象建立。
- [x] **`TICKET-CSP-05` [Platform Compute Proxy]**: `CapabilityRouter`, `LocalHeterogeneousDriver`, `NvidiaDgxSparkDriver`, `ForbiddenVectorInterceptor` 微秒級預檢門禁實作。
- [x] **`TICKET-CSP-06` [Platform Agent Proxy]**: `AiAgentProxy`, `GraphifyMapService`, Scheme B `MctsSearchEngine` 實作。

---

## 📊 檔案異動矩陣 (File Mutation Matrix)

| 檔案路徑 | 變更狀態 | 說明與設計意圖 |
| :--- | :--- | :--- |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/proxy/compute/IComputeResourceProxy.java` | **NEW** | 算力代理中樞介面聲明 |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/proxy/compute/ComputeTaskData.java` | **NEW** | 推論任務與 VRAM 需求傳輸模型 |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/proxy/compute/KernelForgeTask.java` | **NEW** | KernelForge 運算子優化任務模型 |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/proxy/compute/ComputeResult.java` | **NEW** | 算力執行結果與狀態封裝 |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/proxy/compute/LocalHeterogeneousDriver.java` | **NEW** | RTX 3070 + RX 6600 XT 雙卡並聯驅動器 |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/proxy/compute/NvidiaDgxSparkDriver.java` | **NEW** | DGX Spark 128GB Unified Memory 節點驅動器 |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/proxy/compute/ForbiddenVectorInterceptor.java` | **NEW** | Pre-Flight 80% 負樣本微秒級零成本預檢門禁 |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/proxy/compute/CapabilityRouter.java` | **NEW** | 能力導向動態算力分流器 |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/proxy/agent/GraphifyMapService.java` | **NEW** | CPG AST 代碼屬性圖譜建構器 |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/proxy/agent/MctsSearchEngine.java` | **NEW** | Scheme B 混合 Reward 計算器 |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/proxy/agent/AiAgentProxy.java` | **NEW** | 特種 AI Agent 代理中樞 |
| `csp/csp-base/src/test/java/net/yefangwong/csp/common/proxy/CapabilityRouterTest.java` | **NEW** | `CapabilityRouter` & `ForbiddenVectorInterceptor` JUnit 測試 |
| `csp/csp-base/src/test/java/net/yefangwong/csp/common/proxy/AiAgentProxyTest.java` | **NEW** | `AiAgentProxy` & Scheme B MCTS JUnit 測試 |

---

## 🧪 驗證與測試計畫 (Pre-Flight Test Plan)
- 執行 Maven 指令：
  `mvn test -am -pl csp/csp-base`
- 驗證結果：全套單元測試 100% Passed 綠燈通過 🟢
