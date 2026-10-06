# ADR-001: CorneliusUI Design System 核心架構與微交互決策

## 1. 決策背景 (Context)
Madaga CSP 平台需要一套統一且高質感的企業級設計系統 (Design System) —— **CorneliusUI**。雖然專案目前主要基於 Vue 3 與 Quasar 框架，但考量到未來的跨平台與跨框架戰略（例如：可能導入 React、手機端 Capacitor/Ionic WebView、甚至支援 Apple VisionOS 等 WebXR 設備），我們必須確立 UI 元件的技術選型與視覺互動哲學，避免陷入「被單一框架綁架」或「過度元件化」的技術債。

## 2. 核心架構決策 (Decision)

### 2.1 採用 Web Components 作為互動元件標準
為了保證 UI 資產的「Write once, run everywhere」，我們決議：
* **複雜與高互動性元件 (如 Button, Select, Modal)**：全面擁抱 **Web Components (Custom Elements)** 規範。
* **實作方式**：在當前環境中，優先使用 Vue 3 內建的 `.ce.vue` (Custom Element) 進行開發，並註冊為瀏覽器原生標籤（如 `<cornelius-button>`）。
* **優勢**：透過 Shadow DOM 實現了 100% 的 CSS 隔離，元件樣式不會污染全局，也不會被全局覆寫；未來可無痛遷移至 React 等任何支援 W3C 標準的開發環境。

### 2.2 保持排版標籤的語意化 (純 CSS 驅動)
* **純文字與排版 (如 Label, Title, Text)**：強烈避免將靜態文字過度元件化（不建立 `<cornelius-heading>`）。
* **實作方式**：文字排版統一由全局 CSS (例如 `typography.css` 搭配 `var(--cui-font-base)`) 接管，開發時必須直接使用標準 HTML 標籤（如 `<h1>`）。
* **優勢**：獲得最佳的 SEO (搜尋引擎最佳化) 與無障礙訪問性 (A11y)，並保持 HTML 結構的簡潔與語意化。

## 3. 視覺與微交互哲學 (Visual & Micro-interaction Philosophy)

為了彰顯 Madaga CSP 作為企業級數據平台的**沉穩、可靠與專業**，我們在微交互 (Micro-interactions) 上，決定**捨棄 Material Design 的炫技特效，轉而擁抱 Apple HIG 的實體反饋哲學**。

### 3.1 捨棄水波紋，強調光影 (Hover 狀態)
* 捨棄 Material Design 預設的強烈幾何位移（如 Hover 時大幅增加陰影高度、或強制位移）。
* **實作方案**：透過 `::before` 偽元素疊加 `currentColor`，並給予極低的透明度（如 `opacity: 0.12`）。讓元件在 Hover 時，看起來像是被自然光照亮，達到柔和、均勻的變淡/變亮效果，同時維持陰影層級 (Elevation) 的穩定。

### 3.2 實體化的按壓反饋 (Active 狀態)
* 捨棄追蹤滑鼠 X/Y 座標的漣漪特效 (Ripple Effect)。
* **實作方案**：
  1. **瞬間反饋**：點擊瞬間取消過渡動畫 (`transition: none`)，確保觸覺反饋零遲滯。
  2. **整體形變與明暗**：點擊時整顆元件微縮（如 `transform: scale(0.96)`），並同步降低亮度（如 `filter: brightness(0.85)`）。
  3. **柔和回彈**：釋放滑鼠時恢復標準的 0.22s 漸變，營造真實世界實體按鈕的彈簧回饋感。
