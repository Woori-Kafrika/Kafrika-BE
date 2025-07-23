# Jenkins 연동 가이드

## 개요

이 프로젝트는 Jenkins를 통한 CI/CD 파이프라인을 구성하여 자동화된 빌드, 테스트, 배포를 지원합니다.

## 파일 구조

```
.jenkins/
├── scripts/
│   ├── deploy.sh      # 배포 스크립트
│   ├── health-check.sh # 헬스 체크 스크립트
│   └── rollback.sh    # 롤백 스크립트
└── Jenkinsfile.deploy # 배포 전용 파이프라인

Jenkinsfile              # 기본 CI/CD 파이프라인
README-Jenkins.md        # 이 파일
```

## Jenkins 설정

### 1. Jenkins 설치 및 플러그인

- Jenkins 2.387.3 이상 설치
- 필요한 플러그인:
  - Git plugin
  - Pipeline plugin
  - Maven Integration plugin
  - JUnit plugin
  - Workspace Cleanup plugin

### 2. 도구 설정

Jenkins 관리 > Global Tool Configuration에서 다음 도구들을 설정:

#### JDK 17

- Name: `JDK 17`
- JAVA_HOME: `/usr/lib/jvm/java-17-openjdk` (또는 실제 JDK 경로)

#### Maven 3.9.5

- Name: `Maven 3.9.5`
- MAVEN_HOME: `/usr/share/maven` (또는 실제 Maven 경로)

### 3. Jenkins Job 생성

#### 기본 CI/CD Job

1. 새로운 Pipeline job 생성
2. Pipeline 정의에서 "Pipeline script from SCM" 선택
3. SCM을 Git으로 설정
4. Repository URL 설정
5. Script Path를 `Jenkinsfile`로 설정

#### 배포 Job

1. 새로운 Pipeline job 생성 (예: `kafrika-backend-deploy`)
2. Pipeline 정의에서 "Pipeline script from SCM" 선택
3. SCM을 Git으로 설정
4. Repository URL 설정
5. Script Path를 `.jenkins/Jenkinsfile.deploy`로 설정

## 파이프라인 단계

### 기본 파이프라인 (Jenkinsfile)

1. **Checkout**: 소스 코드 체크아웃
2. **Build**: Maven을 사용한 컴파일
3. **Test**: 단위 테스트 실행 및 결과 수집
4. **Package**: JAR 파일 생성
5. **Archive**: 빌드 아티팩트 보관

### 배포 파이프라인 (.jenkins/Jenkinsfile.deploy)

1. **Checkout**: 소스 코드 체크아웃
2. **Build**: Maven을 사용한 패키징
3. **Deploy**: 애플리케이션 배포
4. **Start Application**: 애플리케이션 시작
5. **Health Check**: 헬스 체크 수행

## 환경 변수 설정

### 배포 환경 변수

- `DEPLOY_PATH`: 애플리케이션 배포 경로 (기본값: `/opt/kafrika-backend`)
- `APP_NAME`: 애플리케이션 JAR 파일명 (기본값: `demo-0.0.1-SNAPSHOT.jar`)

## 스크립트 사용법

### 배포 스크립트

```bash
chmod +x .jenkins/scripts/deploy.sh
./jenkins/scripts/deploy.sh
```

### 헬스 체크 스크립트

```bash
chmod +x .jenkins/scripts/health-check.sh
./jenkins/scripts/health-check.sh
```

### 롤백 스크립트

```bash
chmod +x .jenkins/scripts/rollback.sh
./jenkins/scripts/rollback.sh
```

## 모니터링

### 로그 확인

```bash
# 애플리케이션 로그
tail -f /opt/kafrika-backend/app.log

# Jenkins 빌드 로그
# Jenkins 웹 인터페이스에서 확인
```

### 헬스 체크 엔드포인트

애플리케이션이 정상적으로 실행되면 다음 엔드포인트로 헬스 체크 가능:

- `http://localhost:8080/actuator/health`

## 문제 해결

### 빌드 실패 시

1. Jenkins 빌드 로그 확인
2. Maven 의존성 문제 확인
3. Java 버전 호환성 확인

### 배포 실패 시

1. 배포 스크립트 로그 확인
2. 애플리케이션 로그 확인
3. 포트 충돌 확인
4. 권한 문제 확인

### 롤백

배포 실패 시 자동으로 이전 버전으로 롤백됩니다.

## 보안 고려사항

- Jenkins 서버 접근 제한
- 배포 서버 접근 권한 관리
- 민감한 정보는 Jenkins Credentials 사용
- 정기적인 로그 정리

## 추가 설정

### Slack 알림 설정

Jenkins에서 Slack 플러그인을 설치하고 빌드 결과를 Slack으로 알림을 받을 수 있습니다.

### Docker 컨테이너 배포

필요시 Docker 이미지 빌드 및 배포로 확장 가능합니다.
