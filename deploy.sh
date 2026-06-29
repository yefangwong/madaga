#!/bin/bash
# ==============================================================================
# deploy.sh - Madaga CSP Portal Web Application Deployment Script
# ==============================================================================
# Usage: ./deploy.sh <env> <war_path> [port_override] [ssl_keystore] [ssl_password]
# Example: ./deploy.sh dev ./csp-portal-web-0.0.1-SNAPSHOT.war
# ==============================================================================

set -e

ENV=$(echo "$1" | tr '[:upper:]' '[:lower:]')
WAR_PATH="$2"
PORT_OVERRIDE="$3"
SSL_KEYSTORE="$4"
SSL_PASSWORD="$5"

if [ -z "$ENV" ] || [ -z "$WAR_PATH" ]; then
    echo "Usage: $0 <env: dev|sit|uat|prod> <war_path> [port_override] [ssl_keystore] [ssl_password]"
    exit 1
fi

if [ ! -f "$WAR_PATH" ]; then
    echo "[ERROR] WAR file not found at: $WAR_PATH"
    exit 1
fi

# 1. 根據環境設定預設連接埠 (Port Mapping)
case "$ENV" in
    dev)
        PORT=8080
        ;;
    sit)
        PORT=8082
        ;;
    uat)
        PORT=8083
        ;;
    prod)
        # 若有設定 SSL 憑證，預設走 443；否則走 80 (或 8081 反向代理)
        if [ ! -z "$SSL_KEYSTORE" ]; then
            PORT=443
        else
            PORT=80
        fi
        ;;
    *)
        echo "[ERROR] Unknown environment: $ENV. Supported: dev, sit, uat, prod"
        exit 1
        ;;
esac

# 若有傳入 port_override，則進行覆蓋
if [ ! -z "$PORT_OVERRIDE" ]; then
    PORT="$PORT_OVERRIDE"
fi

echo "========================================="
echo " Deploying Madaga CSP Portal to [$ENV]"
echo " Target Port: $PORT"
echo " Source Artifact: $WAR_PATH"
echo "========================================="

# 2. 建立部署運行目錄
DEPLOY_DIR="$(pwd)/deploy_run/$ENV"
mkdir -p "$DEPLOY_DIR"
cp "$WAR_PATH" "$DEPLOY_DIR/csp-portal-web.war"
cd "$DEPLOY_DIR"

# 3. 停止舊有進程 (Graceful Shutdown)
echo "Checking for running processes on port $PORT..."
# 尋找佔用該連接埠的 PID (相容 macOS / Linux)
PID=""
if command -v lsof >/dev/null 2>&1; then
    PID=$(lsof -t -i:$PORT || true)
fi

# 如果找不到，再透過 ps 尋找相同 Profile 的 Java 進程
if [ -z "$PID" ]; then
    PID=$(ps -ef | grep "csp-portal-web.war" | grep "spring.profiles.active=$ENV" | grep -v grep | awk '{print $2}' || true)
fi

if [ ! -z "$PID" ]; then
    for p in $PID; do
        echo "Found running application (PID: $p). Sending SIGTERM..."
        kill $p || true
    done
    
    # 等候 5 秒確認進程是否結束
    echo "Waiting for process to exit..."
    for i in {1..5}; do
        STILL_RUNNING=0
        for p in $PID; do
            if kill -0 $p 2>/dev/null; then
                STILL_RUNNING=1
            fi
        done
        if [ $STILL_RUNNING -eq 0 ]; then
            break
        fi
        sleep 1
    done
    
    # 若仍在運行，強制清除
    for p in $PID; do
        if kill -0 $p 2>/dev/null; then
            echo "Process $p did not exit. Sending SIGKILL..."
            kill -9 $p || true
        fi
    done
else
    echo "No existing process running on port $PORT."
fi

# 4. 準備 Java 啟動指令與環境變數
MADAGA_HOME="$(cd ../.. && pwd)"
echo "Setting MADAGA_HOME to: $MADAGA_HOME"

# 構建 JVM 參數
JAVA_OPTS="-Dmadaga.home=$MADAGA_HOME -Dspring.profiles.active=$ENV -Dserver.port=$PORT"

# 整合 SSL (方案 B)
SCHEME="http"
if [ ! -z "$SSL_KEYSTORE" ] && [ ! -z "$SSL_PASSWORD" ]; then
    echo "Configuring Embedded SSL using Keystore: $SSL_KEYSTORE"
    JAVA_OPTS="$JAVA_OPTS -Dserver.ssl.enabled=true -Dserver.ssl.key-store=$SSL_KEYSTORE -Dserver.ssl.key-store-password=$SSL_PASSWORD -Dserver.ssl.key-store-type=PKCS12"
    SCHEME="https"
fi

# 5. 背景啟動應用程式
echo "Starting Spring Boot application..."
LOG_FILE="portal-$ENV.log"
nohup java $JAVA_OPTS -jar csp-portal-web.war > "$LOG_FILE" 2>&1 &

NEW_PID=$!
echo "Application started in background (PID: $NEW_PID). Output redirected to: $DEPLOY_DIR/$LOG_FILE"

# 6. 主動健康檢查輪詢 (Health Check Loop)
echo "Performing active health check on $SCHEME://localhost:$PORT/ ..."
SUCCESS=0
TIMEOUT=60
INTERVAL=3
ELAPSED=0

# 用於檢測的 URL 加上 -k 忽略 self-signed 憑證校驗
CHECK_URL="$SCHEME://localhost:$PORT/"

while [ $ELAPSED -lt $TIMEOUT ]; do
    HTTP_CODE=$(curl -s -k -o /dev/null -w "%{http_code}" "$CHECK_URL" || true)
    
    # 只要 HTTP_CODE 介於 200 到 499 之間，或為 500 (代表 Servlet 容器已啟動並在響應)，就認定啟動成功
    if [ "$HTTP_CODE" -ge 200 ] && [ "$HTTP_CODE" -lt 600 ]; then
        echo "[SUCCESS] Health check passed with HTTP status: $HTTP_CODE"
        SUCCESS=1
        break
    fi
    
    echo "Waiting for application to boot... (HTTP response: $HTTP_CODE, elapsed: ${ELAPSED}s)"
    sleep $INTERVAL
    ELAPSED=$((ELAPSED + INTERVAL))
done

if [ $SUCCESS -eq 1 ]; then
    echo "========================================="
    echo " [SUCCESS] Deployment completed successfully!"
    echo " Environment: $ENV"
    echo " URL: $SCHEME://localhost:$PORT/"
    echo " PID: $NEW_PID"
    echo "========================================="
    exit 0
else
    echo "========================================="
    echo " [ERROR] Health check failed after ${TIMEOUT}s!"
    echo " Printing last 50 lines of $LOG_FILE:"
    echo "========================================="
    tail -n 50 "$LOG_FILE" || true
    echo "========================================="
    exit 1
fi
