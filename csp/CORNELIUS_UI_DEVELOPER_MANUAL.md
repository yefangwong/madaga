# 🎨 CorneliusUI 前端開發文件與 UI Developer 手冊指南

> **文件定義**：本文件為 **CorneliusUI (CSP 核心前端設計系統)** 之官方開發指南與手冊規範。  
> **線上互動手冊**：啟動前端後，訪問 [`http://localhost:5173/`](http://localhost:5173/) 即為 **Cornelius UI Developer 手冊**，提供所有視覺元件的即時效果預覽與「一鍵複製」程式碼。  
> **所屬專案**：鴻方移動智能科技 · Cornelius Service Platform (CSP)  
> **維護者**：yfwong (翁藝芳)  
> **版本**：v1.0.0  

---

## 📖 一、 Cornelius UI Developer 手冊定位與使用方式

### 1. 什麼是 Cornelius UI Developer 手冊？
在 `csp/csp-portal-web/frontend/index.html` 中所維護的畫面，即是 **Cornelius 官方 UI Developer 視覺手冊**。它參照 Apple Developer 規範結構打造，專門為團隊成員（包含後端工程師、前端開發者與專案管理者）提供**最直覺、不用死記語法、複製貼上即可用**的視覺元件庫。

### 2. 如何開啟線上手冊？
只要啟動前端即時預覽伺服器，即可在瀏覽器隨時查閱手冊：
```bash
# 1. 進入前端工作目錄
cd csp/csp-portal-web/frontend

# 2. 啟動本機伺服器 (具備 50ms 極速熱重載)
npm run dev
```
啟動後打開瀏覽器訪問：👉 **[`http://localhost:5173/`](http://localhost:5173/)**

### 3. 開發者日常工作流（3 步驟「複製貼上」）
1. **瀏覽與挑選**：在手冊頁面頂部導覽列點選需要的分類（例如：`輸入框與表單`、`按鈕與標籤`、`卡片與表格`、`彈跳視窗`）。
2. **查看預覽**：在「實際效果預覽」區塊查看互動狀態（輸入、聚焦橙光、下拉、按鈕懸浮、彈窗等）。
3. **一鍵複製貼上**：點擊程式碼區塊右上角的 **「複製程式碼」** 按鈕，直接貼進您的 Thymeleaf / HTML 頁面即可運行！

---

## 🧩 二、 元件分類與關鍵 Class 速查表 (Cheat Sheet)

| 分類 | 元件名稱 | 關鍵 CSS Class / 語法 | 1 句話白話用途 |
| :--- | :--- | :--- | :--- |
| **表單輸入** | 標準文字輸入框 | `class="cui-input"` | 44px 觸控高，點擊發出暖橙光暈 |
| **表單輸入** | 等寬數字/金額框 | `class="cui-input cui-num"` | 搭配 Montserrat 等寬排版，數值對齊不跳動 |
| **表單輸入** | 唯讀停用輸入框 | `class="cui-input" disabled` | 灰色鎖定，防止使用者竄改資料 |
| **表單輸入** | 下拉選單 | `class="cui-select"` | 內建跨平台向量 Chevron 箭頭，點擊展開選單 |
| **表單輸入** | 複合搜尋工具列 | `class="search-demo-grid"` | 電腦版自動橫排齊平、手機版自動整齊換行 |
| **動作按鈕** | 膠囊主按鈕 (推薦) | `class="cui-btn cui-btn-primary cui-btn-pill"` | 暖磚橙圓弧主按鈕，用於「確認送出/搜尋」 |
| **動作按鈕** | 次要按鈕 | `class="cui-btn cui-btn-secondary"` | 淺灰底色按鈕，用於「取消/返回」 |
| **動作按鈕** | 外框按鈕 | `class="cui-btn cui-btn-outline"` | 白底細框按鈕，用於「匯出/重設」 |
| **動作按鈕** | 危險動作按鈕 | `class="cui-btn cui-btn-danger"` | 紅色按鈕，用於「刪除/終止」 |
| **動作按鈕** | 按鈕群組 | `class="cui-btn-group"` | 無縫相連的按鈕組（如 Share + Export） |
| **動作按鈕** | 操作工具列 | `class="cui-toolbar"` | 彈性橫向排列按鈕與下拉選單之工具列 |
| **狀態指示** | 成功/通過標籤 | `class="cui-badge cui-badge-success"` | 綠色徽章，代表「正常/已核准」 |
| **狀態指示** | 審核/排隊標籤 | `class="cui-badge cui-badge-warning"` | 黃色徽章，代表「排隊中/處理中」 |
| **狀態指示** | 異常/失敗標籤 | `class="cui-badge cui-badge-danger"` | 紅色徽章，代表「連線異常/失敗」 |
| **狀態指示** | 草稿/中立標籤 | `class="cui-badge cui-badge-neutral"` | 灰色徽章，代表「草稿/未發布」 |
| **排版網格** | 響應式網格系統 | `class="cui-grid cui-grid-2 / 3 / 4"` | 支援 1~12 欄等寬與手機自動折行網格 |
| **數據展示** | 統計指標卡片 | `class="cui-card cui-stat-card cui-card-interactive"` | 儀表板 KPI 大數字，滑鼠移入有微浮起動畫 |
| **數據展示** | 資料清單表格 | `class="cui-table-container"` + `class="cui-table"` | 斑馬線清單，手機自動支援橫向滾動 |
| **反饋彈窗** | 毛玻璃模態視窗 | `CorneliusUI.openDialog('id')` | 點擊跳出彈窗，支援點擊遮罩/Escape 關閉 |
| **工具函式** | 原生分享/複製連結 | `CorneliusUI.share()` | 呼叫系統分享面板或自動複製連結 |
| **工具函式** | 表格匯出 CSV | `CorneliusUI.exportTableToCSV('table')` | 一鍵將指定 HTML 表格匯出為 CSV 檔 |

---

## 🔌 三、 在 Spring Boot / Thymeleaf 頁面中引入

後端 Thymeleaf 模板僅需標準引入即可全站套用 CorneliusUI：

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8" />
  <title>CSP 系統頁面</title>
  
  <!-- 1. 引入字體 (Montserrat + 思源黑體) 與 FontAwesome -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&family=Noto+Sans+TC:wght@300;400;500;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

  <!-- 2. 引入 CorneliusUI 生產樣式表 -->
  <link rel="stylesheet" th:href="@{'/dist/css/cornelius-ui.css'}" />
</head>
<body>

  <!-- 頁面內容：直接貼上 UI Developer 手冊複製的 HTML -->
  <div class="cui-card">
    <button class="cui-btn cui-btn-primary cui-btn-pill">確認送出</button>
  </div>

  <!-- 3. （選用）若有使用模態彈窗，在 </body> 前引入 JS -->
  <script th:src="@{'/dist/js/cornelius-ui.js'}"></script>
</body>
</html>
```

---

## 🎨 四、 鴻方名片色彩與 Token 規範

CorneliusUI 核心色調源自鴻方名片標準設計：

| CSS 變數 | 色碼 / 數值 | 預覽與用途 |
| :--- | :--- | :--- |
| `--cui-primary` | `#E26D38` | <span style="display:inline-block;width:12px;height:12px;background:#E26D38;border-radius:2px;"></span> **鴻方暖磚橙**（主品牌色、主按鈕、重點強調） |
| `--cui-primary-hover` | `#C95522` | <span style="display:inline-block;width:12px;height:12px;background:#C95522;border-radius:2px;"></span> 主色懸浮 Hover 加深 |
| `--cui-text-title` | `#1E1D1B` | <span style="display:inline-block;width:12px;height:12px;background:#1E1D1B;border-radius:2px;"></span> **深炭黑**（大標題、名片姓名文字） |
| `--cui-text-body` | `#3E3A36` | <span style="display:inline-block;width:12px;height:12px;background:#3E3A36;border-radius:2px;"></span> **正文內文**（表單輸入文字） |
| `--cui-text-secondary` | `#6B6661` | <span style="display:inline-block;width:12px;height:12px;background:#6B6661;border-radius:2px;"></span> **內文灰**（標籤副標、次要資訊） |
| `--cui-border` | `#E5E1DC` | <span style="display:inline-block;width:12px;height:12px;background:#E5E1DC;border-radius:2px;"></span> 柔和米灰框線色 |
| `--cui-bg-page` | `#F8F8F7` | <span style="display:inline-block;width:12px;height:12px;background:#F8F8F7;border-radius:2px;"></span> 紙質暖白背景 |
| `--cui-surface` | `#FFFFFF` | <span style="display:inline-block;width:12px;height:12px;background:#FFFFFF;border-radius:2px;border:1px solid #ccc;"></span> 卡片與彈窗純白背景 |

---

## 🛠️ 五、 前端工程化與建置指令

所有樣式原始碼皆在 `csp/csp-portal-web/frontend/src/cornelius-ui/` 模組化維護：

```bash
# 進入目錄
cd csp/csp-portal-web/frontend

# 安裝相依
npm install

# 啟動 UI 手冊即時調校伺服器 (http://localhost:5173/)
npm run dev

# 生產打包編譯（自動輸出至 static/dist/css/cornelius-ui.css 與 js/）
npm run build
```

---

## 📂 六、 目錄架構對照

```text
csp/csp-portal-web/
├── frontend/                                   # 前端工程化工作區 (Cornelius UI 手冊)
│   ├── index.html                              # 👉 Cornelius UI Developer 線上手冊 (http://localhost:5173/)
│   ├── package.json                            # npm run dev / build 指令
│   ├── vite.config.js                          # Vite 配置與後端代理
│   └── src/
│       ├── cornelius-ui/                       # CorneliusUI 模組化原始碼
│       │   ├── tokens.css                      # 顏色、圓角、陰影 Token
│       │   ├── typography.css                  # 字體與等寬數字
│       │   ├── components/                     # 各元件 CSS (buttons, forms, dialogs, cards...)
│       │   ├── index.css                       # 單一進入點 Bundle
│       │   └── index.js                        # 彈窗控制等輔助 JS
│       └── main.js                             # 手冊邏輯與複製按鈕 Helper
└── src/main/resources/static/dist/             # Vite 自動打包產物目錄 (供後端直接引用)
```

---
*本手冊由 鴻方移動智能科技 · CSP 架構小組維護。如有新增 UI 元件需求，請更新 `frontend/index.html` 手冊與對應 CSS 模組。*
