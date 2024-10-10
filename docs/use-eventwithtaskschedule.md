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
