#!/bin/bash

# 테스트용 배포 스크립트
DEPLOY_PATH="/tmp/kafrika-backend-test"
APP_NAME="demo-0.0.1-SNAPSHOT.jar"

echo "Starting deployment test..."

# 애플리케이션 중지
echo "Stopping application..."
pkill -f "$APP_NAME" || true

# 백업 생성
echo "Creating backup..."
if [ -f "$DEPLOY_PATH/$APP_NAME" ]; then
    cp "$DEPLOY_PATH/$APP_NAME" "$DEPLOY_PATH/$APP_NAME.backup.$(date +%Y%m%d_%H%M%S)"
fi

# 새 버전 복사
echo "Copying new version..."
cp "target/$APP_NAME" "$DEPLOY_PATH/"

# 애플리케이션 시작
echo "Starting application..."
cd "$DEPLOY_PATH"
nohup java -jar "$APP_NAME" > app.log 2>&1 &
echo $! > app.pid

echo "Deployment test completed!"
echo "Application PID: $(cat app.pid)"
echo "Log file: $DEPLOY_PATH/app.log" 