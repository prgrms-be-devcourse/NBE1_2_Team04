# NBE1_2_Team04
# ⚽풋살 팀 매칭 서비스 FootWithMe

![foot_with_me_image](https://github.com/user-attachments/assets/96b798e6-0381-48a7-85f0-098ddb727c65)

<br>

## 프로젝트 소개

- FootWithMe는 ~~~ 입니다.
- ~~ 하는 서비스입니다.
- ~~ 하는 특징이 있습니다.
- 등등.

<br>

## 팀원 구성

<div align="center">

| **나민혁** | **노관태** | **문재경** | **박윤영** | **황민우** |
| :------: |  :------: | :------: | :------: | :------: |
| [<img src="https://avatars.githubusercontent.com/u/112960401?v=4" height=100 width=100> <br/> @NaMinhyeok](https://github.com/NaMinhyeok) | [<img src="https://avatars.githubusercontent.com/u/65394501?v=4" height=100 width=100> <br/> @Repaion24](https://github.com/Repaion24) |  [<img src="https://avatars.githubusercontent.com/u/108010440?v=4" height=100 width=100> <br/> @MoonJaeGyeong](https://github.com/MoonJaeGyeong) |  [<img src="https://avatars.githubusercontent.com/u/134857967?v=4" height=100 width=100> <br/> @pyy2114](https://github.com/pyy2114) |  [<img src="https://avatars.githubusercontent.com/u/97011190?v=4" height=100 width=100> <br/> @HMWG](https://github.com/HMWG) | 

</div>

<br>

## 1. 개발 환경

- Front : HTML, React, styled-components, Recoil
- Back-end : 제공된 API 활용
- 버전 및 이슈관리 : Github, Github Issues, Github Project
- 협업 툴 : Discord, Notion, Github Wiki
- 서비스 배포 환경 : Netlify
- 디자인 : [Figma]()
- [커밋 컨벤션](docs/Branch%20strategy%20and%20pull-request.md)
- [코드 컨벤션](docs/Code%20Convention.md)
- [스프라이트]()
  <br>

## 0. 개발 툴

| Software | 세부 Spec 사양 (Version) |
| --- | --- |
| Java | Java SE 17.0.11 |
| Spring Boot | 3.3.4 |
| Spring Boot Libraries | Data JPA, Web, Validation, Security, Redis Reactive: 2.1.0 |
| Lombok | 1.18.20 |
| QueryDSL | JPA: 5.0.0 (Jakarta) |
| JWT | JJWT API: 0.11.5 |
| Swagger/OpenAPI | SpringDoc OpenAPI MVC UI: 2.1.0 |
| MySQL | MySQL Community 8.0.39 |
| MySQL Connector | 8.0.29 |
| H2 Database | 2.1.214 (Test 용도) |
| JUnit | JUnit Platform Launcher: 1.9.2 |
| JTS | JTS Core: 1.19.0 |
| Hibernate Spatial | 6.5.3.Final |
| Spring Security | OAuth2 Client: 6.3.1 |
| WebSocket | Spring Boot Starter WebSocket |
| Embedded Redis | 0.7.2 (ARM 지원) |
| Jackson | Jackson Datatype JSR310, Jackson Databind |
| Spring RestDocs | Spring RestDocs Asciidoctor, MockMVC |


## 2. 채택한 개발 기술과 브랜치 전략

### React, styled-component

- React
    - 컴포넌트화를 통해 추후 유지보수와 재사용성을 고려했습니다.
    - 유저 배너, 상단과 하단 배너 등 중복되어 사용되는 부분이 많아 컴포넌트화를 통해 리소스 절약이 가능했습니다.
- styled-component
    - props를 이용한 조건부 스타일링을 활용하여 상황에 알맞은 스타일을 적용시킬 수 있었습니다.
    - 빌드될 때 고유한 클래스 이름이 부여되어 네이밍 컨벤션을 정하는 비용을 절약할 수 있었습니다.
    - S dot naming을 통해 일반 컴포넌트와 스타일드 컴포넌트를 쉽게 구별하도록 했습니다.

### Recoil

- 최상위 컴포넌트를 만들어 props로 유저 정보를 내려주는 방식의 경우 불필요한 props 전달이 발생합니다. 따라서, 필요한 컴포넌트 내부에서만 상태 값을 가져다 사용하기 위해 상태 관리 라이브러리를 사용하기로 했습니다.
- Redux가 아닌 Recoil을 채택한 이유
    - Recoil은 React만을 위한 라이브러리로, 사용법도 기존의 useState 훅을 사용하는 방식과 유사해 학습비용을 낮출 수 있었습니다.
    - 또한 Redux보다 훨씬 적은 코드라인으로 작동 가능하다는 장점이 있었습니다.
- 로그인과 최초 프로필 설정 시 유저 정보를 atom에 저장하여 필요한 컴포넌트에서 구독하는 방식으로 사용했습니다.

### eslint, prettier

- 정해진 규칙에 따라 자동적으로 코드 스타일을 정리해 코드의 일관성을 유지하고자 했습니다.
- 코드 품질 관리는 eslint에, 코드 포맷팅은 prettier에 일임해 사용했습니다.
- airbnb의 코딩 컨벤션을 참고해 사용했고, 예외 규칙은 팀원들과 협의했습니다.
- 협업 시 매번 컨벤션을 신경 쓸 필요 없이 빠르게 개발하는 데에 목적을 두었습니다.

### 브랜치 전략

- Git-flow 전략을 기반으로 main, develop 브랜치와 feature 보조 브랜치를 운용했습니다.
- main, develop, Feat 브랜치로 나누어 개발을 하였습니다.
    - **main** 브랜치는 배포 단계에서만 사용하는 브랜치입니다.
    - **develop** 브랜치는 개발 단계에서 git-flow의 master 역할을 하는 브랜치입니다.
    - **Feat** 브랜치는 기능 단위로 독립적인 개발 환경을 위하여 사용하고 merge 후 각 브랜치를 삭제해주었습니다.

<br>

## 3. 프로젝트 구조

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

<br>

## 4. 역할 분담

### 🍊나민혁

- **투표**
    - 쓰시면 됩니다.
- **예약**
    - 쓰시면 됩니다.

<br>

### 👻노관태

- **구장**
    - 쓰시면 됩니다.
- **예약**
    - 쓰시면 됩니다.

<br>

### 😎문재경

- **회원**
    - 쓰시면 됩니다.
- **예약**
    - 쓰시면 됩니다.

<br>

### 🐬박윤영

- **팀**
    - 쓰시면 됩니다.
- **예약**
    - 쓰시면 됩니다.

<br>

### 🐬황민우

- **채팅**
    - 쓰시면 됩니다.
- **예약**
    - 쓰시면 됩니다.

<br>

## 5. 개발 기간 및 작업 관리

### 개발 기간

- 전체 개발 기간 : 2022-12-09 ~ 2022-12-31
- UI 구현 : 2022-12-09 ~ 2022-12-16
- 기능 구현 : 2022-12-17 ~ 2022-12-31

<br>

### 작업 관리

- GitHub Projects와 Issues를 사용하여 진행 상황을 공유했습니다.
- 주간회의를 진행하며 작업 순서와 방향성에 대한 고민을 나누고 GitHub Wiki에 회의 내용을 기록했습니다.

<br>

## 6. 신경 쓴 부분

- [접근제한 설정](https://github.com/likelion-project-README/README/wiki/README-6.%EC%8B%A0%EA%B2%BD-%EC%93%B4-%EB%B6%80%EB%B6%84_%EC%A0%91%EA%B7%BC%EC%A0%9C%ED%95%9C-%EC%84%A4%EC%A0%95)

- [Recoil을 통한 상태관리 및 유지](https://github.com/likelion-project-README/README/wiki/README-6.%EC%8B%A0%EA%B2%BD-%EC%93%B4-%EB%B6%80%EB%B6%84_Recoil%EC%9D%84-%ED%86%B5%ED%95%9C-%EC%83%81%ED%83%9C%EA%B4%80%EB%A6%AC-%EB%B0%8F-%EC%9C%A0%EC%A7%80)

<br>

## 7. 페이지별 기능

### [초기화면]
- 서비스 접속 초기화면으로 splash 화면이 잠시 나온 뒤 다음 페이지가 나타납니다.
    - 로그인이 되어 있지 않은 경우 : SNS 로그인 페이지
    - 로그인이 되어 있는 경우 : README 홈 화면
- SNS(카카오톡, 구글, 페이스북) 로그인 기능은 구현되어 있지 않습니다.

| 초기화면 |
|----------|
|![splash](https://user-images.githubusercontent.com/112460466/210172920-aef402ed-5aef-4d4a-94b9-2b7147fd8389.gif)|

<br>

## 8. 테스트 커버리지

- 테스트 커버리지 작성

<br>

## 9. 개선 목표

- 개선 목표 작성

<br>

## 10. 프로젝트 후기

### 🍊 나민혁



<br>

### 👻 노관태



<br>

### 😎 문재경



<br>

### 🐬 박윤영



<br>

### 🐬 황민우
