# 📘 CSP 平台開發與架構規範手冊 (Cornelius Service Platform Developer Manual)

> **專案名稱**：Cornelius Service Platform (CSP 平台)  
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
4. **AI Agent 動手改 Code 前強制維護 `PATCH_PLAN.md` 鐵律**：
   * **AI Agent 在真正創建或修改任何生產程式碼 (.java) 與測試檔之前，必須先建立並更新專案根目錄下的 `PATCH_PLAN.md` 檔案（人類工程師無需手動維護）**。

---

### 🛡️ 1.3 雙重實證與極限酷刑門禁 (Evidence-First & Gauntlet Protocol)
為了確保高風險與高保證模組（如金融交易清算、資安可達性評估、權限控制等）之程式碼品質，本專案導入由 Uncle Bob 啟發之 **`old-coder` 雙重實證開發協定**：

1. **觸發關鍵字 (Triggers)**：
   當使用者或工單標明包含：「`請用老碼農模式`」、「`prove it works`」、「`TDD 實作`」、「`高保證模組`」或要求發起極限測試門禁時，Agent 必須自動載入 `old-coder` 技能。

2. **核心開發閉環 (SPEC ➔ GAUNTLET ➔ EVIDENCE Loop)**：
   * 📋 **SPEC (測試計畫簽核)**：代碼撰寫前，Agent 必須產出包含明確測資與邊界條件之測試計畫供審查。
   * 🔴 **RED ➔ 🟢 GREEN ➔ 🧹 REFACTOR**：嚴格執行 TDD 測試驅動開發。
   * 🛡️ **GAUNTLET (8 大酷刑檢查)**：包含 Full Test Suite、Types+Lint、100% Changed-line Coverage、Mutation Testing (變異測試)、Property-based Testing、Real Sandbox Execution、Supply Chain & Secrets 與 Suite Health。
   * 📊 **EVIDENCE (實證報告)**：開發完成後提交具備跑分數據之 `EVIDENCE` 報告，人類工程師審查報告與測試結果後即可採納。


---

### 🏛️ 1.2 SID 6大領域目錄結構與 DB 命名規範 (TMF SID & eTOM Taxonomy)
所有的微服務、API 路由、前端 View 模組以及 Java 包名，統一對齊 **TMF SID 6 大主領域 (傳承 2012 年翁藝芳架構師第一版 CSP 藍圖)**：

```text
net.yefangwong.csp.domain/
├── product/      # 產品、資費、訂閱方案 (Product Offering, Pricing, Package)
├── customer/     # 客戶資料、用戶權限、SLA (Customer Profile, User Auth, SLA)
├── service/      # 邏輯服務、Agent 工作流 (Service Activation, Workflow Orchestration)
├── resource/     # AI 算力模型、基礎設施 (AI Compute Providers, Tokens, Infrastructure)
├── supplier/     # 第三方 API 與外包供應商 (Vendor Wrappers, External Integrations)
└── market/       # 行銷活動、營運數據分析 (Marketing, Sales & Usage Analytics)
```

**資料庫 Table 命名規範**：
- `tbl_prod_*`：產品與資費領域資料表 (例：`tbl_prod_offering`, `tbl_prod_pricing`)
- `tbl_cust_*`：客戶與權限領域資料表 (例：`tbl_cust_profile`, `tbl_cust_sla`)
- `tbl_serv_*`：邏輯服務與流程資料表 (例：`tbl_serv_activation`, `tbl_serv_workflow`)
- `tbl_res_*`：算力與資源領域資料表 (例：`tbl_res_provider`, `tbl_res_usage_log`)
- `tbl_supp_*`：供應商與外部介面資料表 (例：`tbl_supp_vendor`)
- `tbl_mkt_*`：行銷與統計領域資料表 (例：`tbl_mkt_analytics`)


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

## 🌿 6. 前端編譯與低碳化壓縮規範 (Frontend ESG Build Guidelines)

為了實踐 **Madaga CSP 永續軟體原則 (ESG Green Software Guidelines)**，降低單次系統診斷之網路傳輸與裝置能耗，前端開發必須強制引入編譯打包工具，並在建置階段進行最佳化。

### 6.1 綠色編譯與優化標準 (Green Compilation & Optimization)

