# 📡 왜 SSE, WebSocket이 아닌 FCM을 선택했나요?

## 도입 배경
알림 기능은 서비스의 핵심 요소 중 하나입니다.
- 사용자는 챌린지 진행 상황, 인증 리마인드, 보상 수령 여부 등을 **실시간으로** 받아야 합니다.
- 다양한 클라이언트 환경(웹, Android, iOS)을 동시에 지원해야 하며, 안정적인 전송이 중요합니다.

---

## 대안 비교
### 1. SSE (Server-Sent Events)
- **장점**: 구현이 간단하고 서버 → 클라이언트 단방향 이벤트 스트림에 적합
- **단점**: 브라우저 환경에 최적화되어 있고, 모바일 앱(Android/iOS)에서는 활용하기 어렵다.
- **결론**: 멀티 플랫폼 지원이 어렵다.

### 2. WebSocket
- **장점**: 양방향 통신 가능, 지연 시간이 낮음
- **단점**: 연결 상태 유지 비용이 크고, 재연결 및 장애 처리 로직이 복잡하다.
- **결론**: 대규모 유저 환경에서 관리 비용이 높다.

### 3. FCM (Firebase Cloud Messaging)
- **장점**:
    - Android/iOS/웹 **모두 지원**
    - 연결 관리 및 재시도 로직을 Google 인프라가 처리
    - **푸시 기반**이므로 리소스 사용 효율적
- **단점**: Firebase 인프라에 의존적
- **결론**: 멀티 플랫폼, 대규모 트래픽 환경에서 가장 적합

---

## 최종 결정: **FCM**
우리는 알림 시스템의 목표를 "안정적이고 범용적인 실시간 알림" 으로 정의했습니다.

- SSE는 브라우저 전용이라는 한계,
- WebSocket은 연결 유지 비용 문제 때문에,

**멀티 플랫폼 지원과 안정성**을 모두 만족하는 **FCM**을 최종 선택했습니다.

👉 결과적으로, 클라이언트 환경에 구애받지 않고, 최소한의 서버 자원으로 안정적인 알림을 제공할 수 있게 되었습니다.


---
# 🔄 재전송 큐에 Kafka, Redis, RebbitMQ 중 Redis?

## 도입 배경
알림 전송은 항상 실패 가능성이 있습니다.  
네트워크 불안정, 기기 비활성화 등으로 실패한 알림을 일정 시간 후 재시도하는 큐가 필요했습니다.

---

## 대안 비교
### Kafka
- 대규모 트래픽 분산 처리, 메시지 영속성 강점
- 인프라 운영이 복잡하고 무거움
- 단순 재전송 요구사항에는 과도한 선택

### RabbitMQ
- 지연 큐, Dead Letter Queue 등 재시도 관리 기능 제공
- Kafka보다 가볍지만 별도 브로커 운영 필요
- Redis와 달리 캐싱/데이터 관리 통합 활용 불가

### Redis (ZSet)
- 메모리 기반 빠른 성능
- ZSet으로 재시도 시점을 정렬 관리 → 스케줄러와 쉽게 연동
- 이미 프로젝트에서 사용 중 → 인프라 추가 비용 없음
- 다중 서버 환경에서도 확장성 확보

---

## 최종 결정
Kafka는 무겁고, RabbitMQ는 운영 부담이 컸습니다.  
**Redis는 성능·단순성·운영 효율성을 모두 충족**하기 때문에 최종 선택했습니다.


---

---

---
# 알림 재전송 큐를 만든 이유

알림 서비스는 **유저 경험에 직결**된다.
- 네트워크 장애, 토큰 만료, 서버 다운 등으로 알림이 실패하면 유저는 곧바로 불편을 느낀다.
- 따라서 알림을 "한 번 보내고 끝"이 아니라, **실패 시에도 유실 없이 추적**하고 **자동으로 재시도**할 수 있는 구조가 필요했다.

## DB 재전송 큐


| 구조도 | 흐름 설명 |
|--------|-----------|
| <img src="https://velog.velcdn.com/images/wkdrnsgh1/post/d31b54c8-a156-4cff-9ff6-5fb05c48993b/image.png" width="550"> | - 알림을 **전송 시도**한다.<br><br>- 실패하면 **Status=대기** 상태로 DB에 저장한다.<br><br>- 스케줄러가 **DB에서 대기 상태 데이터를 조회**한다.<br><br>- 특정 **주기마다 재전송**을 시도한다. |

<table>
  <tr>
    <th>결과 사진</th>
    <th>결론</th>
    <th>개선 방향</th>
  </tr>
  <tr>
    <td><img src="https://velog.velcdn.com/images/wkdrnsgh1/post/e9de4404-b9cc-4691-9057-a1d7ac332b7b/image.png" width="400"></td>
    <td rowspan="3"><b>DB는 IO 병목 발생</b><br>대량 재전송 시 성능 저하가 드러났다.</td>
    <td rowspan="3">메모리 기반 Redis라면<br>IO 병목 문제를 해소할 수 있을 것 같아<br>전환을 시도했다.</td>
  </tr>
  <tr>
    <td><img src="https://velog.velcdn.com/images/wkdrnsgh1/post/4bea712a-4d9c-4e94-ac7b-b694d560cc60/image.png" width="400"></td>
  </tr>
  <tr>
    <td><img src="https://velog.velcdn.com/images/wkdrnsgh1/post/8b6cdb7b-add2-47c3-82aa-76a4df731609/image.png" width="400"></td>
  </tr>
