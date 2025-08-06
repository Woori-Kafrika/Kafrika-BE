# ☕ Kafrika Backend Service

**Kafrika Backend**는 Kafka를 활용한 실시간 채팅 백엔드 시스템으로,  
**Kafka 사용 유무에 따른 시스템 성능과 부하 처리 능력 차이를 비교 분석**하기 위해 설계된 프로젝트입니다.

해당 프로젝트는 다음 두 방식으로 메시지를 처리합니다:

- 🔄 **동기 방식**:  
  클라이언트 요청 시 즉시 DB에 저장하고 WebSocket으로 브로드캐스트

- 📨 **비동기 방식 (Kafka 기반)**:  
  클라이언트 요청을 Kafka로 전송하고, Consumer가 DB 저장 및 WebSocket 브로드캐스트 처리

---

## 📦 주요 기능

✅ 실시간 WebSocket 기반 채팅 메시지 송수신 (STOMP)

✅ Kafka를 통한 메시지 브로커 연동 및 비동기 처리

✅ 채팅 로그 저장 (PostgreSQL)

✅ 예외 처리 및 공통 응답 포맷 적용

---

## 🚀 Tech Stack

| Category     | Tech Stack                              |
| ------------ | --------------------------------------- |
| Language     | Java 17                                 |
| Framework    | Spring Boot 3.5.4                       |
| Web          | Spring Web, WebSocket                   |
| ORM          | Spring Data JPA, Hibernate              |
| DB           | PostgreSQL, H2 (Test)                   |
| Messaging    | Apache Kafka 3.5                        |
| Build Tool   | Gradle (Groovy DSL)                     |
| Logging      | Logback, Spring Boot Logging            |

---

## 📁 Project Structure

```
📦 kafrika-backend
├── 📂 src
│   ├── 📂 main
│   │   ├── 📂 java/com/fisa/kafrika_backend
│   │   │   ├── 📂 controller        # REST / STOMP Controller
│   │   │   ├── 📂 service           # Business Logic
│   │   │   ├── 📂 repository        # JPA Repository
│   │   │   ├── 📂 domain            # JPA Entity
│   │   │   ├── 📂 dto               # Request/Response DTO
│   │   │   ├── 📂 config            # Kafka, WebSocket, Swagger Config
│   │   │   └── 📂 common            # Exception, Response Wrapper 등
│   │   └── 📂 resources
│   │       ├── application.yml
│   │       └── application-local.yml    # 로컬 테스트를 위한 설정 파일
│   └── 📂 test                   
└── build.gradle                   # Gradle 빌드 스크립트
```

---

## 📬 Kafka 사용 방식
  
### ✅ Producer 역할

- `ChatService.sendKafkaChatMessage()`  
  → 클라이언트로부터 수신한 채팅 메시지를 Kafka 토픽(`chat-message`)에 전송
    
- Kafka 토픽명 : `chat-message`
  
- Key : `userId`
  
- Value : `ChatMessageRequest` JSON 직렬화

### ✅ Consumer 역할

- `ChatKafkaListener.consume()`  
  → Kafka로부터 메시지를 수신하고, DB 저장 후 WebSocket(`/topic/chat`)으로 브로드캐스트
  
- JSON → `ChatMessageRequest` 역직렬화 후:
  
  - DB(`ChatMessage`) 저장
    
  - WebSocket 브로드캐스트(`/topic/chat`)

### ✅ 메시지 직렬화 포맷

- `ChatMessageRequest` 클래스 기반 JSON
```json
{
  "userId": 1,
  "message": "안녕하세요"
}
```

---

## ☸️ 운영 환경

해당 서비스는 K8s 기반의 MSA 환경에서 배포됩니다.

Kafka, PostgreSQL, ArgoCD, Jenkins, Grafana 등은 인프라 구성 저장소에서 관리됩니다.

GitOps 기반 배포를 통해 ArgoCD에서 감시 및 반영됩니다.

👉 전체 MSA 구조 및 CI/CD 흐름은 [Kafrika MSA Infra 저장소](https://github.com/Woori-Kafrika/Kafrika-Infra) 를 참고하세요.

---

## 📊 성능 실험

| 실험 항목       | 동기 방식 (No Kafka) | 비동기 방식 (Kafka 사용) |
|----------------|----------------------|--------------------------|
| 평균 응답 시간 |                |                    |
| 처리량 (TPS)    |               |                   |
| CPU 사용률      |                   |                      |
| 메시지 손실률   |                    |                       |

> 부하 테스트 도구: Apache JMeter
> 
> 테스트 환경: 동일한 Kubernetes 노드, 사용자 수 500명, 메시지 15,000건 기준