#### 1. Tree Shaking (搖樹最佳化)
* **核心機制**：
  在傳統 `<script>` 載入函式庫（例如整個 `lodash.min.js`）的方式下，瀏覽器通常必須下載並解析整個函式庫，即使實際只使用其中少數函式。若專案引入多個大型函式庫，就可能增加下載、解析與執行成本。
  採用 Vite 等現代建構工具時，專案通常會使用 ES Modules（`import` / `export`）。在執行 `npm run build` 時，Rollup 會建立模組之間的依賴關係圖（Dependency Graph），並執行 **Tree Shaking**（Dead Code Elimination），只將實際使用到的程式碼及其必要依賴打包到最終 Bundle，其餘未使用的程式碼則**根本不會被加入最終的 Bundle**。
* **ES Modules (ESM) 前提條件**：
  Tree Shaking 的關鍵在於程式碼必須使用靜態導入的 ES Modules。
  * **可最佳化 (ESM)**：`import { debounce } from "lodash-es";` (可執行靜態分析)
  * **無法最佳化 (CommonJS)**：`const _ = require("lodash");` (動態載入在編譯時難以進行靜態分析，故無法執行 Tree Shaking)
* **依賴打包 (Dependency Handling)**：
  Rollup 並非只保留單一函式本身的程式碼，而是會遞迴分析並打包該函式運作所需的所有必要依賴（例如 `debounce` 會依賴 `isObject`、`now`、`toNumber` 等內部 Helper 函式）。

#### 2. Minification & Compression (程式碼最小化與壓縮)
* **優化機制**：
  自動移除原始碼中所有人類閱讀用的空白、縮排、換行及註解，並進行變數與函式名稱混淆（Obfuscation）與語法結構精簡。
* **效能與 ESG 關聯**：
  最終輸出的 JavaScript 檔案通常比直接引入整個函式庫小得多，可減少下載量、降低瀏覽器 JavaScript 引擎（如 Chrome 的 V8）進行解析 (Parse)、編譯 (Compile) 與執行的運算成本。
  對於高流量網站而言，這種最佳化能有效減少網路資料傳輸量；長期而言，可能降低整體頻寬與數據中心的能源消耗，進而間接減少碳排放。

### 6.2 前端 Vite 整合建置與輸出路徑 (Vite Config & Deploy Workflow)
前端 Vue 3 / Vite 專案必須獨立置於根目錄 `frontend/` 資料夾中，並於 `vite.config.js` 中將編譯目標 `outDir` 設定為後端的資源目錄，實現「開發分離，部署合一」的單伺服器架構：

```javascript
// frontend/vite.config.js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  build: {
    // 💡 將編譯後的純靜態 HTML/JS/CSS 輸出至 Java 靜態資源區
    outDir: path.resolve(__dirname, '../src/main/resources/static'),
    emptyOutDir: true, // 編譯前自動清空舊資源
    minify: 'esbuild', // 使用極速且高壓縮率的 esbuild 作為壓縮器
  }
})
```

### 6.3 執行編譯與部署指令
前端工程師於本地開發時，需於 `frontend/` 目錄下執行以下指令：
```bash
# 1. 啟動本地開發伺服器 (即時熱更新)
npm run dev

# 2. 進行 ESG 打包編譯，並自動部署到 Spring/Java 靜態資源目錄
npm run build
```
這項工序可以保證前端資源在進入 Java JAR 包發佈前已完成「綠色低碳化」，有效降低伺服器頻寬負載與終端使用者下載時的碳足跡，高度符合本系統 **`0.02g CO₂/scan`** 的低碳排架構要求。

---

## ⚡ 7. 算力代理層開發與硬體擴充規範 (Compute Resource Proxy & Compute Drivers)

為了確保 Madaga CSP 平台具備 **Zero Framework Lock-in** 與高前瞻擴充性，底層所有 GPU / AI 算力一律透過 PureMVC 代理層 `ComputeResourceProxy` 進行介接與封裝。

