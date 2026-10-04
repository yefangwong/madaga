# Cornelius Scope Architecture & Development Pattern Rule (哥尼流開源潔淨室架構與範本開發規範)

## 🎯 核心原則與適用範疇
凡涉及 **Cornelius Service Platform (`madaga/csp`、`csp-portal-web`、`csp-base`、`net.yefangwong.csp`)** 之任何系統規劃 (Planning)、功能規格 (Specs)、架構設計 (SD) 或代碼範例 (Examples) 生成，**所有 Agent 強制必須 100% 套用本規範**。

---

### 1. 初心使命與去 Spring 污染原則 (Zero-Spring Pollution in Business Layer)
* **核心使命**：讓不懂 Spring 框架的初級工程師、非資工背景人員或身心障礙者，能靠簡單「Copy & Paste (複製貼上) 範本」或用語音/NLP 驅動自動生成，安全自如地維護與開發系統。
* **分層隔離**：
  * **外層門面（懂 Spring 的底座）**：`View / Controller` ➔ `PureMVC Facade (Stateless Singleton)` 統一接管 HTTP、`@Transactional` 事務與 Spring 容器。
  * **核心業務（不懂 Spring 的純淨世界）**：所有業務邏輯單元 `BaseBL<REQ, RESP>` 必須為 **100% 純 Java POJO**，**嚴禁**在 BL 內部使用 `@Autowired`、`@Component` 或依賴任何 Spring 容器套件。

---

### 2. 開源智財安全與潔淨室命名鐵律 (Clean-Room Naming Matrix)
專案為開源專案（Apache 2.0 授權），必須嚴格落實潔淨室設計（Clean-Room Reverse Engineering）。**嚴禁使用任何客戶端或原廠之私有名稱與匈牙利前綴**，全量強制採納以下標準開源命名：

| 嚴禁使用之私有名稱 | 開源標準潔淨命名 (Mandatory Open Source Terms) | 職責與定位 |
| :--- | :--- | :--- |
| ❌ `PubSubmit` | ➡️ **`TxSubmitter`** 或 **`BatchSubmitter`** | 原子性集中提交引擎 (Unit of Work 執行器) |
| ❌ `MMap` | ➡️ **`UnitOfWork`** | 記憶體變更收集袋 (Martin Fowler PoEAA 標準名) |
| ❌ `VData` | ➡️ **`DataPipeline`** | 萬能參數資料流管道容器 (`common.context`) |
| ❌ `ExeSQL` | ➡️ **`ISqlExecutor`** / **`NativeSqlExecutor`** | JDBC 參數綁定執行器 (`common.db`) |
| ❌ `SSRS` | ➡️ **`DataRecordSet`** | 輕量 SQL 二維結果集封裝 (`common.db`) |
| ❌ `*Schema` | ➡️ **`BaseEntity`** | 物件導向持久化實體基類 (`common.entity`) |
| ❌ `*SchemaSet` | ➡️ **`EntitySet<T>`** / **`PageResult<T>`** | 強型別實體集合載體 (`common.api`) |
| ❌ `cOperate` | ➡️ **`actionCode`** | 標準業務操作代碼字串 |
| ❌ `CError` / `mErrors` | ➡️ **`AppError` / `AppErrors`** | Fluent 鏈式全域診斷容器 (`common.error`) |

---

### 3. BL 雙軌資料存取模式 (Dual-Track Persistence Pattern)
在 `BaseBL.executeBusiness()` 內部，資料操作優先採用：
1. **純查詢 (Query / Read)**：直接調用 **`ISqlExecutor`** 傳入標準 SQL，自動映射結果，**不強制要求定義 MyBatis Mapper 介面與 XML**。
2. **異動變更 (Create / Update / Delete)**：採用 **`UnitOfWork` 收集變更，最後由 `TxSubmitter` 一次性原子提交**：
   ```java
   UnitOfWork uow = new UnitOfWork();
   uow.insert("INSERT INTO sys_permission (code, name, state) VALUES (?, ?, 1)", perm.getCode(), perm.getName());
   if (!txSubmitter.submit(uow)) {
       errors.add("E500", "寫入失敗：" + txSubmitter.getErrorMessage());
       return false;
   }
   ```
   杜絕連線洩漏，保證零半途 Commit 髒資料。

---

### 4. TDD 測試與硬門禁 (TDD & Evidence-First Protocol)
* **極速純單元測試**：BL 測試**嚴禁標註 `@SpringBootTest`**。直接使用 JUnit 5 實例化 BL，保證毫秒級（$< 50\text{ ms}$）回饋。
* **測試覆蓋率硬門禁**：Line Coverage $\ge 80\%$，Branch Coverage $\ge 75\%$。
* **Alibaba P3C 規約**：POJO/Entity 屬性嚴禁基本型態 `boolean`（強制使用 `Boolean`）；日誌輸出強制使用參數佔位符 `{}`。

---
*本規範為 Cornelius Service Platform 專屬強約束規範，詳見 [[facts/cornelius_clean_room_unit_of_work_and_tx_submitter_pattern.md]]。*
