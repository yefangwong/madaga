# Cornelius CSP Portal Web (`csp.yefangwong.net`) 待辦事項 (TODO List)

> [!NOTE]
> 本文件為 Cornelius CSP 商業門戶網站 (`csp.yefangwong.net`) 之長期規劃與輕量待辦事項。

---

## 📌 待辦事項 (Backlog & TODOs)

### 1. [輕量流量搜集] 商業入口網站基礎 GA4/GTM 埋點 (Basic Traffic Collection)
- [ ] **目標**：僅埋設極簡、零負擔的基礎流量搜集，不做過度的點擊流 (Clickstream) 管道開發。
- [ ] **原則 (Anti Over-Engineering)**：
  - **拒絕過度設計**：現階段不購買/不安裝任何昂貴分析軟體（如 Mixpanel, Amplitude, Hotjar 商業版）。
  - **不浪費研發資源**：不花時間撰寫複雜的點擊流事件資料庫與自訂監聽器。
  - **極簡指標**：僅關心兩項資料：
    1. 每日基礎不重複造訪人數 (Basic Pageview / Active Users)。
    2. 主按鈕點擊率：`Contact Sales` / `商業諮詢表單` 點擊。
- [ ] **評估觸發門檻**：
  - 只有當網站每日穩定自然造訪突破 **100 ~ 200 人**，且有持續入站諮詢需求時，始評估是否擴充進階轉化漏斗分析。

---

### 2. [系統功能] CSP Portal 其他規劃項目
- [ ] CSP 商業落地頁 (Landing Page) 視覺與響應式 (RWD) 微調。
- [ ] 企業級 JSR-264 訂單解耦架構展示專區。
- [ ] 諮詢表單發送與 Email 自動通知邏輯。
