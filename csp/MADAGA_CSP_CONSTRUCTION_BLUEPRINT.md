# 🏗️ Madaga CSP 專案架構施工藍圖 (Construction Blueprint)

## 📌 1. 施工目標與總則

* **目標專案**: `/Users/yfwong/Projects/madaga/csp` (Cornelius Service Platform)
* **目標 Group ID / Base Package**: `net.yefangwong.csp` (個人私有產權標準 Package)
* **目標 JDK / Maven**: JDK 17+ 基準，相容 `madaga/csp/pom.xml` 15 模組架構。
* **潔淨代碼 (Clean Room) 鐵律**:
  1. 100% 使用原創潔淨命名 (`ApiResult`, `PageResult`, `AppError`, `AppErrors`, `DataPipeline`, `GlobalContext`, `ISqlExecutor`, `NativeSqlExecutor`, `DataRecordSet`, `BaseBL`, `BaseEntity`)。
  2. 嚴禁任何舊系統關鍵字、舊預留字與匈牙利命名法前綴 (`actionCode` 取代 `cOperate`)。
  3. 支援 `new BL()` / `new DAO()` 手動實例化，100% 確保線程安全與零共享狀態。

### 🛡️ 1.1 平台資產 (Madaga CSP) 與 專屬業務 (PatchVerify) 完全切割原則
1. **Madaga CSP 平台層 (Platform Core - `net.yefangwong.csp.*`)**：`madaga/csp` 僅沉澱 **100% 業務中立 (Business-Agnostic)** 的通用抽象平台類別 (`net.yefangwong.csp.common.api`, `common.bl`, `common.context`, `common.db`, `common.entity`, `tools`)。**絕不包含任何 `patchverify` 或特定應用領域之套件名稱**。
2. **PatchVerify 專屬業務層 (Domain Application Core - `net.yefangwong.patchverify.*`)**：所有與 **CVE 漏洞修補、Jacoco 覆蓋率診斷、GitHub Revert PR、ISO 27001 審計** 強綁定之具體業務，獨立於 `patch-verify` 應用專案中，100% 確保平台層中立與商業 IP 獨立性！

---

## 📐 2. 15 個 Maven 模組對齊施工樹 (Platform Construction Tree)

```text
/Users/yfwong/Projects/madaga/csp/
├── csp-common/
│   ├── src/main/java/common/api/
│   │   └── ApiResult.java                          <-- 階段 1: 歷史相容相容路徑 (已完成 timestamp 簡化)
│   ├── src/main/java/net/yefangwong/csp/common/api/
│   │   └── PageResult.java                         <-- 階段 1: 新起標準 Package 頁面數據封裝
│   └── src/main/java/net/yefangwong/csp/common/error/
│       ├── AppError.java                           <-- 階段 1: 新起標準 Package 單筆全域錯誤診斷載體
│       └── AppErrors.java                          <-- 階段 1: 新起標準 Package 多重錯誤累積與鏈式診斷容器
├── csp-base/
│   └── src/main/java/net/yefangwong/csp/common/bl/
│       └── BaseBL.java                             <-- 階段 1: 5大生命週期抽象 BL (Boolean 簡明判斷)
├── csp-base-domain/
│   └── src/main/java/net/yefangwong/csp/common/context/
│       ├── GlobalContext.java                      <-- 階段 2: 全域操作上下文
│       └── DataPipeline.java                       <-- 階段 2: 萬能數據傳遞管道
├── csp-domain-jpa/
│   └── src/main/java/net/yefangwong/csp/common/entity/
│       ├── BaseEntity.java                         <-- 階段 2: 基礎實體抽象類
│       └── EntitySet.java                          <-- 階段 2: 實體集合承載體
├── csp-persistence-api/
│   └── src/main/java/net/yefangwong/csp/common/db/
│       ├── ISqlExecutor.java                       <-- 階段 3: Native JDBC 執行器介面
│       └── DataRecordSet.java                      <-- 階段 3: SQL 結果集承載體
├── csp-persistence-jpa/
│   └── src/main/java/net/yefangwong/csp/common/db/
│       └── NativeSqlExecutor.java                  <-- 階段 3: JDBC 綁參實作
└── csp-tools/
    └── src/main/java/net/yefangwong/csp/tools/
        └── CspSchemaCompiler.java                  <-- 階段 3.5: DDL/Markdown 自動生成工具 (支援 --enable-batch)
```

