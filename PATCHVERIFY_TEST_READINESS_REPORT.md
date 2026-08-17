# 🛡️ PatchVerify 專案實測覆蓋率與 Readiness 發起門禁評估報告 (Test Readiness Report)

> **專案名稱**：CSP Madaga / PatchVerify 試點庫 (`csp-portal-web`)  
> **評估日期**：2026-08-17  
> **評估工具**：Jacoco Maven Plugin v0.8.12 / JUnit 5  
> **資安與品質規範**：ISO 27001 A.12.6 / A.8.28, PatchVerify FS_S1_N02 / FS_S2_N01 規範  

---

## 📊 1. `csp-portal-web` 實體 Jacoco 覆蓋率測量結果

### 1.1 全模組整體覆蓋率數據
- **Instruction Coverage (指令覆蓋率)**：**50%** ($764 / 1,515$ 指令)
- **Branch Coverage (分支覆蓋率)**：**41%** ($41 / 98$ 分支)
- **Line Coverage (行覆蓋率)**：**48%** ($180 / 375$ 行)
- **Method Coverage (方法覆蓋率)**：**59%** ($58 / 98$ 方法)

### 1.2 包層級 (Package Level) 數據細節

| Package 類別 | 指令覆蓋率 (%) | 分支覆蓋率 (%) | 未覆蓋指令數 | 未覆蓋行數 | 現況評級與風險 |
| :--- | :---: | :---: | :---: | :---: | :--- |
| `com.hongfang.csp.portal.controller` | **9%** | **0%** | 73 | 81 | 🔴 高風險 (極缺乏測試) |
| `com.dhf.hrsys.controller` | **24%** | **11%** | 32 | 106 | 🔴 高風險 (業務邏輯暴露) |
| `com.dhf.energy` | **8%** | N/A | 8 | 9 | 🔴 高風險 |
| `com.hongfang.csp.portal.controller.dashboard` | **60%** | N/A | 1 | 2 | 🟡 中度受測 |
| `com.dhf.util` | **83%** | **65%** | 15 | 61 | 🟢 優良 |
| `com.dhf.hrsys.interceptor` | **86%** | **66%** | 8 | 31 | 🟢 優良 |
| `com.hongfang.csp.portal.security` | **91%** | **57%** | 1 | 19 | 🟢 優良 |
| `com.hongfang.csp.portal` (主程序) | **93%** | **100%** | 2 | 21 | 🟢 優良 |
| `com.hongfang.csp.portal.config` | **81%** | **0%** | 13 | 43 | 🟢 優良 |
| `com.hongfang.csp.portal.controller.auth` | **100%** | N/A | 0 | 2 | 🟢 完全覆蓋 |

---

## ⚖️ 2. PatchVerify 系統門禁審查判決 (Readiness Gate Evaluation)

### 判決結果：【拒接全自動修補 (REJECTED / TIER_3_BLOCKED)】

#### 2.1 判決理據 (Evaluation Rationale)
依據 PatchVerify `FS_S1_N02` 發起與評定邏輯：
1. **Tier 1 (全自動高信任 PR 發起門檻)**：需求 **Line $\ge 80\%$ 且 Branch $\ge 75\%$**。
   - 實測 Line 48% / Branch 41% ➔ **未達標**。
2. **Tier 2 (人工審查降級發起門檻)**：需求 **Line $\ge 60\%$ 且 Branch $\ge 50\%$**。
   - 實測 Line 48% / Branch 41% ➔ **未達標**。
3. **Tier 3 (系統主動阻斷拒接)**：Line $< 60\%$ 或 Branch $< 50\%$。
   - 判定結果：**Tier 3 阻斷降級**。

#### 2.2 資安與迴歸風險分析 (Regression Risk Assessment)
- 專案存在 **52% 的程式碼行數** 與 **59% 的邏輯分支** 處於無測試狀態（特別是核心 Controller 層 `portal.controller` 覆蓋率僅 9%）。
- 若 PatchVerify 自動進行依賴躍升或 code 級別修補，即使 Docker 沙箱執行 `mvn test` 通過，亦無法保證剩餘 52% 未受測試的業務邏輯不會發生破壞性迴歸 (Regression Error)。

---

## 🛠️ 3. 給 PatchVerify 知識庫與 Agent 的後續開發處方 (Actionable Recommendations)

1. **爆破半徑精準補強 (Surgical Coverage Enhancement)**：
   - 針對 CVE 受災依賴（如 Tomcat Embed, Jackson, Axios）所呼叫的 2-Hop 類別（`portal.controller` 與 `hrsys.controller`），補撰單元測試案例。
   - 只要受災 Blast Radius 區域之 Line 達 80% / Branch 達 75%，即可觸發 PatchVerify **Tier 1 Local Scope 放行**。
2. **切換至 Human-in-the-Loop 人工簽核模式**：
   - 在測試覆蓋率提升至 Tier 2 期間，PatchVerify 以 `HUMAN_REVIEW_REQUIRED` 模式運作，所有產出的修補分支需經由工程師在 Web UI 簽核後方可進行 Merge。
