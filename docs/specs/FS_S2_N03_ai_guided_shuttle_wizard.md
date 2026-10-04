# 📋 功能規格文件 (Functional Specification - FS)
## 模組編號：FS_S2_N03 (Swimlane 2 - Task 3)
## 功能名稱：AI 引導式穿梭框報表精靈與確定性 RPA 拋轉動線 (AI-Guided Shuttle Wizard & Deterministic RPA Pipeline)

> **版本**：v1.0.0  
> **制定日期**：2026-10-04  
> **關聯需求**：[[Projects/startup/madaga/BRD.md|BRD-CSP-SRCH-002]], [[Projects/startup/madaga/BRD.md|BRD-CSP-SEC-005]]  
> **核心哲學**：**「AI 是隨機的，而 RPA 與企業核心流程是固定的。用隨機的工具箱解確定的問題，中間必須有一套畫面讓 Human-in-the-Loop 進行錨定與校準。」**  
> **脫敏規範**：完全脫敏，遵循 Lisbon / 潔淨室原則，不保留任何特定企業真實名稱或特定業務機敏代號。

---

## 📌 一、 業務場景與架構痛點 (Context & Problem Statement)

### 1.1 業界痛點：端到端 Text-to-SQL 的「隨機性災難」
當前業界常見的 Text-to-SQL 嘗試讓 LLM 一步登天（One-shot），試圖在黑箱中一次生成完整 SQL 並直接驅動後續自動化。然而：
1. **機率漂移 (Probabilistic Drift)**：大模型受溫度與採樣影響，相同提示詞生成的 SQL 每次可能略有不同（多 Join 一張表、欄位名稱漂移、WHERE 條件漏抓）。
2. **阻抗不匹配 (Impedance Mismatch)**：後續對接的 RPA（機器人流程自動化）或 ERP 核心系統需要 100% 確定、型別固定、格式絕對剛性的資料輸入。一旦隨機產生的 SQL 出錯，RPA 機器人將直接崩潰甚至批次寫入髒資料。
3. **可稽核性缺失 (Auditability Gap)**：金融與大型企業要求嚴格責任歸屬，AI 不能為產出的財務或營運報表簽名背書。

### 1.2 破局方案：AI-Assisted Guided Shuttle Wizard (人機引導穿梭框嚮導)
借鏡金融保險業實戰驗證的報表精靈思想，將「意圖理解」與「語法生成」解耦：
- **AI 僅作為「填空秘書」**：負責根據自然語言意圖，在背後自動為精靈表單進行「預填 / 預選」。
- **穿梭框與卡片作為「剛性緩衝區」**：提供經典的雙向穿梭框 (`-->` / `<--`)、下拉篩選器與聚合勾選，讓人類（經辦/業務）3 秒眼見為憑微調確認。
- **純 Java AST 確定性編譯**：經由人機確認之 JSON 結構，由確定性程式碼直接編譯為標準 SQL，保證 0 隨機性、100% 格式無誤地輸出 Excel 並拋轉 RPA。

---

## 🖥️ 二、 四步嚮導互動動線與畫面規格 (Wizard UX Flow)

```
┌────────────────────────────────────────────────────────────────────────┐
│ 頂部許願列： [ 💬 輸入自然語言需求：上個月各服務廠的工單總額與平均金額... ] [ 🪄 AI 輔助帶入 ] │
├────────────────────────────────────────────────────────────────────────┤
│ 步驟導航： (1) 選擇資料表 ➔ (2) 欄位與過濾條件 ➔ (3) 計算與分組排序 ➔ (4) 預覽與拋轉 RPA │
└────────────────────────────────────────────────────────────────────────┘
```

### 2.1 Step 1：資料表穿梭選取 (Table Shuttle)
* **目標**：從幾百張實體表中，收斂出本報表所需的 1~3 張授權表，確定實體唯一性，杜絕幻覺造表。
* **畫面元素**：
  1. **左側列表 (Available Tables)**：列出系統白名單內所有資料表，呈現「業務中文名稱（高亮標籤）」與「英文實體表名（淺灰字樣）」，附帶即時關鍵字過濾輸入框。
  2. **中繼按鈕**：`[ ➔ 加入 ]` 與 `[ ⬅ 移除 ]` 雙向穿梭按鈕。
  3. **右側列表 (Selected Tables)**：使用者選定之目標表格。
  4. **自動關聯預覽 (Join Topology Preview)**：下方依據資料庫外鍵拓撲（Foreign Keys），自動呈現表間關聯（例如：`tbl_work_order.facility_id = tbl_facility.id`），禁止使用者手寫錯誤的關聯條件。
* **AI 秘書行為**：解析自然語言後，自動將命中的實體表從左邊移入右邊，並在標籤邊框呈現「薄荷綠光暈」提示預填。

---

### 2.2 Step 2：欄位投影與謂詞過濾器 (Columns & Predicate Filters)
* **目標**：確定報表呈現之直行（Columns），並實施謂詞下推（Predicate Pushdown）過濾橫列（Rows）。
* **畫面元素**：
  1. **上半部：欄位穿梭框 (Column Shuttle)**：
     - 左側呈現 Step 1 已選表格的所有欄位清單。
     - 右側為報表輸出直行清單，**支援滑鼠垂直拖曳重新排序**（第 1 個對應 Excel A 欄，第 2 個對應 B 欄）。
  2. **下半部：結構化過濾條件卡片 (WHERE Builder)**：
     - `[ ＋ 新增過濾條件 ]`。
     - 每列條件包含：`[ 欄位下拉選單 ]` ➔ `[ 運算子下拉 (等於 / 介於 / 大於 / 包含 / 為空) ]` ➔ `[ 目標數值輸入框 / 字典下拉 ]`。
     - 若目標欄位屬於資料字典（如狀態碼、廠區代碼），**自動讀取字典快取渲染為下拉選單**，防範手打錯字。
