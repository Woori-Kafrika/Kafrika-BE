#!/bin/bash

# 헬스 체크 스크립트
echo "Performing health check..."

# 애플리케이션이 실행 중인지 확인
if pgrep -f "demo-0.0.1-SNAPSHOT.jar" > /dev/null; then
    echo "Application is running"
    
    # HTTP 헬스 체크 (포트 8080 가정)
    if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
        echo "Health check passed"
        exit 0
    else
        echo "Health check failed - HTTP endpoint not responding"
        exit 1
    fi
else
    echo "Application is not running"
    exit 1
fi 