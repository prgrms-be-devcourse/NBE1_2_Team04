package team4.footwithme.resevation.service

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import team4.footwithme.vote.service.EndVoteEvent

@Component
class ReservationEventHandler(private val reservationService: ReservationService) {
    @EventListener
    fun handleReservationEvent(event: EndVoteEvent) {
        reservationService.createReservation(
            event.memberId,
            event.courtId,
            event.teamId!!,
            event.matchDate,
            event.memberIds!!
        )
    }
}
