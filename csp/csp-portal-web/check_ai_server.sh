#!/bin/bash

# 遠端 AI 推理伺服器 (LM Studio) 連通性與模型檢測腳本
# 伺服器位址: 192.168.137.1:1234

REMOTE_IP="192.168.137.1"
REMOTE_PORT="1234"
REMOTE_URL="http://$REMOTE_IP:$REMOTE_PORT/v1/models"

echo "------------------------------------------"
echo "🔍 正在檢查遠端 AI 推理伺服器 ($REMOTE_IP:$REMOTE_PORT)..."

# 1. 檢查遠端伺服器連通性
RESPONSE=$(curl -s --connect-timeout 5 $REMOTE_URL)

if [ $? -eq 0 ]; then
    echo "🟢 遠端伺服器連通成功！"
    
    # 2. 從回傳的 JSON 中提取模型 ID (使用 sed/grep 簡單解析)
    # LM Studio 通常返回形式: {"data":[{"id":"qwen-2.5-coder-7b-instruct","object":"model",...}]}
    MODEL_ID=$(echo $RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4 | head -n 1)

    if [ -n "$MODEL_ID" ]; then
        echo "✅ 目前遠端已載入模型 ID: $MODEL_ID"
        echo "💡 請確保 Java 後端 (SseServiceImpl.java) 使用的名稱與此 ID 匹配。"
    else
        echo "⚠️  伺服器已回應但未找到已載入的模型，請確認 LM Studio 已 Load 模型。"
    fi
    
    echo "✨ 伺服器已就緒，現在可以啟動 Java 專案進行測試了。"
else
    echo "🔴 無法連線至遠端伺服器 ($REMOTE_IP:$REMOTE_PORT)。"
    echo "⚠️  請檢查以下項目："
    echo "   1. 遠端電腦的 LM Studio 是否已開啟並點擊 'Start Server'？"
    echo "   2. 是否已在 LM Studio 的 'Local Server' 分頁開啟 'CORS'？"
    echo "   3. 確認網路線或 Wi-Fi 連線是否正常。"
    exit 1
fi
echo "------------------------------------------"
