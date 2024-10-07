package team4.footwithme.resevation.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import team4.footwithme.resevation.service.response.ReservationsResponse;

public interface ReservationService {
    Slice<ReservationsResponse> findReadyReservations(Long reservationId, Integer page);
}
