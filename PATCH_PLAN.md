# 📋 Madaga CSP Patch Execution Plan (PATCH_PLAN.md)

> **規則說明**：依據 [[CSP_DEVELOPER_MANUAL.md]] 1.1 鐵律，AI Agent 於修改/建立 Java 生產程式碼前，必須維護並更新此檔案。

---

## 🎯 當前進度 (Current Milestone Progress)
- [x] **`TICKET-CSP-01` [Platform Core API]**: `ApiResult<T>` (方案 A Date) 與 `PageResult<T>` (1-indexed 分頁) 驗證通過 (12/12 測試綠燈)。
- [x] **`TICKET-CSP-02` [Platform Core Context]**: `GlobalContext` 與 `DataPipeline` (Lisbon 演進傳參) 實作並驗證通過 (10/10 測試綠燈)。
- [x] **`TICKET-CSP-03` [Platform Core Logic]**: `BaseBL<RESP>` 5 大生命週期工序重載支援 Lisbon `DataPipeline` 模式。
- [x] **`TICKET-CSP-04` [Platform Core Architecture]**: PureMVC `CspFacade` 入口門面抽象建立。
- [ ] **`TICKET-CSP-05` [Platform Compute Proxy]**: `CapabilityRouter`, `LocalHeterogeneousDriver`, `NvidiaDgxSparkDriver`, `ForbiddenVectorInterceptor` 微秒級門禁。
- [ ] **`TICKET-CSP-06` [Platform Agent Proxy]**: `AiAgentProxy`, `GraphifyMapService`, Scheme B `MctsSearchEngine`。

---

## 📊 檔案異動矩陣 (File Mutation Matrix)

| 檔案路徑 | 變更狀態 | 說明與設計意圖 |
| :--- | :--- | :--- |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/bl/BaseBL.java` | **UPDATE** | 重載支援 Lisbon DataPipeline 模式之 5 大工序 `process(DataPipeline, String)` |
| `csp/csp-base/src/main/java/net/yefangwong/csp/common/facade/CspFacade.java` | **NEW** | 建立無狀態 PureMVC 門面抽象類別 `CspFacade` |
| `csp/csp-base/src/test/java/net/yefangwong/csp/common/bl/BaseBLTest.java` | **UPDATE** | 新增 DataPipeline 模式之 `BaseBL` 5 大工序 JUnit 單元測試 |
| `csp/csp-base/src/test/java/net/yefangwong/csp/common/facade/CspFacadeTest.java` | **NEW** | `CspFacade` 派發與零業務逆向依賴 JUnit 單元測試 |

---

## 🧪 驗證與測試計畫 (Pre-Flight Test Plan)
- 執行 Maven 指令：
  `mvn test -am -pl csp/csp-base`
- 驗證結果：全套單元測試 100% Passed 綠燈通過 🟢
