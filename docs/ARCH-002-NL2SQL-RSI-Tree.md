# ARCH-002: NL2SQL Recursive Self-Improvement (RSI) 決策樹

基於我們對於「編譯器自舉 (Bootstrapping)」與 AI 模型進化的討論，這份文件以 **RSI (Recursive Self-Improvement, 遞迴自我完善)** 的框架，將我們針對 NL2SQL 開發過程中的決策、待決策事項，梳理成一棵決策樹。

這有助於我們在未來收斂開發方向，並明確定義 Bootstrapping 的下一個具體行動。

## RSI 決策與發展樹 (Mermaid 視覺化)

```mermaid
mindmap
  root((NL2SQL 
  RSI 系統))
    (第一階段：V1 基礎建構)
      [決定] 人工定義基準語料
      [決定] 初版 LSTM / LLM 訓練
      [決定] 遠端後端部署 (確保 Schema 安全)
    (第二階段：Bootstrapping 自舉)
      ((預設路徑))
        [決策] 生成合成語料
        [待定] 語料的過濾與品質檢核機制
      ((進階路徑))
        [待定] 語意空間對齊 (Semantic Alignment)
        [待定] 測試案例自動生成
        [待定] AST/規則輔助 (Rule-based 結合)
    (第三階段：閉環與收尾)
      [決定] 語意收斂 (Semantic Convergence)
      [待定] 評估收斂的量化指標 (如 BLEU, Exact Match)
      [待定] 何時觸發下一代模型訓練
```

## 詳細決策狀態說明

### 🟢 已決定 (Decided)
1. **遠端執行架構**：為了保護資料庫 Schema 資安，以及因應模型運算量，`executeCompile` 必須在後端執行，而非客戶端。
2. **語意收斂作為閉環**：放棄傳統編譯器的二進位不動點，改採「語意收斂」與「效能天花板」作為模型自舉停止的判定標準。
3. **合成語料為預設自舉手段**：使用 V1 產生語料來訓練 V2，這被定調為預設 (Default) 的進化路徑。

### 🟡 待討論 / 未決定 (Undecided)
1. **Bootstrapping 的具體產出物**：
   * 究竟只是單純產生「訓練語料 (Training Corpora)」？
   * 還是要產生「測試案例 (Test Cases)」？
   * 還是要在 Embedding 空間中進行「語意邊界」的微調？
2. **品質檢核機制**：V1 產生的語料如果帶有幻覺 (Hallucination)，如何避免 V2 吃到毒藥資料而導致模型崩潰 (Model Collapse)？
3. **量化閉環指標**：雖然我們知道要達到「語意收斂」，但具體要用什麼數學指標（例如生成 SQL 的執行成功率、語意相似度等）來告訴系統「我們已經收尾了」？

---
*註：本樹狀圖將隨著 Madaga CSP 專案的推進，持續更新節點狀態。*
