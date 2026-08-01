# 📋 Madaga CSP Patch Execution Plan (PATCH_PLAN.md)

> **規則說明**：依據 [[CSP_DEVELOPER_MANUAL.md]] 1.1 鐵律，AI Agent 於修改/建立 Java 生產程式碼前，必須維護並更新此檔案。

---

## 🎯 當前進度 (Current Milestone Progress)
- [x] **`TICKET-CSP-01` [Platform Core API]**: `ApiResult<T>` (方案 A Date) 與 `PageResult<T>` (1-indexed 分頁) 驗證通過 (12/12 測試綠燈)。
- [x] **`TICKET-CSP-02` [Platform Core Context]**: `GlobalContext` 與 `DataPipeline` (Lisbon 演進傳參) 實作並驗證通過 (10/10 測試綠燈)。
- [ ] **`TICKET-CSP-03` [Platform Core Logic]**: `BaseBL<RESP>` 5 大生命週期工序與 Lisbon DataPipeline 簽名對齊。
- [ ] **`TICKET-CSP-04` [Platform Core Architecture]**: PureMVC `CspFacade` 入口門面抽象。
- [ ] **`TICKET-CSP-05` [Platform Compute Proxy]**: `CapabilityRouter`, `LocalHeterogeneousDriver`, `NvidiaDgxSparkDriver`, `ForbiddenVectorInterceptor` 微秒級門禁。
- [ ] **`TICKET-CSP-06` [Platform Agent Proxy]**: `AiAgentProxy`, `GraphifyMapService`, Scheme B `MctsSearchEngine`。

---

## 📊 檔案異動矩陣 (File Mutation Matrix)

| 檔案路徑 | 變更狀態 | 說明與設計意圖 |
| :--- | :--- | :--- |
| `csp/csp-common/src/main/java/common/api/ApiResult.java` | **VERIFIED** | 方案 A 單一 Date 來源之 ApiResult 通用響應模型 |
| `csp/csp-common/src/main/java/net/yefangwong/csp/common/api/PageResult.java` | **VERIFIED** | 1-indexed 強型別分頁模型，內建 totalPages 計算 |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/context/GlobalContext.java` | **NEW (COMPLETED)** | 實作全域上下文模型 (operatorEmail, comCode, clientIp, traceId) |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/context/DataPipeline.java` | **NEW (COMPLETED)** | 實作 Lisbon 演進萬能管道，打平 Context 與 Request Payload |
| `csp/csp-base/src/test/java/net/yefangwong/csp/common/context/DataPipelineTest.java` | **NEW (COMPLETED)** | `GlobalContext` & `DataPipeline` JUnit 測試 (2/2 Passed) |

---

## 🧪 驗證與測試計畫 (Pre-Flight Test Plan)
- 執行 Maven 指令：
  `mvn test -am -pl csp/csp-base`
- 驗證結果：**100% Passed (10/10 測試綠燈全數通過)** 🟢
