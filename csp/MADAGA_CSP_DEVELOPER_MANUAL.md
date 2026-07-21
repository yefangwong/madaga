# 📘 Madaga CSP 平台開發與架構規範手冊 (Developer Manual)

> **專案名稱**：Cornelius Service Platform (Madaga CSP)  
> **標準 Base Package**：`net.yefangwong.csp`  
> **相容標準**：JDK 17+ / Maven 多模組 / Spring Boot + MyBatis  
> **目標對象**：平台與業務層開發人員 (Junior / Mid / Senior)

---

## 📌 1. 平台架構與設計總則 (Architecture Principles)

### 🛡️ 1.1 獨立產權與潔淨代碼 (Clean Room) 鐵律
1. **個人產權標準域名**：平台核心套件一律使用 `net.yefangwong.csp.*` 作為基本命名空間，建立 100% 原創之商業 IP 資產。
2. **平台層與業務層完全切割**：
   * **平台核心層 (`net.yefangwong.csp.*`)**：100% 業務中立 (Business-Agnostic)，沉澱通用抽象。
   * **具體應用業務層 (`net.yefangwong.patchverify.*`)**：CVE 漏洞診斷、合規稽核等具體業務完全獨立於應用專案，不污染平台層。
3. **無共享狀態與線程安全**：
   * 建議使用 `new BL()` / `new Delegate()` 等手動實例化方式，隨用隨棄，零全域變數污染。

---

## 📦 2. 基礎通用資產層 (Core Foundation Components)

### 2.1 REST 響應封裝：`ApiResult<T>`
* **Package**: `common.api.ApiResult`
* **設計理念**：極簡 Clean Code 模式，以單一 `Date time` 為時間來源（全面採納 **方案 A**，移除冗餘之 `long timestamp` 屬性，避免狀態不同步）。
* **使用範例**：
  ```java
  // 成功響應
  ApiResult<UserData> res = ApiResult.success(userData);
  
  // 失敗響應
  ApiResult<Void> errRes = ApiResult.failure(400, "無效的參數輸入");
  ```

---

### 2.2 強型別分頁數據封裝：`PageResult<T>`
* **Package**: `net.yefangwong.csp.common.api.PageResult`
* **設計理念**：提供 1-indexed 標準分頁，內建 `totalPages` 自動計算與防呆機制。
* **使用範例**：
  ```java
  // 工廠建構
  PageResult<UserVO> page = PageResult.of(pageNum, pageSize, total, userList);
  
  // 空頁建構
  PageResult<UserVO> emptyPage = PageResult.empty(pageNum, pageSize);
  ```

---

### 2.3 全域錯誤診斷模型：`AppError` & `AppErrors`
* **Package**: `net.yefangwong.csp.common.error.AppError` & `AppErrors`
* **設計理念**：
  * **`AppError`**：非 Exception 的純錯誤資料載體，包含 `code`, `message`, `field` 及 `category` (`VALIDATION`, `BUSINESS`, `SYSTEM`)。
  * **`AppErrors`**：鏈式 Fluent API 診斷容器，實現 `Iterable<AppError>`。
* **使用範例**：
  ```java
  AppErrors errors = AppErrors.create()
      .add("E4001", "權限不足")
      .addValidation("email", "電子郵件格式無效");

  if (errors.hasErrors()) {
      String firstMsg = errors.getFirstMessage();
  }
  ```

---

## 🏗️ 3. 業務邏輯層 (BL) 開發規範：`BaseBL<REQ, RESP>`

### 3.1 為什麼採用 `BaseBL` 樣板模式？
* **對初級工程師 (Junior)**：提供「填空式開發」，只需專注於輸入防呆與業務邏輯，不必擔心連線管理、例外洩漏或交易控制。
* **對資深工程師 (Senior)**：提供「軌道式維護」，強制統一 5 大生命週期，Code Review 極速，且內建 ISO 27001 審計不漏勾。

---

### 3.2 5 大生命週期工序 Flow

所有繼承 `BaseBL<REQ, RESP>` 的業務單元，都會自動遵循以下 5 大工序：

```text
process(request, operatorEmail, actionCode, repoId)
  │
  ├── 1. validateInput(request) ─────────────> [false] ──> 回傳 ApiResult.failure(400, errors.getFirstMessage())
  │                                [true]
  ├── 2. verifyAuthority(request, operatorEmail) ──> [false] ──> 回傳 ApiResult.failure(403, errors.getFirstMessage())
  │                                [true]
  ├── 3. RESP response = executeBusiness(request) ──> 直連 MyBatis Mapper (受 Spring @Transactional 保護)
  │
  ├── 4. writeAuditLog(repoId, operatorEmail, actionCode, "SUCCESS") (ISO 27001 審計軌跡)
  │
  └── 5. 回傳 ApiResult.success(response)
```

---

### 3.3 `BaseBL` 繼承開發 SOP 範例

