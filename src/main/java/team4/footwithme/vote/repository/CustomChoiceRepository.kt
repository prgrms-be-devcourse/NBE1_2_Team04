package team4.footwithme.vote.repository

import team4.footwithme.vote.domain.Choice

interface CustomChoiceRepository {
    fun countByVoteItemId(voteItemId: Long?): Long?

    fun findByMemberIdAndVoteId(memberId: Long?, voteId: Long?): List<Choice>?

    fun findMemberIdsByVoteItemId(voteItemId: Long?): List<Long>?

    fun maxChoiceCountByVoteId(voteId: Long?): Long
}
