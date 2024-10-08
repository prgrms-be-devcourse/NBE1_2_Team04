package team4.footwithme.vote.repository;

import team4.footwithme.vote.domain.Choice;

import java.util.List;

public interface CustomChoiceRepository {

    Long countByVoteItemId(Long voteItemId);

    List<Choice> findByMemberIdAndVoteId(Long memberId, Long voteId);

    List<Long> findMemberIdsByVoteItemId(Long voteItemId);

    Long maxChoiceCountByVoteId(Long voteId);

}
