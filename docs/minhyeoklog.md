
---
# 들어가며
프로젝트를 하며 매주 회고를 하며 내가 학습 한 것 그리고 앞으로 공부해보고 관심이 가는 것을 정리해보려고 한다. 1차 프로젝트때도 회고를 하면서 앞으로도 꾸준히 회고를 해나가야겠다고 생각했고, 프로젝트 기간이 짧지않은 만큼 잊어버리지 않도록 회고를 매주 진행하려고 한다.
회고는 개인적인 복습, 그리고 과거의 내가 어떤 생각을 했는지, 그리고 어떤 결과를 얻었는지 알 수 있는 좋은 방법이라고 생각하기 때문에 작성 할 예정이다.

# 프로젝트 목표
1. 모든 PR에 코드리뷰 하기&받기
2. record 써보기
3. QueryDsl 활용하기
4. SQL 튜닝(?) 하기라고 하며 인덱스 써볼 구석 찾아보기
5. Soft Delete 활용하기
6. @Embaddable, 고급매핑 사용하여 ORM스럽게 사용해보기
7. 노션에 정리해둔 개발 키워드 중 도전 할 수 있는 부분 해보기
8. 당연하게도 테스트 열심히 짜기
9. 기록하기 & 회고쓰기


# 프로젝트의 시작
2차 프로젝트는 자유주제로 REST API를 만들면 되었다. 연휴 때 거창한 계획은 있었지만 다 완료하진 못했다. 하지만 프로젝트가 시작하고 새로운 팀이 꾸려진 만큼 화이팅해보려고 노력했다.

새롭게 만난 팀원들은 다들 굉장히 편하게 대해주셨고, 순조롭게 대화가 오고갔다. 그리고 한분께서 엄청 잘 이끌어주셔서 팀장 추천해드렸고 팀장을 하게 되셨다. 등떠민거같지만 팀장님이 정리를 굉장히 잘해주셔서 추천을 잘했다고 생각한다. 1주차가 지나는 지금도 감사함을 느끼고 있다.

# 설계

## 아이디어 회의
아무래도 자유주제이다 보니 새롭게 만난 사람들과 갑작스럽게 아이디어를 회의하려니 좋은 생각이 떠오르지 않았다. 개인적으로는 기술적으로 다양하게 깊이있는 학습을 하고싶었지만 너무 뻔한 쇼핑몰, 블로그 같은쪽으로 기울었다. 뭔가 새로운걸 떠올리기 쉽지 않았고, 팀원 분들도 좋은 생각이 안떠오르신거같다.

그래서 여름에 혼자서 하려고 했던 프로젝트를 꺼내왔고(도중에 포기했다...) 팀원들이 좋은 아이디어를 많이 내주셔서 여러가지 살을 붙이게 되었다.

### 아이디어 구체화

내가 기존에 하려고 했던 아이디어는 친구들과 함께하고 있는 풋살팀이 풋살을 하기 위해서 여러 과정을 거쳐야했다.

1. 카카오톡 투표로 언제 할 지 정하기
2. 투표로 날짜와 시간이 정해지면 사장님께 구장을 사용 할 수 있는지 연락하기
3. 구장을 사용할 수 있다면 상대팀을 구하기 위해 네이버 밴드에 글 올리기
4. 연락온 상대팀과 협의 후 구장 예약하기
5. 공차기

한번 풋살을 차기 위해서 카카오톡, 문자, 네이버 밴드 등 너무 여러가지를 사용해야 했다. 그리고 투표는 매주올리는데 내가 수동으로 매주 올려야했다...

이러한 이야기를 전달 하니 팀원들이 여러가지 기능들을 이야기 해서 살을 붙이게 되었고, 전체적인 구조는 이 과정들을 자동화 하는것이었다.

1. 투표 스케쥴 만들어서 자동으로 생성하기
2. 투표된 날의 구장이 사용가능한지 여부 확인
3. 투표가 종료되면 자동으로 같은 시간대의 팀 탐색
4. 상대팀 선택하여 매칭 진행
5. 공차기

이렇게 보니까 어차피 5스텝인거 같은데 일단 한 어플리케이션 안에서 동작하고, 대부분이 자동화되어있다는 점을 생각하면 내가 목표하는 바였지 않나 생각한다.

이걸 하기 위해 필요한 기능들을 크게 분류로 나눠서
- 투표 기능
- 채팅 기능
- 구장관련 기능
- 예약&매칭 기능
- 회원관리 기능
- 팀 관련 기능
- 결제 기능

이렇게 나눠보았다. 이곳에서 결제는 뒤로 미루고, 먼저 진행해보려고 한다. 실제로 결제를 하려면 생각보다 준비해야 할 것이 많아보였다.


## 화면 설계
가볍게 기능을 정리해두고 실제로 화면을 그리면서 어떠한 기능이 들어갈지, 어떤 데이터가 필요할지 더 구체화 해보았다.

하지만 할 때 마다 느끼는거지만 디자인은 재능이 없다..

그래도 피그마가 많이 바뀌었다. 분명 올해 3월까지 이런 기능이 없었던거같은데 템플릿들이 생겨서 생각보다 쉽게 만들 수 있었다. 그리고 과거에 팀플을 했던 팀원들이 만들었던 피그마까지 조금 이용했다. 과거에 피그마 만들어준 팀원들 감사합니다.