### 7.1 統一介面與驅動器架構 (`IComputeResourceProxy`)
* **Package**: `net.yefangwong.csp.proxy.ComputeResourceProxy`
* **驅動器分流與硬性門禁設計 (Capabilities-Based Adapter Pattern & Pre-Flight Gate)**：
  1. **`ForbiddenVectorInterceptor` (Pre-Flight 攔截器)**：
     - **運作機制**：掛載於 `CapabilityRouter` 的最前端。比對包含 80% 失敗負樣本（非物理能階、語意死鎖鏈路、發散參數）之 HNSW 向量空間。
     - **職責**：若命中既有失敗禁區，微秒級直接回傳 `ApiResult.failure(400, "HIT_FORBIDDEN_CLUSTER")`，不浪費任何 GPU 算力或機器人濕實驗資源，貫徹 **`0.02g CO₂/scan`** 綠色承諾。
  2. **`LocalHeterogeneousDriver` (預設驅動器)**：
     - **硬體基底**：管理現有 **NVIDIA RTX 3070 (8GB) + AMD RX 6600 XT (8GB) 雙卡硬體並聯與異構算力池**。
     - **職責**：負責日常 SAST 弱點診斷、中小模型 RAG 檢索、單元測試 Mock 與低碳掃描。
  3. **`NvidiaDgxSparkDriver` (外掛擴充驅動器)**：
     - **硬體基底**：透過 gRPC / CUDA RPC 外掛支援未來 **NVIDIA DGX Spark (GB10 128GB Unified Memory)** 節點。
     - **職責**：當任務標記包含 `task.requireUnifiedMemory = true` 或進行超大模型 Kernel Forge MCTS 算子優化時自動派發。

### 7.2 開發者使用範例 (DataPipeline 算力標記)
```java
// 在 BL 中打包 DataPipeline 傳入算力需求標記
DataPipeline pipeline = new DataPipeline(globalContext);
ComputeTask task = new ComputeTask();
task.setRequireUnifiedMemory(true); // 標記需要超大統一記憶體 (自動路由至 DGX Spark 節點)

pipeline.add(task);
ComputeResult result = computeResourceProxy.executeTask(pipeline);
```

---

## 🔒 8. 安全防護與 RBAC 權限架構 (Security & Access Control Architecture)

Cornelius 入口模組 `csp-portal-web` 採用 Spring Security 6 作為底層安全框架，整合企業級角色存取控制 (RBAC)、資料庫式動態鑑權與雙層縱深防禦機制。

### 8.1 安全配置現況與核心參數 (Security Baseline)
* **安全框架版本**：Spring Security 6.1.5 (`spring-boot-starter-security`)。
* **密碼安全策略**：全面強制採用 `BCryptPasswordEncoder` (工作因子 Strength=10)，禁止任何明文密碼落地。
* **登入機制**：標準表單登入 (`/login` ➔ `/doLogin`)，登入成功導向 `/`，失敗導向 `/login?error=true`。
* **403 存取拒絕雙模處理 (`CustomAccessDeniedHandler`)**：
  - **AJAX / REST 請求 (`X-Requested-With: XMLHttpRequest` 或 `Accept: application/json`)**：回傳 HTTP 403 JSON (`{"code": 403, "msg": "FORBIDDEN", "data": "權限不足"}`)。
  - **一般頁面瀏覽**：重定向轉發至友善的 403 錯誤頁面 (`/403` 或 `/error/403.html`)。

### 8.2 3 大核心角色與 6 大模組權限矩陣 (RBAC Matrix)

系統內建三種標準運營角色與六項業務模組權限：

| 模組代碼 (`code`) | 模組名稱 | 對應 URL Pattern | `ROLE_ADMIN` (管理員) | `ROLE_MANAGER` (經理) | `ROLE_EMPLOYEE` (普通員工) |
| :--- | :--- | :--- | :---: | :---: | :---: |
| `employee` | 員工管理 | `/employee/**` | 完整增刪改查 | 完整增刪改查 | 僅限 GET 查詢 (唯讀) |
| `department` | 部門管理 | `/department/**` | 完整增刪改查 | 完整增刪改查 | ❌ 禁止存取 (403) |
| `sysUser` | 用戶管理 | `/sysUser/**` | 完整增刪改查 | ❌ 禁止存取 (403) | ❌ 禁止存取 (403) |
| `sysRole` | 角色管理 | `/sysRole/**` | 完整增刪改查 | ❌ 禁止存取 (403) | ❌ 禁止存取 (403) |
| `sysPermission`| 權限管理 | `/sysPermission/**`| 完整增刪改查 | ❌ 禁止存取 (403) | ❌ 禁止存取 (403) |
| `common` | 通用運營 | `/dashboard/**`, `/profile/**` | 完整操作 | 完整操作 | 完整操作 |