* **AI 秘書行為**：自動將意圖涉及的欄位移入右側，並根據時間（如「上個月」）自動在下方卡片填妥日期範圍。

---

### 2.3 Step 3：運算、分組維度與排序 (Metrics, GroupBy & Sorting)
* **目標**：以零代碼、卡片式勾選，完成多維度聚合與排序定義。
* **畫面元素**：
  1. **分組維度卡片 (Group By)**：
     - 將已選的非數值欄位（如服務廠名稱、部門、月份）呈現為核取方塊 (Checkbox)，勾選即代表分組維度。
  2. **數值計算卡片 (Metrics)**：
     - 將已選的數值欄位（如金額、數量）並排呈現常見運算子核取方塊：`[ ☑ SUM ]`、`[ ☑ AVG ]`、`[ ☑ COUNT ]`、`[ ☐ MAX ]`、`[ ☐ MIN ]`。
  3. **資料排序卡片 (Order By)**：
     - 提供第 1 排序與第 2 排序之下拉選單，並支援 `[ 降序 (DESC) ]` 與 `[ 升序 (ASC) ]` 切換。

---

### 2.4 Step 4：即時預覽、確定性落地與 RPA 拋轉 (Preview & RPA Action)
* **目標**：人眼最後把關、100% 確定性簽章、無縫交棒給 RPA。
* **畫面元素**：
  1. **報表名稱欄位**：預設為 `YYYYMMDD_主題報表.xlsx`，可自由編輯。
  2. **資料網格預覽 (Data Grid Preview)**：
     - 後端自動注入 `LIMIT 10` 撈取前 10 筆真實資料。
     - 渲染現代化 Excel 表格樣式（千分位逗號靠右對齊、日期格式標準化、標題列深色底紋）。
  3. **雙重操作按鈕**：
     - `[ 📥 直接下載 Excel ]`：透過 Apache POI 流式寫入，即時下載 `.xlsx` 檔案。
     - `[ 🤖 啟動 RPA 自動化排程拋轉 ]`：將當前確定之報表 AST 結構存入範本庫，產生唯一 `TemplateID`，發送 Webhook 觸發 RPA 機器人，按排程自動取檔、填報 ERP 或寄送主管郵件。

---

## ⚙️ 三、 後端確定性 AST 資料結構 (Data Contract)

經由四步精靈確定後的資料結構為純 JSON AST，完全消除自由字串：

```json
{
  "templateName": "維修廠業績統計月報",
  "fromTables": [
    {"tableName": "tbl_work_order", "alias": "wo"},
    {"tableName": "tbl_facility", "alias": "fac"}
  ],
  "joinClauses": [
    {"left": "wo.facility_id", "op": "=", "right": "fac.id", "type": "INNER"}
  ],
  "selectedColumns": [
    {"expression": "fac.name", "alias": "服務廠名稱"},
    {"expression": "COUNT(wo.id)", "alias": "工單總筆數"},
    {"expression": "SUM(wo.amount)", "alias": "工單總金額"},
    {"expression": "AVG(wo.amount)", "alias": "平均工單金額"}
  ],
  "whereConditions": [
    {"column": "wo.end_date", "op": "BETWEEN", "values": ["2026-09-01", "2026-09-30"]},
    {"column": "wo.status", "op": "=", "values": ["COMPLETED"]}
  ],
  "groupBy": ["fac.name"],
  "orderBy": [
    {"column": "SUM(wo.amount)", "direction": "DESC"}
  ],
  "rpaConfig": {
    "enabled": true,
    "cronExpression": "0 0 8 1 * ?",
    "targetSystem": "ERP_FINANCE_MODULE"
  }
}
```

---

## 🛡️ 四、 驗收標準與門禁規範 (Acceptance Criteria)

1. **[AC-WIZ-01] 穿梭框雙向響應延遲**：使用者在前端點擊 `-->` 或 `<--` 搬遷項目時，DOM 渲染與狀態更新延遲必須 $\le 16\text{ms}$ (60 FPS)，確保絲滑無卡頓。
2. **[AC-WIZ-02] 零 SQL 注入保證**：後端 `ReportAstCompiler` 解析 JSON AST 時，所有欄位與表名必須強制校驗白名單，所有過濾值必須強制採用 JDBC `PreparedStatement` 佔位符 (`?`) 綁定，阻絕任何 SQL 注入。
3. **[AC-WIZ-03] 預覽效能門禁**：Step 4 之資料預覽請求必須強制限制 `LIMIT 10`，API 返回並渲染完成耗時必須 $\le 200\text{ms}$。
4. **[AC-WIZ-04] Excel 匯出記憶體防禦**：大量資料匯出時，後端強制採用 Apache POI `SXSSFWorkbook` (串流寫入，記憶體窗口 $\le 500$ 列)，嚴禁全量加載至記憶體導致 JVM OOM。

---
## Sources
- [[Projects/startup/madaga/BRD.md]]
- [[Projects/startup/madaga/WORKLOG_20261003.md]]
- [[docs/specs/FS.md]]