---

## 🛠️ 3. 分階段 Step-by-Step 施工檢查表 (Platform Construction Checklist)

### 🔨 階段 1：基礎通用資產層施工 (Modules: `csp-common` & `csp-base`)
- [x] **Step 1.1**: 在 `csp-common` 建立 `common.api.ApiResult` (含 Lombok / 純 JDK 工廠方法、i18n 多語系覆蓋與 100% 向下相容測試)。
  - 📝 **[已決策並完成 - 採納方案 A]**: 移除冗餘之 `long timestamp` 屬性與相關 Builder/Getter 方法，僅保留單一 `Date time` 狀態與時間來源，完成極簡 Clean Code 重構與測試。
- [x] **Step 1.2**: 在 `csp-common` 建立 `net.yefangwong.csp.common.api.PageResult` (含 pageNum, pageSize, total, totalPages, list，已完成 100% 單元測試)。
- [x] **Step 1.3**: 在 `csp-common` 建立 `net.yefangwong.csp.common.error.AppError` (單筆全域錯誤細節與診斷載體，已完成 100% 單元測試)。
- [x] **Step 1.4**: 在 `csp-common` 建立 `net.yefangwong.csp.common.error.AppErrors` (多重錯誤累積與鏈式診斷容器，已完成 100% 單元測試)。
- [x] **Step 1.5**: 在 `csp-base` 建立 `net.yefangwong.csp.common.bl.BaseBL` (規範 5 大生命週期，前置驗證回傳 `boolean`，內建 `AppErrors` 診斷集，已完成階段 1 建置)。

### 🔨 階段 2：通用上下文與實體底座施工 (Modules: `csp-base-domain` & `csp-domain-jpa`)
- [x] **Step 2.1**: 在 `csp-base-domain` 建立 `net.yefangwong.csp.common.context.GlobalContext` (operatorEmail, comCode, clientIp, role, traceId，已完成 100% 單元測試)。
- [x] **Step 2.2**: 在 `csp-base-domain` 建立 `net.yefangwong.csp.common.context.DataPipeline` (打包 GlobalContext 與 Payload 物件，已完成 100% 單元測試)。
- [ ] **Step 2.3**: 在 `csp-domain-jpa` 建立 `net.yefangwong.csp.common.entity.BaseEntity` (id, createdAt, updatedAt)。
- [ ] **Step 2.4**: 在 `csp-domain-jpa` 建立 `net.yefangwong.csp.common.entity.EntitySet` (強型別批次集合承載體)。

### 🔨 階段 3：原生 JDBC 引擎與工具施工 (Modules: `csp-persistence-api`, `csp-persistence-jpa` & `csp-tools`)
- [ ] **Step 3.1**: 在 `csp-persistence-api` 建立 `net.yefangwong.csp.common.db.ISqlExecutor` (execQuery, execUpdate, execBatch 介面)。
- [ ] **Step 3.2**: 在 `csp-persistence-api` 建立 `net.yefangwong.csp.common.db.DataRecordSet` (零反射開銷之結果集承載體)。
- [ ] **Step 3.3**: 在 `csp-persistence-jpa` 建立 `net.yefangwong.csp.common.db.NativeSqlExecutor` (原生 PreparedStatement 綁參實作)。
- [ ] **Step 3.4**: 在 `csp-tools` 建立 `net.yefangwong.csp.tools.CspSchemaCompiler` (自動解析 DDL/Markdown 表格，1秒生成強型別 Entity, EntitySet 與 Native DAO 程式碼之低程式碼工具，支援 `--enable-batch` 按需選配)。

---

## 🧪 4. 驗證與編譯步驟 (Verification & Build)

> 💡 **環境提示**：Madaga CSP 基於 JDK 17+ 標準建立，執行 Maven 編譯與測試前請確保 JAVA_HOME 指向 JDK 17+ (例如 `export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home`)。

1. **madaga/csp 平台層編譯與 install 到本地 Maven 庫**:
   ```bash
   cd /Users/yfwong/Projects/madaga/csp
   export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
   mvn clean install -DskipTests
   ```
2. **單元測試驗證**:
   ```bash
   export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
   mvn test
   ```
