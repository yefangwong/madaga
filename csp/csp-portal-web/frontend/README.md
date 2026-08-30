# 🎨 Cornelius UI Developer 手冊 (Frontend Playground)

> **位置**：`csp/csp-portal-web/frontend/`  
> **線上手冊網址**：啟動後造訪 [`http://localhost:5173/`](http://localhost:5173/)  
> **完整開發文檔**：請參閱 [CORNELIUS_UI_DEVELOPER_MANUAL.md](file:///Users/yefangwong/Documents/GitHub.nosync/madaga/csp/CORNELIUS_UI_DEVELOPER_MANUAL.md)

---

## 🚀 快速啟動 UI Developer 手冊

```bash
# 1. 安裝相依套件
npm install

# 2. 啟動手冊即時調校伺服器
npm run dev
```

啟動後打開瀏覽器訪問：👉 **[`http://localhost:5173/`](http://localhost:5173/)**

---

## 💡 如何使用？

1. **挑選元件**：在網頁上瀏覽文字輸入框、下拉選單、按鈕、狀態標籤、表格、彈跳視窗等。
2. **複製程式碼**：點擊每個元件右上角的 **「複製程式碼」** 按鈕。
3. **貼到專案**：直接貼上至 Thymeleaf HTML 頁面即可運行。

---

## 📦 打包輸出給後端 Spring Boot

```bash
npm run build
```
產物將自動輸出至 `../src/main/resources/static/dist/`：
* `css/cornelius-ui.css` (包含所有樣式的生產 Bundle)
* `js/cornelius-ui.js` (UI 輔助控制函式庫)
