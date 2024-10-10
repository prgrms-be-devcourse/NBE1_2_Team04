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
public VoteResponse getVote(Long voteId) {
    Vote vote = getVoteByVoteId(voteId);
    List<VoteItem> voteItems = getVoteItemsByVoteId(voteId);
    List<VoteItemResponse> voteItemResponses = convertVoteItemsToResponseFrom(voteItems);
    return VoteResponse.of(vote, voteItemResponses);
}
```
일단 이렇게 public은 바꾸었다.
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
