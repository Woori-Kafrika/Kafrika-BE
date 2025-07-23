#!/bin/bash

# 배포 스크립트
echo "Starting deployment..."

# 애플리케이션 중지
echo "Stopping application..."
pkill -f "demo-0.0.1-SNAPSHOT.jar" || true

# 백업 생성
echo "Creating backup..."
if [ -f "demo-0.0.1-SNAPSHOT.jar" ]; then
    cp demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar.backup.$(date +%Y%m%d_%H%M%S)
fi

# 새 버전 복사
echo "Copying new version..."
cp target/demo-0.0.1-SNAPSHOT.jar .

# 애플리케이션 시작
echo "Starting application..."
nohup java -jar demo-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

echo "Deployment completed!" 