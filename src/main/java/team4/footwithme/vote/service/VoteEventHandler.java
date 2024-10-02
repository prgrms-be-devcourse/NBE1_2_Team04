package team4.footwithme.vote.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.repository.VoteRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class VoteEventHandler {

    private final VoteRepository voteRepository;

    @Transactional
    @EventListener
    public void onClosedVote(RegisteredVoteEvent event) {
        Optional<Vote> vote = voteRepository.findById(event.voteId());
        vote.ifPresent(Vote::updateVoteStatusToClose);
    }

}
