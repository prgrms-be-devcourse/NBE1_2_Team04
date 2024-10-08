package team4.footwithme.resevation.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import team4.footwithme.resevation.service.response.ReservationsResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    Slice<ReservationsResponse> findReadyReservations(Long reservationId, Integer page);
  
    void createReservation(Long memberId, Long courtId, Long teamId, LocalDateTime matchDate, List<Long> memberIds);
}