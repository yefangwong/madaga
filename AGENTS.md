# AGENTS.md - Madaga Cornelius Service Platform Development Protocol

You are an expert software engineer and autonomous AI agent developing on the **Madaga / Cornelius Service Platform (CSP)** codebase.
Your mission is to maintain high-signal, enterprise-grade, clean-room software adhering to the **Cornelius Clean-Room Architecture** and **Old-Coder Gauntlet Protocol**.

---

## 🏛️ 1. 核心架構與潔淨室鐵律 (Core Architecture & Clean-Room Rules)

### 1.1 去 Spring 污染與填空式開發 (Zero-Spring Pollution in Business Layer)
- **外層門面（懂 Spring 的底座）**：`View / Controller` ➔ `PureMVC Facade (Stateless Singleton)` 統一接管 HTTP、`@Transactional` 事務與 Spring 容器。
- **核心業務（純淨純 Java POJO）**：所有業務邏輯單元 `BaseBL<REQ, RESP>` 必須為 **100% 純 Java POJO**。
  - **嚴禁**在 BL 內部使用 `@Autowired`、`@Component` 或依賴任何 Spring 容器套件。
  - BL 遵循標準 5 大工序：`extract` ➔ `check` ➔ `executeBusiness` ➔ `assemble` ➔ `wrapResult`。

### 1.2 開源智財安全與潔淨命名矩陣 (Mandatory Clean-Room Terms)
本專案為開源專案（Apache 2.0 授權），必須嚴格落實潔淨室設計。**嚴禁使用客戶端或私有原廠代碼與匈牙利命名**，強制全面採納以下開源標準命名：
- ❌ `PubSubmit` ➡️ **`TxSubmitter`**（集中事務提交引擎）
- ❌ `MMap` ➡️ **`UnitOfWork`**（記憶體變更收集袋）
- ❌ `VData` ➡️ **`DataPipeline`**（資料流管道容器）
- ❌ `ExeSQL` ➡️ **`ISqlExecutor`** / **`NativeSqlExecutor`**（SQL 參數綁定執行器）
- ❌ `SSRS` ➡️ **`DataRecordSet`**（SQL 二維結果集）
- ❌ `*Schema` ➡️ **`BaseEntity`**（持久化實體基類）
- ❌ `*SchemaSet` ➡️ **`PageResult<T>`** / **`EntitySet<T>`**（分頁與實體集合）
- ❌ `cOperate` ➡️ **`actionCode`**（標準操作代碼）
- ❌ `CError / mErrors` ➡️ **`AppError / AppErrors`**（診斷錯誤收集器）

---

## 🛡️ 2. 阿里 P3C 與契約硬門禁 (Alibaba P3C & Quality Gating)

1. **型別安全**：
   - 嚴禁於 POJO / Entity 使用基本型態 `boolean`（強制使用 `Boolean` 防止 RPC/序列化預設 false 假象）。
   - RPC/Controller 介面嚴禁傳遞無型別約束之 `Map` 或萬用 `Object`。
2. **例外與日誌**：
   - 嚴禁空 catch 區塊與 `e.printStackTrace()`。
   - 日誌輸出強制使用參數佔位符 `{}`，禁止字串拼接以防併發記憶體暴增。
3. **執行緒管理**：
   - 嚴禁手動 `new Thread()` 或無界隊列執行緒池（如 `Executors.newFixedThreadPool()`），強制採用命名託管之 `ThreadPoolExecutor`。
4. **P3C 靜態掃描**：
   - 執行 `mvn p3c-pmd:check`，零強制級 (Blocker) 與重大級 (Critical) 違規始可通過門禁。

---

## 🧪 3. 測試覆蓋率與老碼農門禁 (Gauntlet Testing Protocol)

- **純單元測試反饋**：BL 單元測試**嚴禁標註 `@SpringBootTest`**，直接使用 JUnit 5 實例化 BL，毫秒級反饋。
- **覆蓋率底線**：新增或修改之業務邏輯代碼，強制維持 **Line Coverage $\ge 80\%$**，**Branch Coverage $\ge 75\%$**。
- **對稱測試搬遷原則 (Symmetric Test Refactor Rule)**：修改或移動 `src/main/.../X.java` 時，同一筆 Commit 內強制同步檢查並處置 `src/test/.../XTest.java`，嚴禁留下無主孤兒測試。

---

## 📋 4. 單一事實標準與工單鐵律 (PATCH_PLAN.md Mandatory Protocol)

- **工單先行**：AI Agent 在修改或建立任何 Java 程式碼、SQL 腳本之前，**強制必須先檢視並更新專案根目錄之 `PATCH_PLAN.md`**。
- **需求回寫**：若在開發或測試階段發現新業務邏輯或安全約束變更，必須先寫回 `docs/BRD.md`，同步 RTM 雙向追蹤矩陣後，始得修改代碼。

---

## 📖 5. 核心文檔指引
- **商業需求中心**：`docs/BRD.md` (10 大需求與 RTM 矩陣)
- **開發者手冊**：`CSP_DEVELOPER_MANUAL.md` (架構與模式總手冊)
- **綜合測試計畫**：`docs/TEST_PLAN.md` (分階段 TC 與覆蓋率門禁)
- **模組化規格書**：`docs/specs/FS.md`
