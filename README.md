# NBE1_2_Team04
# ⚽풋살 팀 매칭 서비스 FootWithMe

![foot_with_me_image](https://github.com/user-attachments/assets/96b798e6-0381-48a7-85f0-098ddb727c65)

## 프로젝트 소개

- FootWithMe는 풋살 팀 매칭 및 구장 예약 시스템 입니다.
- 팀을 생성하여 팀원들과 풋살 일정을 관리할 수 있습니다.
- 팀 경기 예약을 진행하면 구장 및 날짜 선정 진행을 도와줍니다.
- 용병 시스템을 통해 경기 예약에 부족한 팀원을 구해줍니다.
- 조건에 맞는 다른 팀의 예약이 있다면 경기 매칭을 도와줍니다.
- 조건에 맞는 매칭의 경기장을 예약해줍니다.
- 팀 및 예약에 참여한 인원들과 채팅을 할 수 있습니다.
- 풋살 구장 관리자는 구장에 대해 관리할 수 있습니다.


### 개발 기간

- 전체 개발 기간 : 2024-09-23 ~ 2024-10-10
- 프로젝트 기획 : 2024-09-23 ~ 2024-09-25
- 개발환경 세팅 : 2024-09-24 ~ 2024-09-25
- 기능 구현 : 2024-09-25 ~ 2024-10-09
- 문서화 : 2024-10-09 ~ 2024-10-10


### 작업 관리

- 매일 아침 스크럼 및 데일리 스탠드업 회의를 진행하며 작업한 내용, 작업 할 내용, 이슈 및 협업이 필요한 사항에 대해 공유하고 노션에 기록하며 작업을 진행했습니다.
- GitHub Projects와 Issues를 사용하여 진행 상황을 공유했습니다.
- 작업을 마친 Issue에 대해 Pull Request를 올리면 팀원 모두가 코드 리뷰를 진행하고 피드백 및 간단한 소감 작성을 마친 뒤 병합을 진행했습니다.


### 2차 팀 Notion
[2차 4팀](https://www.notion.so/4-a9e3d850571f4db599a7d62575fc4c33)
 
### 요구사항 명세서
[요구사항 명세서](https://www.notion.so/2e42ec0d763f46089fea5a8f86c4f213)

### 기획서
[2차 팀 프로젝트 기획서](https://www.notion.so/2-6876b9e49d6d41c6b7477db6192e7de1)

---

## 팀원 구성
<div align="center">

|                       **나민혁**                       | **노관태** | **문재경** | **박윤영** | **황민우** |
|:---------------------------------------------------:|  :------: | :------: | :------: | :------: |
| [<img src="https://em-content.zobj.net/source/google/350/seedling_1f331.png" height=100 width=100> <br/> @NaMinhyeok](https://github.com/NaMinhyeok) | [<img src="https://avatars.githubusercontent.com/u/65394501?v=4" height=100 width=100> <br/> @Repaion24](https://github.com/Repaion24) |  [<img src="https://avatars.githubusercontent.com/u/108010440?v=4" height=100 width=100> <br/> @MoonJaeGyeong](https://github.com/MoonJaeGyeong) |  [<img src="https://avatars.githubusercontent.com/u/134857967?v=4" height=100 width=100> <br/> @pyy2114](https://github.com/pyy2114) |  [<img src="https://avatars.githubusercontent.com/u/97011190?v=4" height=100 width=100> <br/> @HMWG](https://github.com/HMWG) | 

</div>
<br>


---

## 1. 개발 환경

- Front : 구현 예정
- Back-end : Java, Spring-boot, JPA, MYSQL, Redis
- 버전 및 이슈관리 : Github, Github Issues, Github Project
- 협업 툴 : Discord, Notion, Slack
- 문서화 : Rest Docs, Notion, README.md
- 테스트 : JUnit5, Postman
- 디자인 : [Figma](https://www.figma.com/design/c3v4rVUGBy3KV9g7kOD5ZK/%EB%8D%B0%EB%B8%8C%EC%BD%94%EC%8A%A4-4%ED%8C%80?node-id=0-1&t=eZOYej1YMUn40Khy-1)

| Software              | 세부 Spec 사양 (Version) |
|-----------------------| --- |
| Java                  | Java SE 17.0.11 |
| Spring Boot           | 3.3.4 |
| Spring Boot Libraries | Data JPA, Web, Validation, Security, Redis Reactive: 2.1.0 |
| Lombok                | 1.18.20 |
| QueryDSL              | JPA: 5.0.0 (Jakarta) |
| JWT                   | JJWT API: 0.11.5 |
| MySQL                 | MySQL Community 8.0.39 |
| MySQL Connector       | 8.0.29 |
| H2 Database           | 2.1.214 (Test 용도) |
| JUnit                 | JUnit Platform Launcher: 1.9.2 |
| JTS                   | JTS Core: 1.19.0 |
| Hibernate Spatial     | 6.5.3.Final |
| Spring Security       | OAuth2 Client: 6.3.1 |
| WebSocket             | Spring Boot Starter WebSocket |
| Embedded Redis        | 0.7.2 (ARM 지원) |
| Jackson               | Jackson Datatype JSR310, Jackson Databind |
| Spring REST Docs      | Spring RestDocs Asciidoctor, MockMVC |

---

## 2. 개발 가이드라인
### 브랜치 전략
- [깃 컨벤션](docs/Branch%20strategy%20and%20pull-request.md)
### 코드 컨벤션
- [코드 컨벤션](docs/Code%20Convention.md)

---

## 3. Entity Relationship Diagram

![erd_image](https://github.com/user-attachments/assets/74b305be-70c8-4ebd-a261-87c6a3a5d1af)
[ERDCloud](https://www.erdcloud.com/d/wsi5ipKNtnCeHDegM)

---

## 4. 프로젝트 패키지 구조

```
─docs
│   └───asciidoc
│       └───api
│           ├───chat
│           ├───game
│           ├───member
│           ├───mercenary
│           ├───participant
│           ├───reservation
│           ├───stadium
│           ├───team
│           └───vote
├───main
│   ├───generated
│   │   └───team4
│   │       └───footwithme
│   │           ├───chat
│   │           │   └───domain
│   │           ├───global
│   │           │   └───domain
│   │           ├───member
│   │           │   └───domain
│   │           ├───resevation
│   │           │   └───domain
│   │           ├───stadium
│   │           │   └───domain
│   │           ├───team
│   │           │   └───domain
│   │           └───vote
│   │               └───domain
│   ├───java
│   │   └───team4
│   │       └───footwithme
│   │           ├───chat
│   │           │   ├───api
│   │           │   │   └───request
│   │           │   ├───domain
│   │           │   ├───repository
│   │           │   └───service
│   │           │       ├───event
│   │           │       ├───request
│   │           │       └───response
│   │           ├───config
│   │           │   ├───redis
│   │           │   └───websocket
│   │           ├───global
│   │           │   ├───api
│   │           │   ├───domain
│   │           │   ├───exception
│   │           │   ├───repository
│   │           │   └───util
│   │           ├───member
│   │           │   ├───api
│   │           │   │   └───request
│   │           │   │       └───validation
│   │           │   ├───domain
│   │           │   ├───jwt
│   │           │   │   └───response
│   │           │   ├───oauth2
│   │           │   │   └───response
│   │           │   ├───repository
│   │           │   └───service
│   │           │       ├───request
│   │           │       └───response
│   │           ├───resevation
│   │           │   ├───api
│   │           │   │   └───request
│   │           │   ├───domain
│   │           │   ├───repository
│   │           │   └───service
│   │           │       ├───request
│   │           │       └───response
│   │           ├───stadium
│   │           │   ├───api
│   │           │   │   └───request
│   │           │   │       └───validation
│   │           │   ├───domain
│   │           │   ├───repository
│   │           │   ├───service
│   │           │   │   ├───request
│   │           │   │   └───response
│   │           │   └───util
│   │           ├───team
│   │           │   ├───api
│   │           │   │   └───request
│   │           │   ├───domain
│   │           │   ├───repository
│   │           │   └───service
│   │           │       ├───request
│   │           │       └───response
│   │           └───vote
│   │               ├───api
│   │               │   └───request
│   │               │       └───annotation
│   │               │           └───validator
│   │               ├───domain
│   │               ├───repository
│   │               └───service
│   │                   ├───request
│   │                   └───response
│   └───resources
└───test
    ├───java
    │   └───team4
    │       └───footwithme
    │           ├───chat
    │           │   └───api
    │           ├───docs
    │           │   ├───chat
    │           │   ├───member
    │           │   ├───mercenary
    │           │   ├───participant
    │           │   ├───reservation
    │           │   ├───stadium
    │           │   ├───team
    │           │   └───vote
    │           ├───member
    │           │   ├───api
    │           │   ├───jwt
    │           │   ├───repository
    │           │   └───service
    │           ├───mercenary
    │           │   └───api
    │           ├───paricipant
    │           │   └───api
    │           ├───resevation
    │           │   ├───api
    │           │   ├───repository
    │           │   └───service
    │           ├───security
    │           ├───stadium
    │           │   ├───api
    │           │   ├───repository
    │           │   └───service
    │           ├───team
    │           │   └───service
    │           └───vote
    │               ├───api
    │               ├───domain
    │               ├───repository
    │               └───service
    └───resources
        └───org.springframework.restdocs.template
```

---

## 5. 역할 분담

### 🌱 나민혁

- **투표**
  - 동적 스케쥴링 + 이벤트 처리를 통한 마감시간에 따른 종료 구현
  - RequestDTO Custom Annotaion 적용
  - 투표 API 구현
- **예약**
  - 투표 마감에 따른 예약 생성
  - 투표 인원에 따라 다른 예약 생성

<br>

### 👻 노관태

- **풋살장**
  - 풋살장에 대한 API 구현
  - 구장에 대한 API 구현
- **예약**
  - 예약 관련 기능 일부 구현
  - 매칭 신청, 수락, 거절 기능 구현

<br>

### 😎 문재경

- **회원**
  - AccessToken 과 RefreshToken 을 이용한 JWT 회원 로그인 구현
  - OAuth 2.0 을 사용한 구글 로그인 구현
  - 멤버 관련 API 구현
- **예약**
    - 예약 취소 API 구현
- **시큐리티**
  - CORS 설정 구현
  - Security 를 통한 유저 권한 체크 및 인증 구현

<br>

### 🙃 박윤영

- **팀**
  - 팀 기능 관련 API 구현
  - 팀 멤버 관련 API 구현
- **예약**
  - 팀 예약 정보 조회 기능 API 구현

<br>

### 🤔 황민우

- **채팅**
  - WebSocket과 Redis를 사용한 메세지 브로커 방식의 채팅 시스템 구현
  - Redis에서 채팅방 세션을 관리 할 Key-value 데이터베이스 사용
  - 팀, 예약 생성시 해당하는 채팅방이 생성되는 이벤트 처리
  - 채팅방, 채팅 멤버를 생성할 수 있는 API 구현
- **예약**
  - 용병 게시판에 대한 API 구현
  - 예약 참여 인원에 대한 API 구현

<br>

---


## 6. 테스트 커버리지
![test_coverage_image](https://github.com/user-attachments/assets/9f3b4575-1dfd-4615-9838-d2965bc20f66)
#### 클래스 82%, 메서드 68%, 라인 65%, 브랜치 38% 커버

---

## 7. 신경 쓴 부분

- Embedded Redis 사용
  - 프로젝트 어플리케이션 실행 시 같이 실행되는 내장 레디스 데몬 사용
  - 개발 시, 통일된 환경 보장


- OAuth2.0 사용
  - Google 계정을 이용한 로그인


- Security 예외 처리
  - [필터 예외처리](https://velog.io/@mjk8087/Filter-Exception-Handler)

  
- 자동화를 위한 이벤트 처리
  - 투표 완료 시, 예약 생성 이벤트
  - 예약, 팀 생성시 해당 채팅방 생성 및 팀원 초대
  - [이벤트처리와 동적 스케쥴링](docs/use-eventwithtaskschedule.md)


- TaskScheduler 사용
  - [동적 스케쥴링 도전하기](docs/use-taskschedule.md)


- Redis pub/sub 기능 사용
  - Redis의 메세지 브로커 기능을 사용한 웹소켓 채팅 기능
  - 다중 서버 사용 시, 하나의 Redis 서버로 같은 채팅방 사용 가능

  
- JPA 단일 테이블 전략
  - VoteItem 및 Chatroom 엔티티에 사용
  - Join이 필요 없으므로 일반적으로 조회 성능 향상
  - [SingleTable전략 더 효과적으로 사용해보기](docs/use-singletable.md)


- Soft Delete 정책
  - delete시 삭제 플래그를 설정하여 데이터의 완전한 삭제를 방지
  - [Soft Delete 사용해보기](docs/use-softdelete.md)


- Test 진행


- RestDocs 작성
  - API 명세서를 Rest Docs로 작성
  - Rest Docs 스니펫을 만들기 위한 API 테스트 검증


- MYSQL 공간 데이터, 함수를 통한 성능 향상
  - [공간함수와 삼각 함수 기반 거리 계산 성능 비교](docs/RBF%20Research.md)


- Custom Annotation으로 RequestDTO 검증
  - [`@Valid` 의 정체와 검증](https://velog.io/@naminhyeok/RequestDTO-%EC%BB%A4%EC%8A%A4%ED%85%80-%EA%B2%80%EC%A6%9D-%EC%99%9C-%EC%95%88%EB%90%A0%EA%B9%8C)


---

## 8. API 명세서

###  [REST Docs](docs/FootWithMeRESTAPI.pdf)

---

## 9. 개선 목표

- 성능 체크 및 코드 리팩토링을 통한 성능 향샹
- 경계값 테스트 진행
- SQL 튜닝 하여 성능 향상
- 마무리 전에 급하게 작성한 코드 리팩토링
- QueryDsl 적극적으로 사용하기
- N+1 문제 발생지점 파악 후 해결
- CQRS 패턴 도전
- SOLID 원칙에 따라서 객체지향 설계로 리팩토링 도전
- 반복문으로 단일쿼리가 나가는 곳 개선
- 동적 스케쥴링 인메모리인 ConcurrentHashMap이 아닌 Quartz 또는 Message Queue 사용
- 채팅 내역 저장 시, 일괄 저장 구현하기 ( 데이터베이스 과부화 방지 )
- 컨테이너화를 통한 환경 통합
- 배포 및 CI/CD 구현하기
<br>

---
## 10. 프로젝트 후기

### 🌱 나민혁

프로젝트 하면서 목표했던 바를 많이 이룬거 같아서 기뻐요.
테스트에 대해서도 팀원들이 같이 고민하고 짜주셔서, 저 혼자서 했으면 못했을 테스트관련 문제도 해결해주시고 해서 좋았습니다.
다들 열정있게 참여해주셔서 잘 끝낼 수 있었던 것 같습니다. 다들 감사합니다 ☺️

[1주차 회고](https://velog.io/@naminhyeok/%EB%8D%B0%EB%B8%8C%EC%BD%94%EC%8A%A4-2%EC%B0%A8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-1%EC%A3%BC%EC%B0%A8-%ED%9A%8C%EA%B3%A0)

[2주차 회고](https://velog.io/@naminhyeok/%EB%8D%B0%EB%B8%8C%EC%BD%94%EC%8A%A4-2%EC%B0%A8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-2%EC%A3%BC%EC%B0%A8-%ED%9A%8C%EA%B3%A0)

<br>

### 👻 노관태

이번 프로젝트에서 팀원들과의 소통에서 많은 것을 배울 수 있었습니다.
특히 PR 과정에서 진행된 코드 리뷰를 통해 지식을 공유하고, 다양한 의견을 토론하는 과정이 큰 도움이 되었습니다.
RESTDocs와 테스트를 통합하여 진행한 경험을 통해 API 문서화와 테스트 자동화의 중요성을 깊이 이해하게 되었습니다.
이번에 배운 내용들을 앞으로의 개발 프로젝트에서도 지속적으로 활용하도록 하겠습니다.
팀원 분들 모두 열정적인 참여 덕분에 더욱 의미 있었습니다. 진심으로 감사드립니다!


<br>

### 😎 문재경
코드 리뷰를 너무 잘해주셔서 부족한 부분을 많이 배워가는 프로젝트였습니다.
평소에 잘 작성하지 않고 잘 인지하지 못했던 테스트 코드 작성에 대해 굉장히 많이 배우고
많이 사용하여 다음에는 더 익숙하게 사용이 가능할 것 같습니다!
또한 평소 Swagger 를 통해서 API 문서화를 진행했는데 RESTDocs 를 사용해서 문서화를
진행한 것도 신선한 경험이었습니다.
모든 팀원 분들이 너무 열심히 잘 해주셔서 성공적으로 마무리 한 것 같습니다!
고생하셨습니다 :)


<br>

### 🙃 박윤영
실력좋은 팀원분들 덕분에 많이 배워가는 프로젝트였습니다.
평소에 부족했던 JPA, JUnit 테스트를 깊게 해볼 수 있는 좋은 기회였고,
git 프로젝트, PR, 코드 리뷰 등을 통해 체계적인 협업을 할 수 있어서 좋았습니다!
제가 실력이 많이 부족했는데 모든 팀원 분들이 잘 도와주시고 꼼꼼하게 코드리뷰를 해주셔서 도움이 많이됐습니다.
다들 감사하고, 정말 고생 많으셨습니다!



<br>

### 🤔 황민우

이번 프로젝트에서 팀장을 담당했는데 팀원분들이 호응도 잘 해주시고 의견 내주시거나 팀 활동 참여를 잘 해주셔서 너무 감사했습니다! <br>
저에게 이런 완성도 있는 큰 프로젝트는 처음이였는데 협업 과정부터 GitHub 및 git 활용, 새로운 기술 적용한 기능 구현과 성능 체크, 테스트 및 문서화까지 너무 색다르고 배움의 깊이가 다른 즐거운 시간이였습니다. <br>
특히 PR을 통해 코드 리뷰하고 받은 피드백을 적용하는 시간이 너무 뜻깊었습니다! 성실하게 모든 활동 잘 참여해주시고 각자 맡은 바 책임 잘 져주신 팀원분들께 다시한번 감사드립니다!!!❤️ <br>
