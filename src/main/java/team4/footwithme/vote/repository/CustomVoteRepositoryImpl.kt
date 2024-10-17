package team4.footwithme.vote.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import team4.footwithme.global.domain.IsDeleted
import team4.footwithme.vote.domain.*
import java.util.*

class CustomVoteRepositoryImpl(private val queryFactory: JPAQueryFactory) : CustomVoteRepository {
    override fun findNotDeletedVoteById(id: Long?): Optional<Vote> {
        return Optional.ofNullable(
            queryFactory.select(QVote.vote)
                .from(QVote.vote)
                .where(
                    QVote.vote.voteId.eq(id)
                        .and(QVote.vote.isDeleted.eq(IsDeleted.FALSE))
                )
                .fetchOne()
        )
    }

    override fun findOpenedVotes(): List<Vote>? {
        return queryFactory.select(QVote.vote)
            .from(QVote.vote)
            .where(
                QVote.vote.isDeleted.eq(IsDeleted.FALSE)
                    .and(QVote.vote.voteStatus.eq(VoteStatus.OPENED))
            )
            .fetch()
    }

    override fun choiceMemberCountByVoteId(voteId: Long?): Long? {
        return queryFactory.select(QChoice.choice.memberId.countDistinct())
            .from(QVote.vote).join(QVoteItem.voteItem).on(QVoteItem.voteItem.vote.eq(QVote.vote))
            .leftJoin(QChoice.choice).on(QChoice.choice.voteItemId.eq(QVoteItem.voteItem.voteItemId))
            .where(
                QVote.vote.voteId.eq(voteId)
                    .and(QVote.vote.isDeleted.eq(IsDeleted.FALSE))
            )
            .fetchOne()
    }

    override fun findRecentlyVoteByTeamId(teamId: Long?): Vote {
        return queryFactory.select(QVote.vote)
            .where(
                QVote.vote.isDeleted.eq(IsDeleted.FALSE)
                    .and(QVote.vote.voteStatus.eq(VoteStatus.CLOSED))
            )
            .from(QVote.vote)
            .orderBy(QVote.vote.updatedAt.desc())
            .fetchFirst()!!
    }

    override fun findAllByTeamId(teamId: Long?): List<Vote>? {
        return queryFactory.select(QVote.vote)
            .from(QVote.vote)
            .where(
                QVote.vote.teamId.eq(teamId)
                    .and(QVote.vote.isDeleted.eq(IsDeleted.FALSE))
            )
            .fetch()
    }
}