</table>

## Redis - ZSet 재전송 큐 (순차)

| 구조도 | 흐름 설명 |
|--------|-----------|
| <img src="https://velog.velcdn.com/images/wkdrnsgh1/post/b1183645-a3c2-4214-a015-e82668292c47/image.png" width="550"> | - 알림을 **전송 시도**한다.<br><br>- 실패하면 **Status=대기** 상태로 Redis ZSet에 저장한다.<br><br>- 스케줄러가 **ZSet에서 score(재시도 시점) 기준으로 조회**한다.<br><br>- 해당 시간이 되면 **대상만 꺼내 재전송**한다. |

<table>
  <tr>
    <th>결과 사진</th>
    <th>결론</th>
    <th>개선 방향</th>
  </tr>
  <tr>
    <td><img src="https://velog.velcdn.com/images/wkdrnsgh1/post/414de6fd-c3b1-4f74-85e8-d2f88b80c91d/image.png" width="400"></td>
    <td rowspan="3"><b>Redis ZSet은 빠르지만</b><br>단일 스레드 구조라<br>대규모 트래픽에서는 지연이 발생했다.</td>
    <td rowspan="3">멀티 스레드 **병렬 처리**로<br>TPS 한계를 개선해보기로 했다.</td>
  </tr>
  <tr>
    <td><img src="https://velog.velcdn.com/images/wkdrnsgh1/post/0a8c7a62-e56b-4768-a64c-f55b9c56834c/image.png" width="400"></td>
  </tr>
  <tr>
    <td><img src="https://velog.velcdn.com/images/wkdrnsgh1/post/e26e476b-a403-4276-812d-1332b93f2b16/image.png" width="400"></td>
  </tr>
</table>

## Redis - ZSet 재전송 큐 (병렬)

| 구조도 | 흐름 설명 |
|--------|-----------|
| <img src="https://velog.velcdn.com/images/wkdrnsgh1/post/97dd6f6c-ad60-47c3-b5a2-d569371ed08d/image.png" width="550"> | - 알림을 **전송 시도**한다.<br><br>- 실패하면 **Status=대기** 상태로 Redis ZSet에 저장한다.<br><br>- 스케줄러가 **여러 스레드로 병렬 실행**되며 대기 항목을 조회한다.<br><br>- 동시에 여러 건을 **병렬로 재전송**하여 TPS를 크게 높인다. |

<table>
  <tr>
    <th>결과 사진</th>
    <th>결론</th>
    <th>추후 개선 방향</th>
  </tr>
  <tr>
    <td><img src="https://velog.velcdn.com/images/wkdrnsgh1/post/1da7c883-c7a0-4101-bde6-7c0c21275673/image.png" width="400"></td>
    <td rowspan="3">병렬 처리로 TPS가 약 5배 향상됐다.<br>30만 건 테스트에서도 안정성을 확인했다.</td>
    <td rowspan="3">병렬 처리에서 발생한 단일 노드 한계 문제를 Kafka의 Partition 기반 분산 처리로 해결할 수 있을 것 같다.</td>
  </tr>
  <tr>
    <td><img src="https://velog.velcdn.com/images/wkdrnsgh1/post/fa37ef80-cf0c-483c-8aee-fbd2061d5389/image.png" width="400"></td>
  </tr>
  <tr>
    <td><img src="https://velog.velcdn.com/images/wkdrnsgh1/post/6b354fd7-4dea-4746-b1f0-935f5fc2f72b/image.png" width="400"></td>
  </tr>
</table>


---

## 성능 비교 결과

| 항목 | DB 기반 | Redis ZSet (단일) | Redis ZSet (병렬) |
|------|---------|-------------------|-------------------|
| 1건 처리 시간 | 2.84 ms | 1.05 ms | **0.98 ms** |
| 500건 재시도 | 483 ms | 177 ms | **98 ms** |
| TPS | 339/s | 953/s | **5100/s** |

<img src="https://velog.velcdn.com/images/wkdrnsgh1/post/e4aa8abd-60ac-4e00-8020-23ab84828bdb/image.png" width="450">

## 추 후 개선사항
### 병렬 처리의 한계와 Kafka 전환 검토

마찬가지로, **Redis 병렬 처리 방식도 한계가 있었다.**  
싱글 스레드 기반인 Redis 특성상 다중 스레드가 동시에 ZSet에 접근하면 **락 경합, 중복 처리, 확장성 부족** 문제가 발생할 수 있었다.

이를 개선하기 위해서는 **분산 락, 샤딩, 오토스케일링** 같은 추가 전략이 필요하며,  
더 나아가 **Kafka**와 같은 분산 메시지 브로커를 도입하는 것도 고려할 만하다.

Kafka는 **데이터 영속성, Consumer Group 기반 확장성, DLQ 지원, MSA 친화성**에서 강점을 갖는다.  
따라서 대규모 트래픽 환경에서는 Redis 기반 재전송 큐와 병행하거나,  
**중장기적으로 Kafka 기반의 이벤트 허브 구조**로 확장하는 것이 더 안정적일 수 있다.
