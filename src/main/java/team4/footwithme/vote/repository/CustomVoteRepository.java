package team4.footwithme.vote.repository;

import team4.footwithme.vote.domain.Vote;
import team4.footwithme.vote.domain.VoteItemLocate;

import java.util.List;
import java.util.Optional;

public interface CustomVoteRepository {

    Optional<Vote> findNotDeletedVoteById(Long id);

    List<Vote> findOpenedVotes();

    Long choiceMemberCountByVoteId(Long voteId);

    Vote findRecentlyVoteByTeamId(Long teamId);

    List<Vote> findAllByTeamId(Long teamId);
}
