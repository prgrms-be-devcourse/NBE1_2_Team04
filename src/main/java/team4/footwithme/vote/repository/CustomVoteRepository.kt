package team4.footwithme.vote.repository

import team4.footwithme.vote.domain.Vote
import java.util.*

interface CustomVoteRepository {
    fun findNotDeletedVoteById(id: Long?): Optional<Vote>

    fun findOpenedVotes(): List<Vote>?

    fun choiceMemberCountByVoteId(voteId: Long?): Long?

    fun findRecentlyVoteByTeamId(teamId: Long?): Vote

    fun findAllByTeamId(teamId: Long?): List<Vote>?
}
