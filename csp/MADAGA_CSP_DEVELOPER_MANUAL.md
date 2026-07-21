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
* **設計理念**：提供 1-indexed 標準分頁，內建 `totalPages` 自動計算 (`ceil(total / pageSize)`) 與防呆機制。
* **核心欄位**：`pageNum` (頁碼), `pageSize` (每頁筆數), `total` (總筆數), `totalPages` (總頁數), `list` (資料列表)。
* **使用範例**：

#### 範例 1：靜態工廠與 Builder 構建
```java
// 1. 靜態工廠常用方式 (傳入 頁碼, 每頁筆數, 總筆數, 數據列表)
PageResult<UserVO> page = PageResult.of(pageNum, pageSize, total, userList);

// 2. 空頁建構 (當查詢筆數為 0 時)
PageResult<UserVO> emptyPage = PageResult.empty(pageNum, pageSize);

// 3. Builder 模式鏈式構建
PageResult<UserVO> customPage = PageResult.<UserVO>builder()
    .pageNum(1)
    .pageSize(20)
    .total(85L)
    .list(userList)
    .build();
```

#### 範例 2：於 `BaseBL` 與 Controller 中搭配 `ApiResult<PageResult<T>>` 回傳
```java
// 在 BL 或 Controller 中統一打包為 ApiResult 回傳前端
public class UserQueryBL extends BaseBL<UserQueryRequest, PageResult<UserVO>> {

    @Override
    protected PageResult<UserVO> executeBusiness(UserQueryRequest request) throws Exception {
        long total = userMapper.countUsers(request);
        List<UserVO> list = userMapper.selectUserPage(request);
        
        // 自動計算 totalPages (例如 total=85, pageSize=20 則 totalPages=5)
        return PageResult.of(request.getPageNum(), request.getPageSize(), total, list);
    }
}

// REST Controller 回傳 JSON 結構：
// {
//   "code": 200,
//   "success": true,
//   "message": "Success",
//   "time": "2026-07-21 11:22:47",
//   "data": {
//     "pageNum": 1,
//     "pageSize": 20,
//     "total": 85,
//     "totalPages": 5,
//     "list": [ ... ]
//   }
// }
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

### 4.1 主流框架規範：MyBatis + Spring `@Transactional`
* 業務操作建議於 Delegate / Controller 層加註 `@Transactional`。
* 異動操作在 `executeBusiness` 內部完成，無須手動 Commit/Rollback。
* 原生 JDBC 引擎 (`ISqlExecutor`) 視為選配基礎設施，僅用於微秒級極速需求。

---

### 4.2 應用端 DAO / Mapper 開發 SOP (4 步驟示範)

所有應用模組 (如 `net.yefangwong.patchverify`) 的 DAO 開發統一遵循以下 4 步驟 SOP：

#### Step 1: 建立應用端 Entity (繼承 `BaseEntity<ID>`)
```java
package net.yefangwong.patchverify.entity;

import net.yefangwong.csp.common.entity.BaseEntity;

public class VulnerabilityEntity extends BaseEntity<Long> {
    private String cveId;
    private String severity;
    private String status;

    // Getters & Setters ...
}
```

#### Step 2: 建立 Mapper 介面加註 `@Mapper`
```java
package net.yefangwong.patchverify.dao;

import net.yefangwong.patchverify.entity.VulnerabilityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface VulnerabilityMapper {
    int insert(VulnerabilityEntity entity);
    VulnerabilityEntity selectByCveId(@Param("cveId") String cveId);
    int updateStatus(@Param("cveId") String cveId, @Param("status") String status);
    List<VulnerabilityEntity> selectPage(@Param("offset") int offset, @Param("limit") int limit, @Param("status") String status);
    long countTotal(@Param("status") String status);
}
```

#### Step 3: 編寫 Mapper.xml SQL 檔
`src/main/resources/mapper/VulnerabilityMapper.xml`:
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="net.yefangwong.patchverify.dao.VulnerabilityMapper">

    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO tbl_vulnerability (cve_id, severity, status, remark, created_at, updated_at)
        VALUES (#{cveId}, #{severity}, #{status}, #{remark}, #{createdAt}, #{updatedAt})
    </insert>

    <select id="selectByCveId" resultType="net.yefangwong.patchverify.entity.VulnerabilityEntity">
        SELECT id, cve_id AS cveId, severity, status, remark, created_at AS createdAt, updated_at AS updatedAt
        FROM tbl_vulnerability
        WHERE cve_id = #{cveId}
    </select>

    <update id="updateStatus">
        UPDATE tbl_vulnerability
        SET status = #{status}, updated_at = NOW()
        WHERE cve_id = #{cveId}
    </update>

</mapper>
```

#### Step 4: 在 `BaseBL` 的 `executeBusiness()` 中直連 Mapper 操作
```java
public class PatchApproveBL extends BaseBL<PatchApproveRequest, PatchApproveResponse> {
    private final VulnerabilityMapper vulnerabilityMapper;

    public PatchApproveBL(VulnerabilityMapper vulnerabilityMapper) {
        this.vulnerabilityMapper = vulnerabilityMapper;
    }

    @Override
    protected PatchApproveResponse executeBusiness(PatchApproveRequest request) throws Exception {
        vulnerabilityMapper.updateStatus(request.getCveId(), "APPROVED");
        return new PatchApproveResponse(request.getCveId(), "APPROVED");
    }
}
```

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
