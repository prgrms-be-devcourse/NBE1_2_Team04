package team4.footwithme.vote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import team4.footwithme.vote.domain.Choice;

import java.util.List;

import static team4.footwithme.vote.domain.QChoice.choice;
import static team4.footwithme.vote.domain.QVote.vote;
import static team4.footwithme.vote.domain.QVoteItem.voteItem;

public class CustomChoiceRepositoryImpl implements CustomChoiceRepository {

    private final JPAQueryFactory queryFactory;

    public CustomChoiceRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long countByVoteItemId(Long voteItemId) {
        return queryFactory.select(choice.count())
            .from(choice)
            .where(choice.voteItemId.eq(voteItemId))
            .fetchOne();
    }

    @Override
    public List<Choice> findByMemberIdAndVoteId(Long memberId, Long voteId) {
        return queryFactory.select(choice)
            .from(choice)
            .join(voteItem).on(choice.voteItemId.eq(voteItem.voteItemId))
            .join(voteItem.vote, vote)
            .where(choice.memberId.eq(memberId)
                .and(vote.voteId.eq(voteId)))
            .fetch();
    }

    @Override
    public List<Long> findMemberIdsByVoteItemId(Long voteItemId) {
        return queryFactory.select(choice.memberId)
            .from(choice)
            .where(choice.voteItemId.eq(voteItemId))
            .fetch();
    }

    @Override
    public Long maxChoiceCountByVoteId(Long voteId) {
        return queryFactory.select(choice.voteItemId)
            .from(choice)
            .join(voteItem).on(choice.voteItemId.eq(voteItem.voteItemId))
            .join(voteItem.vote, vote)
            .where(vote.voteId.eq(voteId))
            .groupBy(choice.voteItemId)
            .orderBy(choice.count().desc())
            .fetchFirst();
    }
}