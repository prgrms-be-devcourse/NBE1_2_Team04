package team4.footwithme.vote.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team4.footwithme.vote.domain.VoteItem

@Repository
interface VoteItemRepository : JpaRepository<VoteItem?, Long?> {
    fun findByVoteVoteId(voteId: Long?): List<VoteItem?>?
}
