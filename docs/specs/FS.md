# 📐 Functional Specification (FS) Master Index - Cornelius CSP

> **需求依據**：[[docs/BRD.md|Cornelius CSP 商業需求池 (BRD)]]  
> **規範標準**：[[facts/fs_naming_and_modularization_convention.md|模組化 FS 規範]] (FS_S{泳道}_N{節點}_{語意}.md)  
> **所屬專案**：`madaga / csp / csp-portal-web`  
> **最後更新日期**：2026-10-03  

---

## 🗺️ 模組化規格索引 (Modular Specifications Index)

依據 BPMN 泳道與節點劃分，規格已拆解為獨立功能模組規格檔：

| 規格代號 (Spec Code) | 泳道與節點 (Lane & Node) | 規格主題與連結 | 對應 BRD 需求 ID |
| :--- | :--- | :--- | :--- |
| `FS_S1_N01` | 泳道 1 (搜尋與診斷), 節點 01 | [[docs/specs/FS_S1_N01_embedded_lucene_search.md|嵌入式 Lucene 9 技術手冊檢索]] | `BRD-CSP-SRCH-001` |
| `FS_S2_N02` | 泳道 2 (資料與白名單), 節點 02 | [[docs/specs/FS_S2_N02_textual_erd_whitelist.md|多資料庫連線探針與文字化 ERD 白名單]] | `BRD-CSP-SRCH-002` |
| `FS_S2_N03` | 泳道 2 (資料與白名單), 節點 03 | [[docs/specs/FS_S2_N03_ai_guided_shuttle_wizard.md|AI 引導式穿梭框報表精靈與確定性 RPA 拋轉]] | `BRD-CSP-SRCH-002`, `BRD-CSP-SEC-005` |
| `FS_S1_N03` | 泳道 1 (搜尋與診斷), 節點 03 | [[docs/specs/FS_S1_N03_semantic_canvas_studio.md|私域術語定義與語意引力畫布]] | `BRD-CSP-SRCH-003` |

---

## 🛡️ 實作標準與架構原則
1. **去 Spring 污染**：所有業務邏輯單元 (`DocSearchBL`, `TextualErdBL`) 繼承純 Java `BaseBL<REQ, RESP>`。
2. **開源潔淨室命名**：統一採用 `TxSubmitter` 與 `UnitOfWork`。
3. **老碼農測試門禁**：Line Coverage $\ge 80\%$, Branch Coverage $\ge 75\%$, P3C 零違規。
