#!/bin/bash

# Ollama 自動啟動與模型預熱腳本
# 適用於 macOS (Darwin) 環境

MODEL_NAME="qwen2.5-coder:7b"
OLLAMA_PORT=11434

echo "------------------------------------------"
echo "🔍 檢查 Ollama 服務狀態..."

# 1. 檢查 Ollama 是否正在執行 (透過埠號 11434)
if ! lsof -i:$OLLAMA_PORT > /dev/null; then
    echo "🔴 Ollama 服務未啟動，正在嘗試啟動應用程式..."
    
    # 嘗試透過 macOS 應用程式路徑啟動
    open -a Ollama
    
    # 等待服務就緒 (最多等待 30 秒)
    echo "⏳ 等待 Ollama 初始化 (預計 5-10 秒)..."
    COUNTER=0
    until curl -s http://localhost:$OLLAMA_PORT > /dev/null || [ $COUNTER -eq 30 ]; do
        sleep 1
        ((COUNTER++))
        echo -n "."
    done
    echo ""

    if [ $COUNTER -eq 30 ]; then
        echo "❌ 啟動失敗，請手動確認 Ollama 是否已安裝。"
        exit 1
    fi
    echo "✅ Ollama 服務啟動成功。"
else
    echo "🟢 Ollama 服務已在執行中。"
fi

# 2. 預熱模型 (確保模型已載入記憶體，避免第一次呼叫時延遲)
echo "🚀 正在預熱模型: $MODEL_NAME ..."
# 傳送一個簡單的指令並捨棄輸出，僅為了觸發模型載入
ollama run $MODEL_NAME "ready" > /dev/null 2>&1

if [ $? -eq 0 ]; then
    echo "✨ 模型預熱完成！現在可以啟動 Java 專案進行測試了。"
else
    echo "⚠️ 模型預熱遇到問題，請確認已執行過 'ollama pull $MODEL_NAME'。"
fi
echo "------------------------------------------"
