package team4.footwithme.vote.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.domain.VoteItem;
import team4.footwithme.vote.domain.VoteItemDate;
import team4.footwithme.vote.repository.VoteRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class VoteEventHandler {

    private final VoteRepository voteRepository;
    private final VoteService voteService;

    @Async
    @Transactional
    @EventListener
    public void onClosedVote(RegisteredVoteEvent event) {
        Vote vote = voteRepository.findNotDeletedVoteById(event.voteId()).orElseThrow(
            () -> new IllegalArgumentException("해당하는 투표가 없습니다.")
        );

        // 만약 이 투표가 장소 투표라면 아무일도 일어나지 않는다.
        List<VoteItem> voteItems = vote.getVoteItems();
        if (voteItems.get(0) instanceof VoteItemDate) {
            voteService.makeReservation(vote);
            vote.updateVoteStatusToClose();
        }
    }

}