![](https://velog.velcdn.com/images/naminhyeok/post/bbdccb18-4d85-41d4-bc59-365214963dfa/image.png)

진짜 순수하게 못만들었다. 그리고 실제로 프론트를 만들건 아니기도 했기 때문에... 대충 어떤 데이터가 들어가는지 감만보려고 했다. 하지만 좀 더 잘해야됐던건 맞다... 다시 보니 중구난방이긴하다.


## ERD 설계

기능들과 화면들을 보면서 어떤 데이터가 필요할 지 생각하면서 ERD를 그렸다. 사실 나는 ERD를 만드는 것도 좋지만 너무 데이터베이스 관점에서 만들고 싶지 않았다. 왜냐면 그렇게 하니 연관관계도 많이 걸리고, 양방향 연관관계, 단방향 연관관계가 많이 생기게 되었다.
데이터베이스의 관점에서는 무결성을 위해서 FK를 갖는게 당연히 좋으나, 실제로 코딩하기에는 불편한점도 꽤 많기도 하고 연관관계 사용을 지양하라는 얘기도 많이보았고, 실제로 과거 프로젝트 때 현업에 있으신분께 이야기를 나눌 기회가 있었는데 실무에서는 FK를 안쓴다는 이야기를 들었었다.

그 때 해주신 말씀으로는 운영상에 문제가 많다고 하셨다. 데이터 마이그레이션을 할 때나 장애가 났을 때 테이블을 다루기 힘들다고 하셨다. 그래서 그 때는 실제로 FK를 하나도 걸지 않고 진행했다.

[DB 외래키에 대해 / JPA 인덱스 명시 + 양방향 연관관계](https://www.youtube.com/watch?v=6q0-IT5J0nI)
[JPA Entity 연관관계 어떻게 걸까요? + 엔티티 연관관계 PTSD](https://www.youtube.com/watch?v=vgNHW_nb2mg)
[[JPA] Foreign key를 제거하기로 했습니다 (feat. JPA 적용)](https://velog.io/@yrc97/JPA-Foreign-key%EB%A5%BC-%EC%A0%9C%EA%B1%B0%ED%95%98%EA%B8%B0%EB%A1%9C-%ED%96%88%EC%8A%B5%EB%8B%88%EB%8B%A4)

위 영상과 글들을 참고해보면 도움이 되지않을까 생각한다.

그리고 객체의 관점에서 Entity를 설계하면 좋지 않을까? 라고도 생각했다. ORM은 말그대로 객체로 매핑해주는건데 항상 DB관점에서만 바라보니까 객체지향적이지도 않다고 생각했다.

하지만 이걸 팀원들에게 설득하는 과정이 생각보다 쉽지는 않았고, 이런 잡설보다는 일단 실제로 하고 코드에서 어떻게 수정하려고 했다.

![](https://velog.velcdn.com/images/naminhyeok/post/20958ae4-8d9c-4d47-9de6-a9ad0a4b39fc/image.png)

솔직히 제대로 정리가 되어있지는 않고 현재는 개발하면서 엔티티도 바뀌면서 조금의 수정이 있었지만, 생각보다 테이블이 많이 나왔다...

채팅관련 개념도 어색했고, 투표도 어색했다. 항상 테이블 설계는 어려운 것 같다. 실제로 어떤 테이블이 필요한지, 그리고 어떤 데이터가 있어야할 지, 정규화는 어떻게 해야되는지

그래도 개발에는 정답은 없다 항상 트레이드 오프니 이번 경험이 다음 ERD 설계를 위한 거름이 되기를 바래야겠다.

## 요구사항 명세 & 시나리오 플로우

이 때가 2일차였을 것이다. 솔직히 설계에 많은 시간을 쏟아야되는 것도 맞지만 뭔가 지쳐갈 즈음이었다. 글로만 적어내려가니 실제 코드가 어떻게 동작할 지 막막하기만 했다. '설계를 어디까지 해야하지?' 라는 생각이 들면서도 과거에 설계를 잘해두니 편했던 경험을 생각하면 설계하는게 어렵고 힘들지만 꼼꼼히 해야한다고 생각했지만...... 하여튼 최선을 다했다 !!!

![](https://velog.velcdn.com/images/naminhyeok/post/08e1c09b-0bd7-4fae-bbb6-1f8e56f80633/image.png)

열심히 팀원들의 도움과 함께 써내려갔다. 회고를 하는 이 시점에서는 구현을 하고 있기 때문에 설계를 좀 더 정교하게 했어야 했다라는 생각도 들지만 좋게 말하면 애자일하게(이렇게 퉁치기 위해서 사용하는 단어가 아닌건 알고있지만) 구현해나가면서 수정이 필요한 부분은 수정해나가면 되지않을까 생각한다.

![](https://velog.velcdn.com/images/naminhyeok/post/9d70b046-11b4-4a3a-be33-4e70849626fe/image.png)

팀원들이 노션에 시나리오 플로우도 작성해주셨다. 나도 몇개 적긴했는데 대부분 팀원들이 잘 작성해주셨다.


# 협업
## Github

깃은 평소에 사용했던 대로 Issue와 PR을 이용했다. 깃 컨벤션은 당연히 `feat: ~~기능구현` 와 같이 기본적인 것을 사용하기로 했다.
그리고 깃 브랜치 전략도 그냥 일반적으로 하는 develop 브랜치를 두고 이슈단위로 브랜치를 파서 작업을 하도록 했다.
그리고 가장 중요한! 공부를 하는데 있어서는 이만한게 없다고 생각한 모든 PR에 모든 팀원이 리뷰를 필수로 하자고 제안했다.

내가 이번 프로젝트 1번 목표로 잡았는데 팀원들이 감사하게도 받아들여주셨고, 그에 따라서 브랜치의 규칙을 만들어서 팀원이 5명이기 때문에 최소 4명이상 리뷰하지 않으면 머지를 할 수 없게 만들었다.

![](https://velog.velcdn.com/images/naminhyeok/post/a0b3f42f-ebd7-419e-a64b-3f673de3a18c/image.png)

다른분이 작업하신 건데 아직 머지를 나만 진행해서 머지가 막혀있다.

공부하는 곳이기 때문에 코드리뷰가 가장 중요하다고 생각한다. 놓쳤던 부분, 생각해볼 만한 거리를 던져 줄 수 있는 좋은 기회 !!


그런데 코드리뷰를 하기 위해 리뷰어로 팀원을 걸어야 했는데 지금 Organizationa 팀에 들어와있는 인원이 53명으로 전부 다 들어와있어서 PR 할 때 리뷰어를 걸기가 너무 불편했다.

![](https://velog.velcdn.com/images/naminhyeok/post/bb5f441f-2633-47f2-a1ec-4a47b250501d/image.png)

리뷰어를 걸려고 하면 팀원 깃헙 닉네임도 다 알아야되고, 4명을 하나씩 검색해서 넣어야했다.

너무 비효율적이라고 생각해서 깃헙 팀안에 세부팀을 만들 수 있는걸로 알아서 따로 요청 드렸다.
![](https://velog.velcdn.com/images/naminhyeok/post/38e6832b-a183-4ce8-b49c-d10e18b1a6cf/image.png)

아주 다행스럽게도 요청하신걸 잘 부탁들어주셔서 팀으로 이용 할 수 있게 되었고 이제야 좀 편안해졌다..

![](https://velog.velcdn.com/images/naminhyeok/post/91b8fb02-a0cb-4659-bd30-8b03dcbd1b74/image.png)

PR을 날릴 때 리뷰어를 찾는데 시간을 5초만 줄여도 PR을 100개 날리면(그럴리가 없을 것 같지만) 8분30초를 아낀다. 이걸 10팀이 만약 다 하면 무려 83분을 아낀다.(물론 말도안되는 비유다)

하여튼 이렇게 편하게 할 수 있는걸 다른 팀들도 혹시라도 모를까봐 공유해두었다.
![](https://velog.velcdn.com/images/naminhyeok/post/00c3f802-24e8-4271-8a87-00313c74e896/image.png)

지금 몰래보니까 쓰는팀도 계시고 안쓰는팀들도 계신다. 굳이 안쓰신다면야 뭐..😅


## 일정관리

내가 팀장은 아니지만.. 나는 일정관리를 해주는 많은 툴이 있는데 노션으로 일정관리를 해야한다는게 굉장히 불편했다. 어차피 issue 만들어서 진행하고, PR만들고 리뷰하고 하는데 이걸 굳이 노션으로 또 해야된다고? 라고 생각하니 정말정말 싫었다. 그게 싫으면 내가 직접 뭐라도 좋은 방법을 가져가야지 별 수 있나

그래서 Github Projects를 이용하기로 했다.
과거에 한번 써보려고 했는데 음.. 정말 제대로 못썼다고 생각한다. 아무래도 대부분 팀장이라고 해봤자 이름이 팀장이지 전체적인 흐름을 보고 필요한 업무를 정리해두고 할 수 있는 사람은 없었기 때문에 있으나 마나였다.

![](https://velog.velcdn.com/images/naminhyeok/post/dac3cba2-1347-4f3d-a2b1-3c2104a3ba42/image.png)

그냥 기본 칸반을 이용하도록 했다. 이정도면 그래도 정리되어 보이고 좋다고 생각한다. 내가 아는 애자일이라면 백로그에 작업들이 있고 그걸 팀원들이 가져가는 식이겠지만, 우리는 각자 할일을 만들기도 바쁘기 때문에 나는 내가 할일을 미리 정리해서 칸반에 넣어두었다.

![](https://velog.velcdn.com/images/naminhyeok/post/2817fd10-d0e6-48fc-9bd7-7847484fc80c/image.png)

뭐 대충.. 작업 일정도 이렇게 보면 기분은 좋지 않나.....

근데 Github Projects를 사용해봤는데 생각보다 설정이 쉽지 않았다 내 마음대로 커스터마이징이 잘 되지도 않고, 생각보다 자동화가 덜되는 느낌이다. 내가 설정을 잘 못해서 그런가 PR이 열리면 In Review로 갔으면 좋겠는데 이것도 안되고, 백로그에 있는 것을 Convert Issue를 하면 이슈 템플릿을 선택해서 만드는 것도 아닌 백로그에 적은 내용을 제목으로 이슈가 만들어진다. 그리고 이슈에서 브랜치를 만들면 In Progress로 가고싶은데 그것도 안된다.

그러니까 정리하자면 이렇게 자동화가 되면 좋겠다.
1. 백로그에 할 일 정리 후 이슈로 전환 시 템플릿 정해서 이슈 작성
2. 작성된 이슈는 Ready상태로 변경
3. 이슈에서 Create Branch로 이슈와 연결된 브랜치 만들 시 In Progress로 이동
4. Issue에 대한 PR이 만들어지면 In Review로 진입
5. PR이 모두 Approve되면 머지 대기중과 같은 칸반을 하나 더 만들어서 이동
6. PR이 닫히면 Done으로 이동

뭐 Jira에서는 되는지 모르겠다. 실제로 Jira를 많이 쓴다고 하니 나중에 기회되면 Jira도 써보고 싶다. 예전에 쓰려다가 제대로 못썼던 경험이 있어서 아쉬웠다.

## 문서화

아무래도 팀원들이 문서화를 잘한다.. 팀원들이 노션도 잘 꾸며주고 해서 대부분의 문서는 노션으로 다 만들고 있다 앞서 작성했던 요구사항 명세나, Git 컨벤션, Code 컨벤션 등등

![](https://velog.velcdn.com/images/naminhyeok/post/bb12aa01-22fb-4f01-be18-d1dd671fccc1/image.png)

여기 보여지는거 말고도 상세하게 들어가면 엄청 잘되어있는데.. 어떻게 캡처로는 다 보여줄 방법이 없다. 하여튼 잘꾸며주시고 문서화 잘해주시는 팀원들 감사하다.

# 구현
1주차는 구현을 많이 하지는 못했다. 그래도 팀원들이랑 같이 엔티티 작업하고 진행해서 새로운 경험도 많이 해보고 좋았다.

## 엔티티 작성
엔티티 작성같은 경우는 난 한명이 해야한다고 생각했다. 왜냐면 분명히 충돌이 많이 날거라고 생각했다. 그래서 누군가 자신있는 사람이 작성하거나, 화면공유를 통해서 같이 봐야된다고 생각했다. 근데 인텔리제이에서 엄청 좋은 기능이 있었다.

### Code With me로 동시 작업하기
![](https://velog.velcdn.com/images/naminhyeok/post/7607243a-e9f8-4751-ac82-5a20a63f1f7d/image.png)
인텔리 제이의 우측 상단에 돋보기 모양 왼쪽 사람+ 모양을 누르면 사람들을 내 intellij에 초대할 수 있었다. 마치 노션이나, 클라우드 docs들 처럼 사용 할 수 있게 되었다.

그래서 이걸 통해서 사람들과 엔티티를 작성했다. 근데 이게 내가 사람들을 초대했는데 사람들이 생성자 만들기 단축키나, 리팩토링 같은 단축키가 안먹힌다고 했다. 그리고 조금 버벅이기도 했다. 아마도 내가 초대받는 입장이였으면 답답해서 한번쯤은 한숨을 쉬었을지도...ㅎㅎ

### Soft Delete 사용해보기
실제로 현업에서는 데이터를 삭제하는 경우는 거의없다고 한다. 사용자가 데이터 삭제를 요청하거나, 무슨무슨법에 의해 삭제한다거나 뭐 하여튼 그런 경우아니면 데이터를 보존한다고 한다. 그래서 나도 이번 프로젝트에서는 실제로 삭제하지 않고 사용해보려고 한다.

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private IsDeleted isDeleted = IsDeleted.FALSE;
    
}
```

삭제여부를 추가해서 사용하도록 하였다. 사실 여기까지는 뻔하다. 아 그리고 이 부분에서 boolean 써도 되는거 아니냐는 이야기도 있었는데, 개발에 정답은 없듯이 나는 지금 상황에선 Enum이 낫다고 생각했다.
MySQL과 H2만 사용 할 것이고 다른 DB로 넘어갈 일은 없다고 생각해서 Enum을 사용했다.

MySQL에서 boolean은 사실상 boolean이 아니기때문에.. 난 그게 싫었다. 아래 글을 참고해보면 좋을 것 같다.

[MySQL BOOLEAN 컬럼](https://medium.com/daangn/mysql-boolean-%EC%BB%AC%EB%9F%BC-7abd9b35c664)

Enum이냐 Boolean이냐는 정답은 없으니 프로젝트에 알맞은 선택을 내리면 된다고 생각한다. 난 enum이 더 낫다고 생각했을 뿐 둘의 차이를 아는게 더 중요하다고 생각한다.

그리고 soft delete를 사용했기 때문에 조회나 삭제 할 때 삭제되었는지 확인하거나, 삭제가 아닌 Update쿼리를 날리는 등 다른 처리가 필요했다.

이럴 때를 위해서 `@SQLDelete`나 `@Where`를 통해서 조금 더 편리하게 사용 할 수 있었다.

그 때 인프런의 질문중 하나를 발견했고 지양하는쪽으로 가는게 좋겠다고 생각했다.

[baseEntity와 softDelete 질문](https://www.inflearn.com/community/questions/304378/baseentity%EC%99%80-softdelete-%EC%A7%88%EB%AC%B8)

하지만 이 때 팀원이 "실제로 삭제해줄 필요는 없지 않을까요? `@SqlDelete`는 써도 될 것 같아요" 라고 해주셔서 다시 한번 생각해봤는데 실제 서비스의 경우는 회원의 요청이나, 법과 같은 부분에 따라 삭제해야 할 수 있지만 우리는 그런 경우는 없기 때문에 편의성을 위해 `@SqlDelete`는 사용하며 가기로 했다.

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE vote SET is_deleted = 'TRUE' WHERE vote_id = ?")
@Entity
public class Vote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @NotNull
    private Long memberId;

    @NotNull
    private Long teamId;

    @NotNull
    @Column(length = 50)
    private String title;

    @NotNull
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "vote")
    private List<VoteItem> voteItems = new ArrayList<>();

    @Builder
    private Vote(Long memberId, Long teamId, String title, LocalDateTime endAt) {
        this.memberId = memberId;
        this.teamId = teamId;
        this.title = title;
        this.endAt = endAt;
    }

    public static Vote create(Long memberId, Long teamId, String title, LocalDateTime endAt) {
        validateEndAt(endAt);
        return Vote.builder()
            .memberId(memberId)
            .teamId(teamId)
            .title(title)
            .endAt(endAt)
            .build();
    }

    private static void validateEndAt(LocalDateTime endAt) {
        if (endAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("투표 종료일은 현재 시간 이후여야 합니다.");
        }
    }

    public void addChoice(VoteItem voteItem) {
        this.voteItems.add(voteItem);
    }

}
```

위에서 부터 읽어왔으면 양방향 연관관계 지양하자해놓고 양방향썼네 라고 생각할수도 있는데 투표항목이랑 투표는 완전히 라이프사이클 똑같다고 생각해서 사용했다.

### @Column(nullable = false) 대신 @NotNull 써보기

다른 사람의 코드를 볼 일이 있었는데 그 분께서 엔티티에 `@NotNull`을 사용하고 계셨다. 나는 `@NotNull`과 같은 validate는 당연히 엔티티에서는 사용 못할 줄 알았다. 그래서 나는 그때 **"왜 `@Column(nullable = false)` 대신 `@NotNull`써요 ? `@NotNull`은 안되지않나요 ? `@Column(nullable = false)` 쓰는게 더 좋을 것 같아요 일반적으로 많이 쓰기도 하구요"** 라고 말했다.

아주 무지한 발언이었다. 알아보지도 않고 그냥 또 관성적으로 쓰니까 그리고 일반적으로 `@Valid`와 함께
써야하니까 안될거라고 생각했다. 그러고 나서 나한테 `@NotNull`도 똑같은 기능을 한대요 그리고 더 좋대요 라고하셨다. 그래서 나도 찾아봤다.

[[JPA] nullable=false와 @NotNull 비교, Hibernate Validation](https://kafcamus.tistory.com/15)

이 글을 보고 살짝 충격을 먹었다고 해야될까..? DB에 직접 가기전에 어플리케이션단에서 체크를 해주는게 당연히 더 좋은거 아닌가? 그래서 나는 앞으로 가능하다면 사람들에게 이 정보를 알리고 `@NotNull`을 한번 사용해보도록 하기로 했다.

```java
@Entity
public class Vote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @NotNull
    private Long memberId;

    @NotNull
    private Long teamId;

    @NotNull
    @Column(length = 50)
    private String title;

    @NotNull
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "vote")
    private List<VoteItem> voteItems = new ArrayList<>();

    @Builder
    private Vote(Long memberId, Long teamId, String title, LocalDateTime endAt) {
        this.memberId = memberId;
        this.teamId = teamId;
        this.title = title;
        this.endAt = endAt;
    }

    public static Vote create(Long memberId, Long teamId, String title, LocalDateTime endAt) {
        validateEndAt(endAt);
        return Vote.builder()
            .memberId(memberId)
            .teamId(teamId)
            .title(title)
            .endAt(endAt)
            .build();
    }

    private static void validateEndAt(LocalDateTime endAt) {
        if (endAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("투표 종료일은 현재 시간 이후여야 합니다.");
        }
    }

    public void addChoice(VoteItem voteItem) {
        this.voteItems.add(voteItem);
    }

}
```

위 코드를 다시 가져왔는데 다 @NotNull인 것을 확인 할 수 있다.

![](https://velog.velcdn.com/images/naminhyeok/post/57f9f926-6e3e-4a30-9ffc-6821d3e6f95d/image.png)

실제로 데이터베이스에도 NotNull이 잘 먹힌걸 확인 할 수 있다.

### SingleTable 전략 사용하기

JPA 관련해서 공부하면서 봤는데 실무에서는 자주 쓰인다고 한다. (알려주신분 배민다니신다.. 신뢰할 수 있지 않을까 ?) 안쓰이면 아쉬운거고.. 그래도 있는 기능이니 한번 사용해보았다.

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Entity
public class VoteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    public VoteItem(Vote vote) {
        this.vote = vote;
        vote.addChoice(this);
    }

}
```

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class VoteItemDate extends VoteItem {

    private LocalDateTime time;

    @Builder
    private VoteItemDate(Vote vote, LocalDateTime time) {
        super(vote);
        this.time = time;
    }

    public static VoteItemDate create(Vote vote, LocalDateTime time) {
        return VoteItemDate.builder()
            .vote(vote)
            .time(time)
            .build();
    }
}
```
```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class VoteItemLocate extends VoteItem {

    private Long stadiumId;

    @Builder
    private VoteItemLocate(Vote vote, Long stadiumId) {
        super(vote);
        this.stadiumId = stadiumId;
    }

    public static VoteItemLocate create(Vote vote, Long stadiumId) {
        return VoteItemLocate.builder()
            .vote(vote)
            .stadiumId(stadiumId)
            .build();
    }

}
```

나는 투표 항목을 만들어야 할 때 구장 또는 일정을 투표하는 투표 2가지를 만들어야 했다. 그러니까 투표의 타입이 2가지인것이다. 그래서 이걸 JPA 공부하면서 얼핏 들었던 싱글테이블 전략을 쓰면 좋지 않을까? 라는 생각을 했다.
예시에서 나왔던 것도 잘 기억은 안나지만.. Item에서 price나 name같은 것을 공통되게 쓰지만 상세한 book이나, movie와 같이 컬럼이 달라지는 경우에 썼던 걸로 기억한다. 그래서 이런 경우에 써볼 수 있지 않을까? 라는 생각에서 출발했다.
**@Embaddable, 고급매핑 사용하여 ORM스럽게 사용해보기** 이러한 목표도 있었기 때문에 그냥 써보고 싶었다.

![](https://velog.velcdn.com/images/naminhyeok/post/9bc7ab67-4e51-4a15-984a-e51209aa980b/image.png)

이런식으로 내가 생각한대로 잘 만들어졌다. 솔직히 말해서 TIME이라는 컬럼명이 별로라고는 생각하지만 투표 항목에 들어갈 시간을 뭐라고 해야될 지 모르겠다.

지나고 지금 살펴보면 뭔가 단일 테이블 전략을 사용할 때가 아닌가? 싶기도 한데 그냥 뭐 경험해봤다... 근데 단일 테이블 전략을 쓰니까 안익숙해서 그런가 생각보다 구현할 때 조금 불편하다. 이건 내 실력부족이라고 생각하니까 이번에 사용해본 김에 열심히 한번 해보자

### @Embedded, @Embeddable 열심히 사용하기
사용 할 수 있다면 @Embedded, @Embeddable 사용해서 객체지향적으로 하려고 했다.

```java
@Embeddable
public class LoginType {

    @Enumerated(EnumType.STRING)
    private LoginProvider loginProvider;

    @Column(nullable = false)
    private String snsId;

    @Builder
    private LoginType(LoginProvider loginProvider, String snsId) {
        this.loginProvider = loginProvider;
        this.snsId = snsId;
    }
}
```

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Position {

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Builder
    private Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
```
실제로는 이 안에 검증이 안들어가있지만..? 내가 구현한게 아니라 검증을 추가를 안하셨나보다. 필요한 부분이 있으면 검증을 추가해달라고 하면 좋을 것 같다.

하여튼 이렇게 position같은 경우도 이렇게 객체로 따로 분리해서 사용하는게 좋다고 생각해서 최대한 사용해보려고 노력했다.

### FK 사용안해보기
```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choiceId;

    private Long memberId;

    private Long voteItemId;

    @Builder
    private Choice(Long memberId, Long voteItemId) {
        this.memberId = memberId;
        this.voteItemId = voteItemId;
    }

    public static Choice create(Long memberId, Long voteItemId) {
        return Choice.builder()
            .memberId(memberId)
            .voteItemId(voteItemId)
            .build();
    }

}
```

소심하게 나마 내가 사용하는 엔티티에서 FK를 안걸고 사용하기로 했다. 과거에 이렇게 했던 경험도 있기도 하고 위에서도 많이 말했지만 FK안쓰는게 좋다고 하니까 이렇게라도 살짝 맛보기 해봤다.

회고하는 지금 생각해보면 memberId나 voteItemId에는 인덱스를 걸어서 사용해야되나? 라는 생각이 든다.
왜냐면 기본적으로 FK를 걸면 인덱스로 지정이되는데 지금은 그렇진 않기 때문에 FK와 비슷한 성능을 내려면 인덱스를 써야 하지않을까?

내가 인덱스에 관해서 잘 몰라서 이건 추가로 공부해보고 넣을 예정이다.

그리고 choice는 왜 baseEntity를 상속안받았냐 하면 투표한 시간은 필요 없다고 생각했고, 투표는 수정이 불가하고 취소하고 재투표로 구현 할 것이라 수정 시간은 필요 없다고 생각했다. 그리고 마지막으로 삭제여부는 앞선 수정의 이유와 동일하게 아예 삭제하고 재투표로 구현 할 것이라 상속받을 필요가 없다고 생각했다.
## JPA 좀 더 잘쓰기
### 단건 조회 vs IN 그리고 save vs saveAll

과거에 이런 리뷰를 남겼던 적이 있다. 그리고 글에 남겨뒀던적도 있다. 제목에서는 내용이 없을 것 같지만 바로앞에 나온다.
[테스트에서 @Transactional을 이용한 롤백](https://velog.io/@naminhyeok/%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%97%90%EC%84%9C-Transactional%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%A1%A4%EB%B0%B1)

![](https://velog.velcdn.com/images/naminhyeok/post/6481f8c7-01b9-47fe-8cc2-9100ba740dad/image.png)

물론 이건 1차 프로젝트 때지만 그래서 jpa에서 기본적으로 제공해주는 메서드들 물론 좋지만 알고 사용해야된다고 생각했고 그러다 보니까 항상 내가 짜는 코드에 의심이 생겼다.

```java
for (Long stadiumId : request.choices()) {
    VoteItemLocate voteItemLocate = VoteItemLocate.create(savedVote, stadiumId);
    VoteItemLocate savedVoteItem = voteItemRepository.save(voteItemLocate);

    String stadiumName = stadiumRepository.findStadiumNameByStadiumId(savedVoteItem.getStadiumId());

}
```
반복문을 통해서 단건으로 save하고 find 쿼리를 날리고 있다. 물론 당연히 단건으로 쿼리가 여러개 날라가는건 안좋은건 알고 있다. 과거에 경험했던 것이기도 하고, IN절을 사용해야하는것도 알고 있었다.

그래서 리팩토링을 하기로 마음먹었다.
```java
List<VoteItemLocate> voteItemLocates = request.choices().stream()
    .map(stadiumId -> VoteItemLocate.create(savedVote, stadiumId))
    .toList();

List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);

List<String> stadiumNames = stadiumRepository.findStadiumNamesByStadiumIds(savedVoteItems.stream()
    .map(VoteItemLocate::getStadiumId)
    .toList()
);
```
지금보면 굉장히 더럽지만 다행스럽게도 리팩토링을 하긴했다. 그 당시 기록해두었던걸 가져와서 이렇다.. 보는 사람이 있다면 양해바란다.

근데 나는 뭔가 IN절을 이용하는 것 말고도 위에 리팩토링 하기전 코드가 save를 반복문으로 하는게 더 느린거 아니야 ? 라고 생각했다. 'insert가 매번마다 일어나니까 느려지는거아닐까?' 라고 생각했고 팀원에게 물어보니 성능상 차이는 없을거라고 했다. saveAll도 insert는 하나씩 해줄거니까 별 차이 없을거라고 얘기했다.

그래서 한번 save와 saveAll을 delete와 deleteAllInBatch처럼 살펴봤다.

![](https://velog.velcdn.com/images/naminhyeok/post/0db3ae44-23d8-46bd-8df0-3130dbf60caf/image.png)

그래서 막상 들어가보면 팀원 말대로 반복문으로 똑같이 넣고 있었다... 그래서 궁금해서 검색해봤다.

[[Spring Data JPA] save와 saveAll의 성능 차이에 대한 실험과 결과!(스프링 프록시, @Transactional)](https://sas-study.tistory.com/388)
[[JPA] save()와 saveAll()의 성능 차이](https://velog.io/@sudhdkso/JPA-save%EC%99%80-saveAll%EC%9D%98-%EC%84%B1%EB%8A%A5-%EC%B0%A8%EC%9D%B4)

> 일단 this. 키워드로 접근했다는 것과 과연 저 호출이 save 메소드의 @Transactional 프록시를 동작하게 하였는가를 알아야 합니다.
스프링의 빈은 대부분 프록시 객체로 채워져있습니다. @Transactional 어노테이션은 이러한 프록시 객체의 참조로 실행되어야 제 역할을 하게 되는데요. this 키워드로 호출한 경우 단지 내부 메소드를 호출한 효과를 주게되어 프록시 참조를 통한 호출이 아니게 되는 것입니다.


> save()와 saveAll()모두 @Transactional 어노테이션이 달린 것을 볼 수 있습니다 .그리고 기본 트랜잭션 propagation 타입은 REQUIRED입니다. REQUIRED는 트랜잭션이 존재하지 않으면 새로운 트랜잭션을 생성하고, 이미 존재하면 그 트랜잭션에 참여하는 유형입니다.
그리고 @Transactional 의 경우 AOP 프록시 기반으로, 외부 Bean 객체가 있고, 이 객체의 함수를 호출해야 Intercept가 되어 트랜잭션으로 묶이게 됩니다. 따라서 save()를 여러번 호출하는 경우는 계속 기존의 트랜잭션이 존재하는 지 계속 확인해줘야 하기 때문에 추가로 리소스가 소모됩니다. 하지만 saveAll()의 경우는 내부에서 save()를 호출하기 때문에 saveAll()을 할 때 트랜잭션이 생성되어 하나의 트랜잭션으로 작동하게 됩니다. 그래서 save()를 여러번 하는 것보다 성능이 더 좋은 것입니다.

이렇게 블로그에 기술되어있다.

아무래도 `@Trnasactional` 그리고 스프링 프록시, AOP 다 개념이 부족한가보다... 정확하게 이해는 못했지만 반복문을 돌면서 save를 하면 save는 트랜잭션이 존재하는지 계속 확인하지만 saveAll로 처리하면 한번만 호출하기 때문에 성능이 더 좋다는 의미인 것 같다.

그러면 나의 경우는 어떨까? 사실 내 코드는 트랜잭션이 저때 열리는게 아니긴하다. 그렇다면 첫번째 인용문을 다시 한번 읽어보면 this 키워드로 호출한 경우 프록시 참조를 통한 호출이 아니게 된다고 한다.
그러니까 다시 이해해보자면 트랜잭션이 열려 있더라도 save는 기존의 Transactional을 프록시 참조하기 위해 계속 호출하게 되지만 saveAll은 첫 한번만 호출한다는 뜻 아닐까?

이부분도 전부 다 이해한건 아니라서 제대로 알아볼 필요가 있는 것 같다. 하여튼 기존 코드를 수정해서 전체 코드는 아래와 같이 되었다.

```java
@Transactional
@Override
public VoteResponse createStadiumVote(VoteStadiumCreateServiceRequest request, Long teamId, String email) {
    Long memberId = getMemberId(email);
    validateTeamId(teamId);
    List<Long> stadiumIds = request.stadiumIds();
    validateStadiumIds(stadiumIds);

    Vote vote = Vote.create(memberId, teamId, request.title(), request.endAt());
    Vote savedVote = voteRepository.save(vote);

    List<VoteItemLocate> voteItemLocates = stadiumIds.stream()
        .map(stadiumId -> VoteItemLocate.create(savedVote, stadiumId))
        .toList();

    List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);

    List<String> stadiumNames = getStadiumNames(savedVoteItems);

    List<VoteItemResponse> voteItemResponse = getVoteItemLocateResponse(savedVoteItems, stadiumNames);
    return VoteResponse.of(vote, voteItemResponse);
}
```

내 나름의 깔끔한 코드를 만들려고 노력해봤는데 생각보다 잘안된것 같긴하지만.. 하여튼 saveAll을 쓰는 식으로 갔다.

### QueryDsl 사용해보기

```java
@Override
public Long findMemberIdByMemberEmail(String email) {
    return queryFactory.select(member.memberId)
        .from(member)
        .where(member.email.eq(email)
            .and(member.isDeleted.eq(IsDeleted.FALSE)))
        .fetchOne();
}
```

그냥 별건 아니지만 사용해보려고 노력하는 중에 있다. 앞으로는 좀 더 정교한 쿼리랑 성능까지 고려할 수 있는 쿼리를 짜는걸 노력해보도록 지금은 알아가는 중이라 하는 중이다~ 정도이다.

## Record 사용해보기

1차 프로젝트 때 record를 써본다는걸 깜빡하고 습관적으로 class로 만들어서 사용했다.
아무래도 cmd+n으로 생성할때 class가 제일 위에있으니까.. 습관이 되어버렸다. 그래서 이번에는 팀차원에서 저희 다 Record로 작성해봐요! 하고 record로 만들었다.

![](https://velog.velcdn.com/images/naminhyeok/post/67a8029b-0840-43f8-91fc-03dcfd42b95a/image.png)

```java
public record VoteDateCreateRequest(
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 50, message = "제목은 50자 이하여야 합니다.")
    String title,
    @Future(message = "투표 종료 시간은 현재 시간보다 미래의 시간으로 지정해야합니다.")
    LocalDateTime endAt,
    @Size(min = 1, message = "일정 선택은 필수입니다.")
    List<DateChoices> choices
) {

    public VoteDateCreateServiceRequest toServiceRequest() {
        return new VoteDateCreateServiceRequest(
            title,
            endAt,
            choices.stream()
                .map(DateChoices::choice)
                .toList()
        );
    }
}
```

평범하게 record를 사용했다. 뭐 특별히 한건 없지만 record는 AllArgsConstructor가 포함되어있으니 생성자를 만들 때 컴팩트 생성자를 만들어야 된다고 한다. 이부분은 더 공부를 해봐야되는 부분이다. 실제로 사용해볼 일이 드물었다. 단순히 데이터를 주고받다보니 컴팩트생성자를 이용해야될 상황이 잘없었다...

이 부분도 공부해보도록 해야겠다.

# 트러블 슈팅
## 문제의 시큐리티
시큐리티 설정이 항상 나를 괴롭힌다 테스트에서도 그렇고 이곳 저곳에서 문제를 일으킨다. 근데 그냥 아마도 내가 시큐리티를 잘 몰라서 그런것 같다.

### h2-console 오류
![](https://velog.velcdn.com/images/naminhyeok/post/536462d0-00ba-4ad6-ba59-5e544ca02c19/image.png)

음...? 근데 딱봐도 시큐리티 문제였다. 이런경우는 맨날 시큐리티다.

SpringSecurity의 기능 중 클릭재킹(사용자가 누르려는 버튼 위에 투명한 버튼이나 가짜 버튼을 만들어 다른 링크로 이동하게끔 만드는 해킹 기술)을 방지하기 위해 사용되는 X-Frame-Options 이라는 옵션이 존재한다고 한다..

X-Frame-Options 헤더는 웹 페이지가 다른 웹 페이지의 '프레임'으로 로드 되는 것을 제어하는데 사용되는 것으로, H2 콘솔 또한 다른 웹 페이지에 '프레임'으로 포함되어 사용되기 때문에 로드를 막은 것이다.

시큐리티는... 공부열심히해야겠다..

[H2-console에서 Localhost에서 연결을 거부했습니다. 오류 발생시 해결법](https://devpad.tistory.com/112)
[[BackOffice Project] SpringSecurity 사용 중 H2 console localhost 연결 거부 TroubleShooting](https://velog.io/@jangcoding/BackOffice-Project-SpringSecurity-%EC%82%AC%EC%9A%A9-%EC%A4%91-H2-console-localhost-%EC%97%B0%EA%B2%B0-%EA%B1%B0%EB%B6%80-TroubleShooting)
[Spring Security 마이그레이션 중 발생한 에러 해결](https://findthelostedhobby.tistory.com/164)

위의 소중한 글들 때문에 해결했다. 시큐리티는 뭐가 이렇게 빠르게 바뀌는지 자꾸 deprecate됐다고 못쓴다고 한다.. 하여튼 나의 경우는 이렇게 해결했다.

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement((sessionManagement) -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
            .requestMatchers("/**").permitAll()
        )
        // 이곳부터 headers에 추가
        .headers((headerConfig)->
            headerConfig.frameOptions((HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
        )
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
}
```


### Docs는 통과하지만 Api는 통과못한다?

![](https://velog.velcdn.com/images/naminhyeok/post/04acbfcb-6538-408f-850c-0334defb7a01/image.png)

과거에 사용하던 대로 컨트롤러단 테스트는 WebMvcTest로 진행했다. 그리고 예전과 같이 당연히 시큐리티 설정에서 오류가 났으니 `@MockWithUser` 어노테이션을 추가하고 `with(csrf())`를 하면 403, 401이 풀리면서 200이 되겠구나 ! 라고 생각했다.

```java
@DisplayName("새로운 구장 투표를 생성한다.")
    @WithMockUser
    @Test
    void createLocateVote() throws Exception {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        StadiumChoices stadiumChoices1 = new StadiumChoices(1L);
        StadiumChoices stadiumChoices2 = new StadiumChoices(2L);
        VoteStadiumCreateRequest request = new VoteStadiumCreateRequest("연말 행사 투표", endAt, List.of(stadiumChoices1, stadiumChoices2));


        VoteResponse response = new VoteResponse(
            1L,
            "연말 행사 투표",
            endAt,
            List.of(new VoteItemResponse(1L, "최강 풋살장", 0L),
                new VoteItemResponse(2L, "열정 풋살장", 0L)
            )
        );

        given(voteService.createStadiumVote(any(VoteStadiumCreateServiceRequest.class), eq(1L), any(String.class)))
            .willReturn(response);

        mockMvc.perform(post("/api/v1/votes/stadiums/{teamId}", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.voteId").value(1L))
            .andExpect(jsonPath("$.data.title").value("연말 행사 투표"))
            .andExpect(jsonPath("$.data.endAt").value(endAt.toString()))
            .andExpect(jsonPath("$.data.choices").isArray())
            .andExpect(jsonPath("$.data.choices[0].voteItemId").value(1L))
            .andExpect(jsonPath("$.data.choices[0].content").value("최강 풋살장"))
            .andExpect(jsonPath("$.data.choices[0].voteCount").value(0L))
            .andExpect(jsonPath("$.data.choices[1].voteItemId").value(2L))
            .andExpect(jsonPath("$.data.choices[1].content").value("열정 풋살장"))
            .andExpect(jsonPath("$.data.choices[1].voteCount").value(0L));

    }

    @DisplayName("새로운 구장 투표를 생성 할 때 제목은 필수이다.")
    @WithMockUser
    @Test
    void createLocateVoteWhenTitleIsNotExistThenThrowException() throws Exception {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        StadiumChoices stadiumChoices1 = new StadiumChoices(1L);
        StadiumChoices stadiumChoices2 = new StadiumChoices(2L);
        VoteStadiumCreateRequest request = new VoteStadiumCreateRequest(null, endAt, List.of(stadiumChoices1, stadiumChoices2));

        given(voteService.createStadiumVote(any(VoteStadiumCreateServiceRequest.class), eq(1L), any(String.class)))
            .willReturn(new VoteResponse(
                1L,
                "연말 행사 투표",
                endAt,
                List.of(new VoteItemResponse(1L, "최강 풋살장", 0L),
                    new VoteItemResponse(2L, "열정 풋살장", 0L)
                )
            ));

        mockMvc.perform(post("/api/v1/votes/stadiums/{teamId}", 1L).with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("제목은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());

    }
```
딱 두개정도만 보자면 하나는 정상적인 경우, 하나는 예외가 발생하는 경우를 테스트하고 있다. 그리고 테스트 결과가 어떻게 되는지 한번 확인해보자

![](https://velog.velcdn.com/images/naminhyeok/post/d8f3c253-7272-47d8-8ba9-06c12be8be29/image.png)

위에가 조금 짤렸는데 request도 잘가는 것 같다. 그런데 response를 보니까 Body에 아무값도 오지 않는다 ! Status가 200이 뜨고 아무 값도 오지않는다.

예외를 발생하는 테스트도 살펴보자

![](https://velog.velcdn.com/images/naminhyeok/post/d0fee283-a1a9-4002-94e8-22d934566e8a/image.png)

놀랍게도 예외도 발생안하고 200이 뜬다. 그리고 데이터는 당연히 아무것도 없다. 이게 뭘까..?

근데 신기한건 Docs는 통과한다는 것이다.

```java
@DisplayName("구장 투표를 등록하는 API")
    @Test
    void createStadiumVote() throws Exception {
        //given
        LocalDateTime endAt = LocalDateTime.now().plusDays(1);
        StadiumChoices stadiumChoices1 = new StadiumChoices(1L);
        StadiumChoices stadiumChoices2 = new StadiumChoices(2L);
        VoteStadiumCreateRequest request = new VoteStadiumCreateRequest("연말 행사 투표", endAt, List.of(stadiumChoices1, stadiumChoices2));

        given(voteService.createStadiumVote(any(VoteStadiumCreateServiceRequest.class), eq(1L), any(String.class)))
            .willReturn(new VoteResponse(
                1L,
                "연말 행사 투표",
                endAt,
                List.of(new VoteItemResponse(1L, "최강 풋살장", 0L),
                    new VoteItemResponse(2L, "열정 풋살장", 0L)
                )
            ));

        mockMvc.perform(post("/api/v1/votes/stadiums/{teamId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("vote-stadium-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("title").description("투표 제목"),
                    fieldWithPath("endAt").description("투표 종료 시간"),
                    fieldWithPath("choices").description("투표 선택지 목록"),
                    fieldWithPath("choices[].stadiumId").description("구장 ID")
                ),
                pathParameters(
                    parameterWithName("teamId").description("팀 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.voteId").type(JsonFieldType.NUMBER)
                        .description("투표 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("투표 제목"),
                    fieldWithPath("data.endAt").type(JsonFieldType.ARRAY)
                        .description("투표 종료 시간"),
                    fieldWithPath("data.choices").type(JsonFieldType.ARRAY)
                        .description("투표 선택지 목록"),
                    fieldWithPath("data.choices[].voteItemId").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 ID"),
                    fieldWithPath("data.choices[].content").type(JsonFieldType.STRING)
                        .description("투표 선택지 내용"),
                    fieldWithPath("data.choices[].voteCount").type(JsonFieldType.NUMBER)
                        .description("투표 선택지 투표 수")
                )
            ));
    }
```
docs에서 테스트하는 코드이다.
다른거라고는 테스트환경이 둘이 다르다. 그리고 docs는 perform의 import가 RestDocs이다. 무슨 문제인지는 더 공부해봐야되겠다.

그래서 이건 아직도 해결하지 못해서 Api Test에 대해서는 Disabled하고 진행하고 있다.


![](https://velog.velcdn.com/images/naminhyeok/post/aec781f1-55d4-4d4d-8e09-4ec42e8e871e/image.png)

시큐리티 관련 설정 문제는 아닌것 같다. 왜냐하면 Docs는 잘 통과하기도 하고, 실제로 포스트맨으로 하면 잘된다.

아마도 WebMvcTest 문제일 것으로 보이는데 이거는 내가 테스트에 대해서 더 공부해보고 따로 포스팅을 하는게 좋을 것 같다.

## Embedded Redis

embedded redis를 사용하면 실제 레디스를 실행환경이나 테스트 환경에서 레디스를 설치하지 않아도 되는 장점이 있다고 한다.

근데 ozimov는 arm 맥북에서 동작하지 않는다고 한다. 2020년쯤 마지막 커밋이니 당연히 안될거같다..

이건 블로그 많이 찾아보면 나오는 내용이긴 한데 나의 경우는 바이너리 파일이 무슨뜻인지 몰라서 당황했었다.

[Spring - Mac M1(ARM)에서 Embedded Redis를 실행하지 못하는 이유와 해결 방법](https://green-bin.tistory.com/78#EmbeddedRedisConfig-1)

이 블로그를 보고 하면 되는데 하다가 나는 바이너리 파일이 뭔지를 몰랐다.. 그리고 뭔지 제대로 안알려준다. 그냥 바이너리 파일을 이름을 바꿔서 하란다. 팀원들의 도움을 통해서 바이너리 파일을 찾아서 옮겼다.

`압축을 해제한 레디스 폴더/src/redis-server` 이다. redis-server가 바이너리 파일이니 이걸 옮겨서 사용하면 된다.

```java
@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.data.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() {
        if(isArmMac()) {
            redisServer = new RedisServer(getRedisServerExecutable(), redisPort);
        } else {
            redisServer = new RedisServer(redisPort);
            redisServer.start();
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    private File getRedisServerExecutable(){
        try {
            return new File("src/main/resources/binary/redis/redis-server-6.2.5-mac-arm64");
        } catch (Exception e) {
            throw new NoSuchElementException("Redis Server Executable File Not Found");
        }
    }
    private boolean isArmMac() {
        return Objects.equals(System.getProperty("os.arch"), "aarch64")
            && Objects.equals(System.getProperty("os.name"), "Mac OS X");
    }
}
```
config 파일을 이렇게 수정해서 사용하니까 아마도? 로컬 레디스를 사용하지 않는 것 같다. 근데 문제는 테스트 상황에서 applicationtest만 통과를 못한다.

![](https://velog.velcdn.com/images/naminhyeok/post/360de31e-5242-41ca-aebd-61ba8a40fef7/image.png)

도대체 이건 왜그런걸까? config 파일에 test가 없어서 그럴까? 그래서 test를 넣고 돌리면 테스트가 전부 다 깨진다. 하하 이것도 공부를 더 해봐야되지않을까 생각한다.
테스트하는거 너무 좋은데 테스트는 나를 너무 골치아프게 한다. 이러한 과정들이 나중에 쌓여서 테스트를 잘하게 되면 좋겠다.
일단 이대로 냅둬도 다른 테스트는 안깨져서 여유 될 때 한번 확인해보고 실제 프로덕션 코드에 문제를 발생시키는건 아니니까 이대로 넘어가려고 한다.

## Request에서 Custom Valid 어떻게 사용할까?

이부분은 블로그에 글을 작성했다.
[RequestDTO 커스텀 검증 왜 안될까?](https://velog.io/@naminhyeok/RequestDTO-%EC%BB%A4%EC%8A%A4%ED%85%80-%EA%B2%80%EC%A6%9D-%EC%99%9C-%EC%95%88%EB%90%A0%EA%B9%8C)

간단하게 설명하자면 나는 리스트의 중복을 체크하는 로직을 request에서 처리하고 싶었다. 이건 spring validator를 이용 할 수 없었기 때문에 커스텀한 검증을 만들어서 사용했는데 이걸 꼭 request에서 사용하고 싶다보니 생긴 문제와 실제 `@Valid`의 동작을 알아보았다. 그리고 커스텀 어노테이션으로 처리했다.

```java
public record VoteStadiumCreateRequest(
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 50, message = "제목은 50자 이하여야 합니다.")
    String title,
    @Future(message = "투표 종료 시간은 현재 시간보다 미래의 시간으로 지정해야합니다.")
    LocalDateTime endAt,
    @Size(min = 1, message = "구장 선택은 필수입니다.")
    @Duplicate(message = "중복된 구장은 포함 할 수 없습니다.")
    List<StadiumChoices> choices
) {
    public VoteStadiumCreateServiceRequest toServiceRequest() {
        return new VoteStadiumCreateServiceRequest(
            title,
            endAt,
            choices.stream()
                .map(StadiumChoices::stadiumId)
                .toList()
        );
    }
}
```
위 코드 처럼 말이다. 자세한 내용은 글을 확인해보면 좋을 것 같다.

---
# 들어가며
이번 회고는 조금 간단하게? 해보려고 한다. 너무 1주차 회고는 무슨 회고가 아니라 거의 1주차 내 삶을 다 쓴거같다... 내용을 얼마나 줄일 수 있을진 모르겠지만 고민했던 부분, 그리고 결과 정도 작성해보고 앞으로의 목표를 다시 한번 생각해보려고 한다. 내가 생각하기에는 그냥 뭔가 많이 못했다... 일주일동안 생각보다 이룬게 없다고 해야될까 그래서 그냥 짧게 적어지는게 아닐까 생각한다.

1. 모든 PR에 코드리뷰 하기&받기
2. record 써보기
3. QueryDsl 활용하기
4. SQL 튜닝(?) 하기라고 하며 인덱스 써볼 구석 찾아보기
5. Soft Delete 활용하기
6. @Embaddable, 고급매핑 사용하여 ORM스럽게 사용해보기
7. 노션에 정리해둔 개발 키워드 중 도전 할 수 있는 부분 해보기
8. 당연하게도 테스트 열심히 짜기
9. 기록하기 & 회고쓰기

내가 프로젝트 하기전에 목표했던 것 중 2주차가 지나는 지금 얼마나 목표에 다가갔을까
2주차가 지나는 지금 1,2,5,6,8,9는 대부분 목표를 달성했다고 생각한다.

QueryDsl 활용은 일단 쓰고는 있긴한데, 그냥 정말 맛보기 정도라서 내가 생각한 목표에 다가가진 못했다.
SQL 튜닝과 인덱스 활용은 이슈만 파놓고 어쩌다보니 계속 미루게 되었다.
7번인 노션에 정리해둔 키워드 중 도전은 내가 생각한 만큼은 못했다. 물론 키워드 중 목표인 QueryDsl, record, 고급매핑 등이 있긴한데 무언가 아직까지는 만족스럽진 못하다...

# 2주차 한 일

## SingleTable전략 더 효과적으로 사용해보기?
나는 이번 프로젝트를 하면서 ORM스럽게(?) 하여튼 좀 더 객체지향적으로 한번 써보고싶었다. 그래서 SingleTable 전략도 썼고, 그에 따라서 엔티티 설계도 하고, 적용해서 써보았다.

근데 아무리 생각해도 내가 짰던 코드들이 SingleTable전략을 사용하는데 있어서 도움이 안되고 있는 것 같았다. 어쨌든 상속 구조를 사용하고 있었으면 뭔가 상속을 이용하는 무언갈 해야 될 것 같은데 나는 그냥 두 객체가 따로 놀고있었다. VoteItemLocate랑 VoteItemDate를 그냥 다른 객체처럼 해서 쓰고 있었다.
```java
// 장소 투표 조회
@Transactional(readOnly = true)
@Override
public VoteResponse getStadiumVote(Long voteId) {
    Vote vote = getVoteBy(voteId);
    List<VoteItem> voteItems = getVoteItemsBy(voteId);
    List<String> stadiumNames = getStadiumNames(voteItems);
    List<VoteItemResponse> voteItemResponse = getVoteItemLocateResponse(voteItems, stadiumNames);
    return VoteResponse.of(vote, voteItemResponse);
}

private List<VoteItemResponse> getVoteItemLocateResponse(List<VoteItem> voteItems, List<String> stadiumNames) {
    return voteItems.stream()
        .map(voteItem -> createVoteItemLocateResponse(voteItems, stadiumNames, voteItem))
        .toList();
}

private VoteItemResponse createVoteItemLocateResponse(List<VoteItem> voteItems, List<String> stadiumNames, VoteItem voteItem) {
    Long voteItemId = voteItem.getVoteItemId();
    Long voteCount = choiceRepository.countByVoteItemId(voteItemId);
    String contents = stadiumNames.get(voteItems.indexOf(voteItem));
    return VoteItemResponse.of(voteItemId, contents, voteCount);
}

// 일정 투표 조회
@Transactional(readOnly = true)
@Override
public VoteResponse getDateVote(Long voteId) {
    Vote vote = getVoteBy(voteId);
    voteItemRepository.findByVoteVoteId(voteId);

    List<VoteItemDate> voteItems = voteItemRepository.findByVoteVoteId(voteId).stream()
        .map(voteItem -> (VoteItemDate) voteItem)
        .toList();

    List<VoteItemResponse> voteItemResponse = getVoteItemDateResponse(voteItems);
    return VoteResponse.of(vote, voteItemResponse);
}

private List<VoteItemResponse> getVoteItemDateResponse(List<VoteItemDate> voteItems) {
    return voteItems.stream()
        .map(this::mapToVoteItemResponse)
        .toList();
}

private VoteItemResponse mapToVoteItemResponse(VoteItemDate voteItem) {
    Long voteItemId = voteItem.getVoteItemId();
    String content = voteItem.getTime().toString();
    Long count = choiceRepository.countByVoteItemId(voteItemId);
    return VoteItemResponse.of(voteItemId, content, count);
}
```
물론 이렇게 그냥 써도 된다고는 생각하는데, 뭔가 이상한거같았다.. 아니 분명히 상속 구조인데? 그냥 따로 따로 논다고 해야되려나 이럴거면 그냥 테이블을 따로 만들거나 차라리 그냥 nullable하게 만드는게 더 나았던걸까? 라고 생각이 든다.

그래서 그냥 한번 궁금증에 어떻게 `VoteItem`이라는 부모 객체를 잘 이용하면서 상속구조를 이용해 볼 수 없을까?를 고민하게 되었다.

```java
@Transactional(readOnly = true)
@Override
public VoteResponse getStadiumVote(Long voteId) {
    Vote vote = getVoteByVoteId(voteId);
    List<VoteItem> voteItems = getVoteItemsByVoteId(voteId);
    List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(voteItems);
    return VoteResponse.of(vote, voteItemResponses);
}

@Transactional(readOnly = true)
@Override
public VoteResponse getDateVote(Long voteId) {
    Vote vote = getVoteByVoteId(voteId);
    List<VoteItem> voteItems = getVoteItemsByVoteId(voteId);
    List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(voteItems);
    return VoteResponse.of(vote, voteItemResponses);
}
```
일단 이렇게 public은 바꾸었다. 근데 갑자기 보면서 든 생각이 두개 메서드가 아예 똑같다는 것이다.. 이것도 그냥 하나로 통합할 수 있지 않을까? 라는 생각도 든다. 회고를 다 쓰고나서 해봐야겠다.

그래서 어떻게 이렇게 할 수 있었냐 하면 제네릭을 사용했다.

```java
private <T extends VoteItem> List<VoteItemResponse> convertVoteItemsToResponseFrom(List<T> voteItems) {
    return voteItems.stream()
        .map(this::convertVoteItemToResponse)
        .toList();
}

private <T extends VoteItem> VoteItemResponse convertVoteItemToResponse(T voteItem) {
    if (voteItem instanceof VoteItemLocate voteItemLocate) {
        return convertToVoteItemResponseFrom(voteItemLocate);
    }
    if (voteItem instanceof VoteItemDate voteItemDate) {
        return convertToVoteItemResponseFrom(voteItemDate);
    }
    throw new IllegalArgumentException("지원하지 않는 투표 항목입니다.");
}

private VoteItemResponse convertToVoteItemResponseFrom(VoteItemDate voteItemDate) {
    return VoteItemResponse.of(
        voteItemDate.getVoteItemId(),
        voteItemDate.getTime().toString(),
        choiceRepository.countByVoteItemId(voteItemDate.getVoteItemId())
    );
}

private VoteItemResponse convertToVoteItemResponseFrom(VoteItemLocate voteItemLocate) {
    return VoteItemResponse.of(
        voteItemLocate.getVoteItemId(),
        stadiumRepository.findStadiumNameById(voteItemLocate.getStadiumId()),
        choiceRepository.countByVoteItemId(voteItemLocate.getVoteItemId())
    );
}
```
그냥 `<T extends VoteItem>`을 받을 수 있도록 해서 자식들을 받아서 처리 하도록 하였다. 그런데 상세 구현부가 조금 달라지다 보니까 instanceOf를 사용하여서 Date와 Locate에 대해서는 다른 처리를 하도록 구현하였다.

이렇게 작성하고 나니까 뭔가 편안(?) 해진 기분이라고 해야될까...? 뭔가 상속이라는걸 제대로 써먹어본거 아닐까? 라고 생각은 하게 되었는데 솔직하게 말하면 이렇게 짜는게 맞나? 라는 의구심이 더 많이 들긴한다.

일단 내 생각엔 제네릭을 사용해서 받는것 까진 나쁘지않은 아이디어라고 생각했다. 왜냐면 SingleTable이라는게 DType에 다른 객체로 사용하려는 의도가 있었다고 생각한다. 나의 경우는 Locate와 Date가 다른 로직을 갖는 것 처럼

그런데 일단 instanceOf를 저렇게 쓰는게 맞나? 라는 생각이 든다 그럼 다른 타입의 객체가 추가 될 때마다 instanceOf를 추가하나? 이게 정말 객체지향적인게 맞나? 라는 생각도 든다.
그리고 response를 만들 때 쿼리가 나간다. 이게 stream으로 돌고 있다. 그러면 단일쿼리가 반복문으로 나가는 것이다. 이게 결국 좋은 구조가 아니지않나? 라는 생각이든다.

SingleTable에 대해서는 그냥 SingleTable이 뭔지 설명만 하는게 많지 실제로 사용해본 예제가 나오는 경우가 많이 없는 것 같다. 그래서 뭔가 참고 할만한 자료도 없었고, 그냥 진짜 내맘대로 짰다. 만족스러우면서도 불만족스럽다.

하여튼 이렇게 SingleTable이지만 각 객체가 하는 역할이 다르니까 이 때는 한 테이블에 컬럼을 nullable하게 만드는 것 보다 SingleTable전략을 가져가는게 좀 더 좋은 방안이지 않을까? 라고 생각하면서 내가 SingleTable을 어느 때 사용해야 될지 다시 한번 생각해보게 되었다.

## 동적 스케쥴링 도전하기
여기서 생각보다 너무많은 시간을 썼다. 그리고 그냥 너무 생각이 많았다. 단순하게 생각하면 됐을걸 일단 구현해보고 수정해나가던지 더 좋은 방법으로 교체하던지 했으면 됐는데 시작부터 고민을 많이했다.

내가 동적 스케쥴링을 사용하게 된 계기는 '투표가 종료시간이 되면 종료되어야 한다.'라는 기능을 구현하기 위해서 였다. 그냥 진짜 간단하게 구현하려면 종료시간이 되면 바꾸는거? 그냥 스케쥴링 짧게 걸어서 하면되지라고 생각했다. 근데 이 방법은 싫었다. 모든 요구사항을 맞추기 위해서는 cron 을 짧게 가져가서 시간에 맞춰서 종료하게 해야하는데 이러면 만약 투표종료시간이 아닐때도 계속 스케쥴러가 돌고있을 것 아닌가. 비효율의 극치라고 생각했다. 그래서 나는 여기저기 물어보기도 하면서 검색도 하고 알아낸 방법은 동적 스케쥴링이었다.

동적 스케쥴링을 사용하면 종료시간에 스케쥴러가 동작하게 하여서 투표 마감시간이 되면 종료하는 메서드를 실행 시킬 수 있다고 했다.

그래서 동적 스케쥴링을 사용하기로 했고 먼저 스프링에서 제공해주는 `TaskScheduler`를 사용하기로 했다.
사용하기로 한 이유는 생각보다 사용하는게 어렵지는 않아보였다. 그냥 스케쥴을 넣어주면 동작하는 것으로 보였다. 파고들면 상세한 구현부는 어렵겠지만 일단 내가 가져다 쓸수는 있을 것으로 보였다.
### Instant 타입이 뭐야?

![](https://velog.velcdn.com/images/naminhyeok/post/5cc92e9b-ae39-44b9-89b5-4e75a7f9ae40/image.png)

처음에 메서드를 쓰려고 했을 때 처음 보는 타입을 넣으라고 했다. Instant? 이게 도대체 뭔지 몰랐다. 그래서 나는 그냥 종료시간을 뭐 Instant타입으로 감싸면 되지 라고 생각하고 그냥 뇌를 빼고 대충 메서드체이닝으로 넣어서 해봤다. 그런데 내가 생각한대로 동작하지 않았다. 코드는 아래와 같다.

```java
private void addVoteTaskToTaskSchedule(Vote vote) {
        taskScheduler.schedule(publishClosedVoteTask(vote.getVoteId()), vote.getEndAt().toInstant(ZoneOffset.UTC));
    }
```

toInstant가 뭔가 그냥 형변환을 해줄 것 같았다. 그리고 다시 파라미터로 뭘 넣어야되나 봤더니 ZoneOffSet을 넣으라고 되어있었다. 그냥 어디서 들어본 UTC를 넣었더니 또 감사하게도 인텔리제이에서 자동완성을 통해서 이렇게 코드를 만들어주셨다.

아무것도 모르고 코딩하니까 될리가 없다.

> UTC란? 영국을 기준(UTC+0:00)으로 각 지역의 시차를 규정한 것이다. 한국은 영국보다 9시간 빠르므로UTC+9:00이라고 표시한다.

그러니까 내가 해준대로 하면 영국시간 기준으로니까 종료시간+9시간 뒤에 스케쥴이 실행된다. ㅋㅋㅋㅋ 이러니까 될리가 있나

하여튼 그래서 Instant Type이 뭐였을까

> Instant는 자바 1.8 java.time package에 들어가있으며 UTC의 타임 라인에있는 순간으로,
1970년 1월 1일 UTC의 첫 번째 순간 이후의 현재 시간까지의 나노초를 나타낸 값 입니다.
부분의 비즈니스 로직, 데이터 저장 및 데이터 변경은 UTC로 이루어져야하므로 자주 사용하기에 편리한 클래스입니다.

출처: [Java8+ Instant vs LocalDateTime 각 사용방법](https://velog.io/@lsb156/Instant-vs-LocalDateTime)

그러니까 LocalDateTime은 지역정보와 같은 곳이 없기 때문에 시차를 고려할 수 없다? 와 같은 내용인 것 같다. 그냥 LocalDateTime을 생각없이 써왔는데 이러한 문제도 존재하는구나.. 라고 생각하게 되었다.

```java
public Instant getInstantEndAt() {
    return endAt.atZone(ZoneId.systemDefault()).toInstant();
}
```
이렇게 리팩토링 해서 지금 돌아가고 있는 시스템의 시각으로 Instant객체로 반환하도록 수정하여서 하니 스케쥴러가 내가 원하는 시간에 잘 동작하였다.

### 이벤트기반 동적 스케쥴링(?)

노션에 정리해둔 내용중 스프링부트 이벤트 처리가 있었다. 결합도를 낮추기 위해서 사용한다고 하니 한번 사용해보고 싶었다. 최종 로직의 목표는 상태만 바꾸는게 아닌 투표 종료되면 상대팀과 자동으로 매칭 할 수 있도록 하는 것이었다.

사실 여기서부터 좀 많이 애를 먹었다.

```java
@Transactional
@Override
public VoteResponse createStadiumVote(VoteStadiumCreateServiceRequest request, Long teamId, Member member) {
    Long memberId = member.getMemberId();
    validateTeamByTeamId(teamId);

    Vote vote = Vote.create(memberId, teamId, request.title(), request.endAt());
    Vote savedVote = voteRepository.save(vote);
    addVoteTaskToTaskSchedule(savedVote);

    List<VoteItemLocate> voteItemLocates = createVoteItemLocate(request, savedVote);

    List<VoteItemLocate> savedVoteItems = voteItemRepository.saveAll(voteItemLocates);
    List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(savedVoteItems);
    return VoteResponse.of(vote, voteItemResponses);
}


private void addVoteTaskToTaskSchedule(Vote vote) {
    taskScheduler.schedule(publishClosedVoteTask(vote.getVoteId()), vote.getInstantEndAt());
}

private Runnable publishClosedVoteTask(Long voteId) {
    return () -> eventPublisher.publishEvent(new RegisteredVoteEvent(voteId));
}
```
나는 이런식으로 투표가 만들어 질 때 이벤트를 스케쥴에 등록하도록 했다.
이 때 내가 Vote를 보내도 되는데 id를 보낸 이유는 간단하다.

실제로 vote가 어떻게 변경되었을 지 모른다. 마감시간이 변경되거나, 삭제를 했을 수도 있다. 그러한 경우를 대비해서 id를 넘기고 이벤트 처리하는 부분에서 한번 Vote에 대해서 확인하고, 업데이트가 일어나게 하였다.

```java
@Async
@Transactional
@EventListener
public void onClosedVote(RegisteredVoteEvent event) {
    Optional<Vote> vote = voteRepository.findNotDeletedVoteById(event.voteId());
    vote.ifPresent(Vote::updateVoteStatusToClose);
}
```

실제로 이벤트를 받았을 때 하는 행동이다.
근데 나는 여기서 `@TransactionalEventListner`를 사용하고 싶었다.

왜냐하면 투표를 생성하는 로직에서 예외가 발생하면 이벤트를 발행하지 않고 싶었다. 그래서 `@TransactionalEventListener`를 사용해서 커밋 된 후에 실행되게 만들고 싶었다.

그래서 나는 로직을 조금 수정했다.

```java
@Async
@Transactional
@TransactionalEventListener
public void onClosedVote(RegisteredVoteEvent event) {
    Optional<Vote> vote = voteRepository.findById(event.voteId());
    vote.ifPresent(Vote::updateVoteStatusToClose);
}
```
![](https://velog.velcdn.com/images/naminhyeok/post/d03c9c65-e680-4b1c-a370-f8227769cf76/image.png)

이러한 오류가 났다. Popagation.REQUIRES_NEW 는 새로운 트랜잭션이 생성하고 현재 트랜잭션이 있다면 이를 일시중지 하기 때문에 명시적으로 새 트랜잭션을 실행할 수 있도록 선언해야 한다는 뜻이다.

```java
@Async
@Transactional(propagation = Propagation.REQUIRES_NEW)
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void onClosedVote(RegisteredVoteEvent event) {
    Optional<Vote> vote = voteRepository.findById(event.voteId());
    vote.ifPresent(Vote::updateVoteStatusToClose);
}
```

그래서 나는 TransactionPhase도 명시적으로 표현해주고 REQUIRES_NEW 도 표현해주었다. 그런데 내가 공부가 부족한건지 모르겠지만 이렇게 바꾸니까 더티체킹이 제대로 이루어지지가 않는다...ㅎㅎ

지금 당장에 이거에만 목메고 있을 수가 없어서 추후에 더 알아보려고 한다. 그래서 최종적으로는 첫 코드와 같이 갔다.

```java
@Async
@Transactional
@EventListener
public void onClosedVote(RegisteredVoteEvent event) {
    Optional<Vote> vote = voteRepository.findNotDeletedVoteById(event.voteId());
    vote.ifPresent(Vote::updateVoteStatusToClose);
}
```

이벤트리스너를 사용할 때 비동기처리를 권장하고 있으므로 비동기를 달아주었다. 비동기는 트랜잭션과 함께 사용할 때 항상 조심해야하지만... 투표를 종료하는 로직이니 크게 문제없지 않을까? 라고 생각하면서 달아두었다.

지금 상태로 가면 문제점은 아까전에 예상한대로 예외가 발생해도 이벤트가 등록이된다.

### 투표가 미리 종료되면 스케쥴러는 어떻게 ?

내 기존코드는 투표가 종료되거나 수정될 때 스케쥴러를 변경 할 수가 없었다. 그냥 무식하게 음 모르겠고 거기가서 예외가 발생하던지 해라는 식으로 구현해두었다. 근데 아무리 생각해도 이건 말이 안된다. 임의로 종료를 하거나 종료시간이 바뀌면 무언가 해주어야 한다고 생각이 들었다.

근데 진짜 아무리 TaskScheduler에 대한 정보랑 공식문서를 찾아봐도 나와있지 않았다. 어떠한 정보도 내가 맘대로 스케쥴링을 관리 할 수가 없었다.

그래서 처음에는 아 이럴때 메시지큐를 써야되는건가? 라고 생각하면서 메시지큐에 대해 엄청 찾아봤다. 아니면 레디스? 이런 생각하면서 어떻게 이걸 구현해야되나 싶었다. 그러던 중 그냥 Map으로 관리하면 되지않을까 라는 생각에 Map으로 관리를 하게 되었는데 ConcurrentHashMap을 사용하는게 Multi-Thread 환경에서 사용할 수 있도록 나온 클래스 이므로 좋아보였다.

그래서 그냥 간단하게 ConcurrentHashMap을 써서 구현했다.

```java
private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

private void addVoteTaskToTaskSchedule(Vote vote) {
    ScheduledFuture<?> scheduledTask = taskScheduler.schedule(publishClosedVoteTask(vote.getVoteId()), vote.getInstantEndAt());
    scheduledTasks.put(vote.getVoteId(), scheduledTask);
}

private Runnable publishClosedVoteTask(Long voteId) {
    return () -> eventPublisher.publishEvent(new RegisteredVoteEvent(voteId));
}

private void cancelTaskInSchedulerFromVoteId(Long voteId) {
    ScheduledFuture<?> scheduledTask = scheduledTasks.get(voteId);
    if (scheduledTask == null) {
        throw new IllegalArgumentException("해당하는 투표 ID로 등록된 작업이 없습니다.");
    }
    scheduledTask.cancel(false);
    scheduledTasks.remove(voteId);
}
```

이렇게 해서 실제로 이미 처리된 스케쥴에 대해서는 다시 실행되지 않도록 구현하였다.

조금은 내가 원하는 스케쥴러에 가까워진 느낌은 있다.

하지만 아직도 맘에 안드는 것이 많긴하다. 기능적으로는 당연히 지금 문제는 없지만, 인메모리로 Map을 가지고 있는것도 맘에 안든다. 그리고 아까 해결 못한 예외가 발생해도 이벤트가 발생하는것도 맘에 안든다.

그래서 Quartz도 찾아서 시도해보고, Kafka나 Redis를 사용해야되나 생각하긴 했는데.. 지금 당장엔 좀 너무 헤비한 작업이 아닐까 생각해서 일단 해야할 일들을 모두 처리하고 개인적으로 시도해보려고 한다.
사실은 Quartz는 연습해보고 도입하려고 했는데 잘 되는것 같으면서도 잘 안돼서.. 시간상 포기했다. 나중에 바꿀 수 있으면 공부를 더 해서 바꾸면 좋을 것 같다.

PR의 내이름을 보면 내가 기능 개발하면서 좀 깊게 한건 여기까지지였던 것 같다. 나머지 기능 개발한 부분은 깊게 했다거나 하는 부분은 없는 것 같아서 여기까지 하려고한다.
