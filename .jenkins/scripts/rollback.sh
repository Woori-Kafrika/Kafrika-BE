#!/bin/bash

# 롤백 스크립트
echo "Starting rollback..."

# 애플리케이션 중지
echo "Stopping application..."
pkill -f "demo-0.0.1-SNAPSHOT.jar" || true

# 최신 백업 찾기
BACKUP_FILE=$(ls -t demo-0.0.1-SNAPSHOT.jar.backup.* 2>/dev/null | head -1)

if [ -n "$BACKUP_FILE" ]; then
    echo "Found backup: $BACKUP_FILE"
    
    # 현재 버전 백업
    if [ -f "demo-0.0.1-SNAPSHOT.jar" ]; then
        mv demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar.failed.$(date +%Y%m%d_%H%M%S)
    fi
    
    # 백업에서 복원
    cp "$BACKUP_FILE" demo-0.0.1-SNAPSHOT.jar
    
    # 애플리케이션 시작
    echo "Starting application with rollback version..."
    nohup java -jar demo-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
    
    echo "Rollback completed!"
else
    echo "No backup found for rollback"
    exit 1
fi 