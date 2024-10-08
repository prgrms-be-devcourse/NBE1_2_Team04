package team4.footwithme.vote.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.domain.VoteItem;
import team4.footwithme.vote.domain.VoteItemDate;
import team4.footwithme.vote.domain.VoteItemLocate;
import team4.footwithme.vote.repository.ChoiceRepository;
import team4.footwithme.vote.repository.VoteItemRepository;
import team4.footwithme.vote.repository.VoteRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class VoteEventHandler {

    private final VoteRepository voteRepository;
    private final ChoiceRepository choiceRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    @Transactional
    @EventListener
    public void onClosedVote(RegisteredVoteEvent event) {
        Vote vote = voteRepository.findNotDeletedVoteById(event.voteId()).orElseThrow(
            () -> new IllegalArgumentException("해당하는 투표가 없습니다.")
        );
        vote.updateVoteStatusToClose();

        // 만약 이 투표가 장소 투표라면 아무일도 일어나지 않는다.
        List<VoteItem> voteItems = vote.getVoteItems();
        if (voteItems instanceof VoteItemLocate) {
            return;
        }
        if( voteItems instanceof VoteItemDate) {
            Long voteItemDateId = choiceRepository.maxChoiceCountByVoteId(vote.getVoteId());
            List<Long> memberIds = choiceRepository.findMemberIdsByVoteItemId(voteItemDateId);
            Optional<VoteItemDate> voteItemDate = vote.getVoteItems().stream()
                .filter(voteItem -> voteItem.getVoteItemId().equals(voteItemDateId))
                .map(voteItem -> (VoteItemDate) voteItem)
                .findFirst();
            Long memberId = vote.getMemberId();

            LocalDateTime matchDate = voteItemDate.get().getTime();

            Long teamId = vote.getTeamId();

            Vote locateVote = voteRepository.findRecentlyVoteByTeamId(teamId);
            Long voteItemLocateId = choiceRepository.maxChoiceCountByVoteId(locateVote.getVoteId());
            Optional<VoteItemLocate> voteItemLocate = locateVote.getVoteItems().stream()
                .filter(voteItem -> voteItem.getVoteItemId().equals(voteItemLocateId))
                .map(voteItem -> (VoteItemLocate) voteItem)
                .findFirst();
            Long courtId = voteItemLocate.get().getCourtId();

            eventPublisher.publishEvent(new EndVoteEvent(
                courtId,
                memberId,
                teamId,
                matchDate,
                memberIds
            ));

        }
    }

}