### 8.3 資料模型與多角色設計 (Data Model & Multi-Role Schema)
* **實體關聯**：
  - `sys_user`：帳號基本資料（密碼儲存 BCrypt Hash）。
  - `sys_role`：角色主表（包含 `code: ROLE_ADMIN`, `name: 管理員` 等）。
  - `sys_permission`：權限資源表（包含 `name`, `code`, `url`）。
  - `sys_role_permission`：角色與權限關聯表。
  - `sys_user_role`：用戶與角色多對多關聯表（`user_id`, `role_id`），支援單一用戶指派多角色。
* **使用者載入服務 (`UserDetailsServiceImpl`)**：
  在 `loadUserByUsername` 中，同時將用戶擁有的角色（`ROLE_ADMIN` 等）與權限代碼（`employee` 等）裝配入 Spring Security 的 `GrantedAuthority` 集合，確保 `hasRole(...)` 與 `hasAuthority(...)` 雙向判定均能生效。

### 8.4 前端 UI 授權控制與圓角色塊標籤規範 (UI Authorization & Badges)
1. **Thymeleaf 權限標籤**：
   在 `pom.xml` 引入 `thymeleaf-extras-springsecurity6`，並在 HTML 側邊欄透過 `sec:authorize="hasRole('ADMIN')"` 動態隱藏非授權選單。
2. **圓角色塊標籤 (Tag / Badge) 樣式規範**：
   在角色清單與用戶清單中，關聯的權限與角色必須以圓角色塊標籤匡列：
   ```css
   .tag-badge {
       display: inline-block;
       padding: 2px 10px;
       border-radius: 12px;
       font-size: 12px;
       font-weight: 500;
       margin-right: 4px;
       margin-bottom: 2px;
   }
   .tag-admin { background-color: #fee2e2; color: #991b1b; border: 1px solid #f87171; }
   .tag-manager { background-color: #e0e7ff; color: #3730a3; border: 1px solid #818cf8; }
   .tag-employee { background-color: #ecfdf5; color: #065f46; border: 1px solid #34d399; }
   .tag-perm { background-color: #f1f5f9; color: #334155; border: 1px solid #cbd5e1; }
   ```

---

## 🏛️ 9. 開源潔淨室交易提交架構 (Clean-Room UnitOfWork & TxSubmitter Pattern)

為了達成高可移植性、Zero Framework Lock-in 與嚴格智慧財產權潔淨室 (Clean Room) 隔離，本平台之所有業務邏輯層嚴格遵守「去 Spring 污染」架構模式。

### 9.1 核心鐵律 (The Golden Rules)
1. **去 Spring 污染與填空式開發 (Zero-Spring Pollution in BL)**：
   業務邏輯單元 `BaseBL<REQ, RESP>` 必須為 **100% 純 Java POJO**，嚴禁在 BL 內部使用 `@Autowired` 或引入 Spring 容器套件。
2. **開源潔淨室命名準則 (脫敏鐵律)**：
   - 專案為開源專案，**嚴禁使用客戶原廠私有名稱**（嚴禁寫原廠系統代號，統稱 **Lisbon 模式**；嚴禁提及外部特定商業廠商）。
   - 全面強制採用開源標準命名：
     * `TxSubmitter`：集中資料庫交易提交引擎（取代私有提交器）。
     * `UnitOfWork`：記憶體變更收集袋（取代私有 Map / 容器）。
     * `DataPipeline`：資料流傳遞管道。
     * `ISqlExecutor`：底層 SQL 執行抽象介面。
     * `actionCode`：操作代碼（`INSERT`, `UPDATE`, `DELETE`）。
     * `AppError / AppErrors`：自定義無依賴錯誤診斷物件。

### 9.2 純 Java `BaseBL` 五大工序 (5-Step Execution Pipeline)
所有繼承 `BaseBL` 之業務邏輯實體必須循序完成 5 大標準工序：
```text
1. getInputData()   ➔ 參數抽取與型別綁定
2. checkInputData() ➔ 輸入校驗與業務防呆 (零 Spring Validator 依賴)
3. dealData()       ➔ 核心業務計算，組裝 UnitOfWork
4. prepareOutput()  ➔ 封裝業務響應物件 (RESP)
5. execute() 閉環    ➔ 調用 txSubmitter.submit(uow) 提交交易，或交由外層 Delegate 集中處理
```

