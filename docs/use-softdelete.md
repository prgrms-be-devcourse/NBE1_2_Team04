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