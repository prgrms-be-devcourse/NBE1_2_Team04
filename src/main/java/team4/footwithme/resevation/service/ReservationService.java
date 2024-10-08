package team4.footwithme.resevation.service;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    void createReservation(Long memberId, Long courtId, Long teamId, LocalDateTime matchDate, List<Long> memberIds);
}
