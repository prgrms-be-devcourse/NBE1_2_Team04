package team4.footwithme.resevation.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import team4.footwithme.vote.service.EndVoteEvent;

@Component
public class ReservationEventHandler {

    private final ReservationService reservationService;

    public ReservationEventHandler(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @EventListener
    public void handleReservationEvent(EndVoteEvent event) {
        reservationService.createReservation(event.memberId(), event.courtId(), event.teamId(), event.matchDate(), event.memberIds());
    }
}
