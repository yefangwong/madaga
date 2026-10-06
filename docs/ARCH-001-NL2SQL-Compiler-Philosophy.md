# ARCH-001: NL2SQL 架構決策與編譯器自舉哲學 (Compiler Philosophy)

## 1. 核心探索背景
在建構 Madaga CSP 的 NL2SQL (自然語言轉 SQL) POC 時，我們針對「為何編譯動作 (`executeCompile`) 需要遠端執行而非本機執行」進行了深度的架構與編譯原理反思。本文件記錄了此次探索中關於「傳統編譯器 vs AI 編譯器」的本質差異，以及「自舉 (Bootstrapping)」概念的延伸與應用。

## 2. 傳統編譯器與 NL2SQL 的架構差異
* **傳統編譯器 (Software 1.0)**：如 `javac`。其本質為確定性 (Deterministic) 的規則轉換，體積輕巧。
  * *歷史彩蛋*：`23.4K` 的 `javac.exe` 僅為 C 語言寫的啟動器 Stub，真正的編譯器本體為 Java 自己寫成的 `tools.jar` 或 JDK Modules。由於體積與算力需求低，理應於本機執行。
* **NL2SQL AI 編譯器 (Software 2.0)**：
  1. **算力與體積瓶頸**：底層依賴 LSTM 或 LLM，模型權重動輒數 GB 起跳，且需 GPU 算力推論，無法輕易部署於客戶端瀏覽器。
  2. **資安與 Context 隔離**：正確的 SQL 產出高度依賴企業機密的 Database Schema，將編譯器置於後端，是保護資料庫結構不被外洩的必要防線。

## 3. 自舉 (Bootstrapping) 與「塔式起重機」隱喻
在編譯器設計（如《龍書》理論）中，「自舉」是指用舊版編譯器編譯出新版編譯器。我們在此提出了一個極度精準的跨領域隱喻：**塔式起重機 (Tower Crane)**。
* **起重機底座 (V1 編譯器)**：最初用其他語言 (如 C) 打造的簡陋基底。
* **液壓頂升系統 (編譯過程)**：起重機利用自身的力量把自己撐高，並塞入新的標準節 (Mast Section)。這等同於編譯器用自己編譯自己的原始碼，讓自身進化。
* **實體拆塔 vs 不動點閉環**：實體起重機的收尾是依序拆除；而傳統編譯器的收尾閉環是 **不動點驗證 (Fixed-Point Verification)**——當 V2 編譯出的執行檔與 V2 再次編譯出的執行檔，達到「二進位 (Bit-for-bit) 100% 相同」時，即證明編譯器已穩定且完美閉環。

## 4. AI 神經網路作為「模糊編譯器」的收尾
針對我們自行訓練的 LSTM (能精準理解並生成「鮭魚生魚片」)，其本質即為 **Software 2.0 的編譯器**。
* **AI 的自舉 (Model Distillation / Synthetic Data)**：使用 V1 網路生成大量的對應語料，再用這些高品質的合成語料去訓練出更強的 V2 網路。
* **AI 的收尾 (Semantic Convergence)**：由於語言的模糊性與神經網路的隨機性，AI 編譯器無法達到 Bit-for-bit 的二進位不動點。其閉環條件為 **「語意收斂 (Semantic Convergence)」** 或 **「效能天花板 (Performance Plateau)」**。當 V3 使用 V2 的資料訓練後，其在語意空間（如 Embedding 的叢集距離）不再進步，甚至退化 (Model Collapse) 時，即代表該模型架構已達到自我學習的閉環極限。
