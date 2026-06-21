# Upwork 接案定位與專案交付規劃

本文件記錄專案開發者（Mark Wong）在 Upwork 平台上的競爭優勢定位，以及第一個接案服務商品（Project Catalog）的規格設計與本專案（Madaga）的代碼演示規劃。

---

## 核心競爭優勢定位 (Core Strengths)

在 Upwork 上，相較於高度競爭且飽和的 React/前端 UI 設計，Mark 擁有更具稀缺性且高價值的後端與領域技術優勢：

1. **Java & Spring Boot**：現代化企業級後端核心開發。
2. **保險領域知識 (Insurance Domain Knowledge)**：理解複雜的保險業務邏輯（投保、理賠、精算、再保等）。
3. **Oracle / DB2 / PostgreSQL**：擅長處理傳統與大型企業常用資料庫。
4. **舊系統現代化 (Legacy System Modernization)**：協助企業將老舊系統（例如 COBOL、Mainframe、EJB 等）重構成 Spring Boot 微服務或 API。
5. **API 整合 (API Integration)**：跨系統對接與數據傳輸。

---

## 第一個 Upwork Project Catalog 設計

### 服務標題 (Title)
> **Spring Boot REST API Development and Integration**

### 核心技術與功能展示 (Core Deliverables)
* **Spring Boot 3** 專案骨架
* **REST API** 設計規範與實作
* **Swagger/OpenAPI** 自動化接口文件
* **JWT Authentication** 安全認證機制
* **Oracle / DB2 / PostgreSQL / MySQL** 整合與配置
* **Unit Test** 單元測試覆蓋率演示
* **Deployment Guide**（Docker, GCP, App Engine 等部署指南）

### 作品集展示資產 (Portfolio Assets)
為了在 Upwork 商品頁面上達成最佳視覺與技術說服力，將準備以下展示資源：
1. **GitHub Demo**：本專案（Madaga / CSP）整理後的開源/展示倉庫連結。
2. **Swagger UI 畫面**：精美的 API 測試與文件畫面截圖。
3. **API 架構圖**：清晰的後端階層式（Controller, Service, Repository）及資料庫流程架構圖。
4. **系統截圖**：運行狀態、日誌、部署成功的監控畫面等。

---

## 本專案 (Madaga) 的調整配合方向

為了完美對接上述的 Project Catalog 展示需求，本專案後續將專注於以下調整：

1. **升級與標準化 REST API**：
   * 在 `csp-api` 模組中建立一組符合 Swagger 規範的高品質 REST APIs（可用保險業務做為 Mock 業務背景，例如「保單查詢」、「保費試算」）。
2. **Swagger / OpenAPI 3 整合**：
   * 引入 Springdoc-openapi 依賴，實現開箱即用的 Swagger UI。
3. **JWT 認證演示**：
   * 實作完整的 JWT Token 發行與過濾鏈驗證。
4. **雙資料庫支援演示**：
   * 提供 `Oracle` 與 `PostgreSQL`（以及本機 `SQLite` 或 `MySQL`）的 Profile 切換範例與連接配置。
5. **編寫完善的 README 與 Deployment Guide**。

---

*最後更新時間：2026-06-19*