```java
package net.yefangwong.patchverify.bl;

import net.yefangwong.csp.common.bl.BaseBL;
import common.api.ApiResult;
import org.springframework.stereotype.Component;

public class PatchApproveBL extends BaseBL<PatchApproveRequest, PatchApproveResponse> {

    // 可直接注入或透過建構子傳入 MyBatis Mapper
    private final VulnerabilityMapper vulnerabilityMapper;

    public PatchApproveBL(VulnerabilityMapper vulnerabilityMapper) {
        this.vulnerabilityMapper = vulnerabilityMapper;
    }

    @Override
    protected boolean validateInput(PatchApproveRequest request) {
        if (request == null || request.getCveId() == null) {
            errors.addValidation("cveId", "CVE ID 不能為空");
            return false;
        }
        return true;
    }

    @Override
    protected boolean verifyAuthority(PatchApproveRequest request, String operatorEmail) {
        if (!operatorEmail.endsWith("@company.com")) {
            errors.add("E403", "非企業授權操作者");
            return false;
        }
        return true;
    }

    @Override
    protected PatchApproveResponse executeBusiness(PatchApproveRequest request) throws Exception {
        // 直連 MyBatis Mapper 執行 CRUD (受 Spring @Transactional 保護)
        vulnerabilityMapper.updateStatus(request.getCveId(), "APPROVED");
        return new PatchApproveResponse(request.getCveId(), "APPROVED");
    }
}
```

---

### 3.4 觀念解惑：`REQ` 與 `RESP` 泛型的 3 種寫法與實務範例

> 💡 **觀念提醒**：`REQ` (Request) 與 `RESP` (Response) 不是某個具體的 Class 檔，而是 **Java 泛型佔位符 (Generic Placeholders)**。開發人員在編寫具體的子類別 (BL 的兒子) 時，依據業務需求決定傳入的型別：

#### 寫法 1：強型別專用 DTO (最推薦 - 適合中大型業務)
```java
// REQ  = UserCreateRequest  (輸入卡片)
// RESP = UserCreateResponse (輸出結果卡片)
public class UserCreateBL extends BaseBL<UserCreateRequest, UserCreateResponse> {

    @Override
    protected boolean validateInput(UserCreateRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            errors.addValidation("username", "帳號不能為空");
            return false;
        }
        return true;
    }

    @Override
    protected boolean verifyAuthority(UserCreateRequest request, String operatorEmail) {
        return true;
    }

    @Override
    protected UserCreateResponse executeBusiness(UserCreateRequest request) throws Exception {
        userMapper.insertUser(request);
        return new UserCreateResponse(request.getUsername(), "CREATED");
    }
}
```

#### 寫法 2：原生單型別 (適合簡單單筆刪除 / 查詢)
```java
// REQ  = String  (只需傳入一個 userId 字串)
// RESP = Boolean (回傳刪除成功與否)
public class UserDeleteBL extends BaseBL<String, Boolean> {

    @Override
    protected boolean validateInput(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            errors.addValidation("userId", "使用者 ID 不能為空");
            return false;
        }
        return true;
    }

    @Override
    protected boolean verifyAuthority(String userId, String operatorEmail) {
        return operatorEmail.endsWith("@admin.com");
    }

    @Override
    protected Boolean executeBusiness(String userId) throws Exception {
        return userMapper.deleteById(userId) > 0;
    }
}
```

#### 寫法 3：萬能管道 `DataPipeline` (適合多物件組合 - 復刻 Lisbon `VData` 體感)
```java
// REQ  = DataPipeline (萬能容器，內裝多種 Schema / VO 物件)
// RESP = Object
public class DynamicProcessBL extends BaseBL<DataPipeline, Object> {

    @Override
    protected boolean validateInput(DataPipeline pipeline) {
        UserVO user = pipeline.get(UserVO.class);
        if (user == null) {
            errors.addValidation("user", "缺少 UserVO 管道資料");
            return false;
        }
        return true;
    }

    @Override
    protected boolean verifyAuthority(DataPipeline pipeline, String operatorEmail) {
        return true;
    }

    @Override
    protected Object executeBusiness(DataPipeline pipeline) throws Exception {
        UserVO user = pipeline.get(UserVO.class);
        return userMapper.processUser(user);
    }
}
```

---

## 🗄️ 4. 持久層與事務管理規範 (Persistence & Transactions)

1. **主流框架：MyBatis + Spring `@Transactional`**：
   * 業務操作建議於 Delegate / Controller 層加註 `@Transactional`。
   * 異動操作在 `executeBusiness` 內部完成，無須手動 Commit/Rollback。
2. **原生 JDBC 引擎：`ISqlExecutor` & `DataRecordSet`**：
   * 提供予超高併發或獨立低程式碼工具 (`CspSchemaCompiler`) 選配使用。

---

## 🧪 5. 開發與編譯環境規範 (Environment Guidelines)

* **JDK 版本**: 必須使用 **JDK 17+** 基準。
* **編譯與測試指令**:
  ```bash
  # 設為 JDK 17 環境
  export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
  
  # 全專案編譯 install
  mvn clean install -DskipTests
  
  # 執行單元測試
  mvn test
  ```

---
*本手冊由 Madaga CSP 架構小組維護，如有新增模組規範請同步更新。*
