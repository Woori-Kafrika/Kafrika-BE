# ☕ Kafrika Backend Service

**Kafrika Backend**는 Kafka를 활용한 실시간 채팅 백엔드 시스템으로,  
**Kafka 사용 유무에 따른 시스템 성능과 부하 처리 능력 차이를 비교 분석**하기 위해 설계된 프로젝트입니다.

해당 프로젝트는 다음 두 방식으로 메시지를 처리합니다:

- 🔄 **동기 방식**  |  클라이언트 요청 시 즉시 DB에 저장하고 WebSocket으로 브로드캐스트

- 📨 **비동기 방식 (Kafka 기반)**  |  클라이언트 요청을 Kafka로 전송하고, Consumer가 DB 저장 및 WebSocket 브로드캐스트 처리

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

| 실험 항목             | 동기 방식 (No Kafka, STOMP) | 비동기 방식 (Kafka 1 Consumer / 1 Partition) | 비동기 방식 (Kafka 1 Consumer / 3 Partitions) |
|----------------------|----------------------------|----------------------------------------------|----------------------------------------------|
| 평균 응답 시간 (ms)  | 약 2,600 (최대 20,000 이상) | **7**                                        | 증가 (최대 7,888ms)                          |
| 처리량 (TPS)         | 급격히 저하됨 (Error 80%↑) | **90.0 req/sec**                             | 초반 안정적이나 후반 급감                     |
| 오류율 (%)           | 80% 이상 메시지 손실        | **0.00%**                                    | 4.53% (408건 오류)                           |
| 안정성               | 서버 과부하, 지연 심화       | 매우 안정적                                   | Consumer 병목으로 성능 저하                  |

> 부하 테스트 도구: Apache JMeter
> 
> 테스트 환경: 사용자 수 500명, 메시지 총 15,000건  

---

## 🔎 인사이트

- **Kafka 도입 효과**
  
  STOMP만 사용했을 때는 평균 응답 시간이 **2.6초**, 최대 **20초**, 오류율 **80% 이상**으로 트래픽 폭증 상황에서 사실상 장애 수준이었음.
  
  반면 Kafka를 사용하면 **90 TPS, 평균 응답 시간 7ms, 오류율 0%**로 안정적인 처리 성능 확보.

- **파티션과 Consumer 구성의 중요성**
  
  단순히 **파티션 수를 늘린다고 성능이 향상되지 않음**.
  
  1 Consumer가 3개 파티션을 모두 처리하는 경우, 오히려 **병목 발생 → 오류율 4.5%, 최대 지연 7.8초**.
  
  → **Consumer 수와 파티션 수의 균형 잡힌 설계**가 필수적임.

**📌 결론**
  
> Kafka는 실시간 채팅 같은 **대규모 동시성 환경에서 안정성과 확장성을 보장**하지만, **파티션/Consumer 매핑 전략**을 신중하게 설계해야 성능을 극대화할 수 있음.  
