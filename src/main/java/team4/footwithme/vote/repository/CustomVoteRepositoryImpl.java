package team4.footwithme.vote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import team4.footwithme.vote.domain.Vote;

import java.util.List;
import java.util.Optional;

import static team4.footwithme.global.domain.IsDeleted.FALSE;
import static team4.footwithme.vote.domain.QChoice.choice;
import static team4.footwithme.vote.domain.QVote.vote;
import static team4.footwithme.vote.domain.QVoteItem.voteItem;
import static team4.footwithme.vote.domain.VoteStatus.CLOSED;
import static team4.footwithme.vote.domain.VoteStatus.OPENED;

public class CustomVoteRepositoryImpl implements CustomVoteRepository {

    private final JPAQueryFactory queryFactory;

    public CustomVoteRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Vote> findNotDeletedVoteById(Long id) {
        return Optional.ofNullable(queryFactory.select(vote)
            .from(vote)
            .where(vote.voteId.eq(id)
                .and(vote.isDeleted.eq(FALSE)))
            .fetchOne());
    }

    @Override
    public List<Vote> findOpenedVotes() {
        return queryFactory.select(vote)
            .from(vote)
            .where(vote.isDeleted.eq(FALSE)
                .and(vote.voteStatus.eq(OPENED)))
            .fetch();
    }

    @Override
    public Long choiceMemberCountByVoteId(Long voteId) {
        return queryFactory.select(choice.memberId.countDistinct())
            .from(vote).join(voteItem).on(voteItem.vote.eq(vote))
            .leftJoin(choice).on(choice.voteItemId.eq(voteItem.voteItemId))
            .where(vote.voteId.eq(voteId)
                .and(vote.isDeleted.eq(FALSE)))
            .fetchOne();
    }

    @Override
    public Vote findRecentlyVoteByTeamId(Long teamId) {
        return queryFactory.select(vote)
            .where(vote.isDeleted.eq(FALSE)
                .and(vote.voteStatus.eq(CLOSED)))
            .from(vote)
            .orderBy(vote.updatedAt.desc())
            .fetchFirst();
    }

    @Override
    public List<Vote> findAllByTeamId(Long teamId) {
        return queryFactory.select(vote)
            .from(vote)
            .where(vote.teamId.eq(teamId)
                .and(vote.isDeleted.eq(FALSE)))
            .fetch();
    }
}
