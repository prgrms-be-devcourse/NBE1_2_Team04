package team4.footwithme.vote.service

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import team4.footwithme.vote.domain.VoteItemDate
import team4.footwithme.vote.repository.VoteRepository

@Component
class VoteEventHandler(private val voteRepository: VoteRepository, private val voteService: VoteService) {
    @Async
    @Transactional
    @EventListener
    fun onClosedVote(event: RegisteredVoteEvent) {
        val vote = voteRepository.findNotDeletedVoteById(event.voteId)
            .orElseThrow { IllegalArgumentException("해당하는 투표가 없습니다.") }

        // 만약 이 투표가 장소 투표라면 아무일도 일어나지 않는다.
        val voteItems = vote.getVoteItems()
        if (voteItems!![0] is VoteItemDate) {
            voteService.makeReservation(vote)
            vote.updateVoteStatusToClose()
        }
    }
}