詳細範例與架構白皮書參見：[[facts/cornelius_clean_room_unit_of_work_and_tx_submitter_pattern.md]]。

---

## 🛠️ 10. 維護者維運與除錯 Know-How (Maintainer Operational Know-How)

本章節為維運與後續接手工程師提供系統核心診斷指南。

### 10.1 密碼管理與重置 SOP
當管理員或使用者忘記密碼時，嚴禁在資料庫中直接更新明文密碼，必須使用 BCrypt 雜湊：
```bash
# 透過 Spring CLI 或本地 Java 快速產生 BCrypt Hash (強度 10)
# 密碼 123456 之合法 Hash 範例：$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi
```
SQL 重置範例：
```sql
UPDATE sys_user 
SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi' 
WHERE username = 'admin';
```

### 10.2 403 Forbidden 存取拒絕快速排查手冊
當用戶反應操作被攔截（出現 403）時，依序檢查以下 4 個排查點：
1. **檢查角色前綴 (`ROLE_`)**：
   Spring Security 之 `hasRole('ADMIN')` 會自動比對 `ROLE_ADMIN`。若資料庫 `sys_role.code` 僅寫 `ADMIN`，則 `UserDetailsServiceImpl` 載入時必須補上 `ROLE_` 前綴，否則權限比對會靜默失效。
2. **檢查 URL 攔截規則順序**：
   在 `SecurityConfig` 中，`requestMatchers` 的聲明順序必須遵守「**由狹窄到寬鬆 (Narrow to Broad)**」原則。若先聲明了 `.anyRequest().authenticated()`，後續定義的 `.requestMatchers("/admin/**").hasRole("ADMIN")` 將永不生效。
3. **檢查 CSRF Token**：
   若是 POST/PUT/DELETE 請求遭拒絕，且伺服器日誌顯示 `Invalid CSRF Token`，請確認前端表單中是否包含 `<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}" />`，或 REST 請求標頭是否攜帶 `X-CSRF-TOKEN`。
4. **檢查多角色合併狀態**：
   若用戶同時具備 `ROLE_MANAGER` 與 `ROLE_EMPLOYEE`，請查驗 `sys_user_role` 是否完整載入兩筆記錄，且 GrantedAuthority 是否包含聯集。

### 10.3 新增業務模組與權限五部曲 (Step-by-Step Expansion SOP)
當維護工程師需要在 CSP 平台擴充一個新業務模組（例如「稽核日誌模組 `audit`」）時，必須循序執行以下 5 步：
1. **步驟 1 (DB 字典註冊)**：在 `sys_permission` 插入資源記錄 (`code: audit`, `name: 稽核日誌`, `url: /audit/**`)。
2. **步驟 2 (角色綁定)**：在 `sys_role_permission` 將該權限 ID 賦予 `ROLE_ADMIN` 的角色 ID。
3. **步驟 3 (路由防護聲明)**：在 `SecurityConfig` 聲明 `.requestMatchers("/audit/**").hasAnyRole("ADMIN")`。
4. **步驟 4 (前端選單授權)**：在側邊欄 `default.html` 選單節點標記 `sec:authorize="hasRole('ADMIN')"`。
5. **步驟 5 (純 Java BL 實作)**：業務邏輯單元繼承 `BaseBL`，使用 `UnitOfWork` 收集稽核日誌，絕不使用 `@Autowired`。

---

## 📚 相關參考文件 (References & Sources)
* 需求中心：[[Projects/startup/madaga/BRD.md]]
* 安全架構與維護事實檔：[[facts/cornelius_csp_portal_security_architecture_and_maintenance_guide.md]]
* 開源潔淨室 UnitOfWork 模式事實檔：[[facts/cornelius_clean_room_unit_of_work_and_tx_submitter_pattern.md]]
* 開源架構規範規則：[`.agents/rules/cornelius_architecture_and_development_pattern_rule.md`](file:///Users/yefangwong/Knowledge/.agents/rules/cornelius_architecture_and_development_pattern_rule.md)
* 平台歷史與演進：[[Projects/startup/madaga/CSP_HISTORY.md]]

---
*本手冊由 Madaga CSP 架構小組維護，如有新增模組規範請同步更新。*

