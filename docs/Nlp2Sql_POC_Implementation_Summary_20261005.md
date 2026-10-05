# Nlp2Sql Compiler Core Engine POC (Phase 1) - 工作總結

**日期**: 2026-10-05
**模組**: `csp-common` (基於 Clean Architecture 原則，以及目前 `csp-api` 尚有舊版 Spring 依賴問題，故實作於此模組以確保平台資產中立性與可測試性)

## 🎯 實作目標
依據施工藍圖 (`CSP_CONSTRUCTION_BLUEPRINT.md`) 第 5 章，以及知識庫的詳細規格 (`FS_S1_N01_nlp2sql_compiler_poc.md`)，透過 TDD (Test-Driven Development) 方式實作「零幻覺、絕對決定性」的自然語言轉 SQL 規則引擎 (`Nlp2SqlCompilerService`)。

## 🛠️ 完成項目

### 1. 核心編譯器實作
完成純 POJO 的無副作用計算服務實作，包含以下編譯流轉邏輯：
* **Lexical Analysis (詞法掃描)**：將自然語言字串切分為 Token 序列，並對未註冊詞彙 (Unknown Token) 進行攔截。
* **Syntax Analysis (語法解析)**：將 Token 組裝成 AST (Abstract Syntax Tree)，正確捕捉 ACTION, ENTITY 及 CONDITION。
* **Semantic Analysis (語意檢驗)**：進行型別檢查 (例如數量詞「一公斤」與 Entity 間的衝突判定)。
* **Code Generation (代碼生成)**：產出精準的 `EXISTS` SQL 查詢語法。

### 2. 資料結構建立
建立完整的資料傳輸與抽象層物件，位置於 `net.yefangwong.csp.domain.nl2sql.dto`：
* `CompilationResultDTO.java`: API 最終回傳的 Payload 格式。
* `AstNode.java`: 抽象語法樹節點，承載 Action 及 Target Table 資訊。
* `Condition.java`: AST 中的條件節點，負責 WHERE 條件描述。
* `Token.java`: 詞法掃描產出的元件單元。

### 3. TDD 驗證與綠燈
完成 `Nlp2SqlCompilerServiceTest` (JUnit 3 規格以相容 `csp-common` 環境)，且成功通過下列三個 AC：
* **AC1: Deterministic Compilation (決定性編譯成功)** - 輸入「查詢財務部有沒有一位吳華瑄的小姐」，成功產出 `SELECT EXISTS(...)` 查詢與完整的 tokens 陣列。
* **AC2: Type Mismatch Rejection (型別衝突防呆)** - 成功攔截並報錯「查詢財務部有沒有一公斤的吳華瑄」(TYPE_ERROR)。
* **AC3: Unknown Token Rejection (詞彙無效防呆)** - 成功攔截並報錯未定義的「研發部」(UNKNOWN_TOKEN)。

---
**附註**: `csp-api` 模組的 POM 已嘗試補上 `jstl` 與 `mysql-connector-java` 等版本，但後續因 Spring Security 等類別在 Spring 6/Spring Boot 3 中的變更 (如 `WebSecurityConfigurerAdapter` 被棄用) 導致全面編譯失敗，故改將此 POC 開發放於基礎核心的 `csp-common` 以保持核心邏輯純淨與不依賴外部框架的特性。
