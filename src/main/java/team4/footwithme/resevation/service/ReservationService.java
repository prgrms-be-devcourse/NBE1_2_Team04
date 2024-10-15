package team4.footwithme.resevation.service;

import org.springframework.data.domain.Slice;
import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.service.request.ReservationUpdateServiceRequest;
import team4.footwithme.resevation.service.response.ReservationInfoDetailsResponse;
import team4.footwithme.resevation.service.response.ReservationInfoResponse;
import team4.footwithme.resevation.service.response.ReservationsResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    Slice<ReservationsResponse> findReadyReservations(Long reservationId, Integer page);

    void createReservation(Long memberId, Long courtId, Long teamId, LocalDateTime matchDate, List<Long> memberIds);

    List<ReservationInfoResponse> getTeamReservationInfo(Long teamId);

    ReservationInfoDetailsResponse getTeamReservationInfoDetails(Long reservationId);

    Long deleteReservation(Long reservationId, Member member);

    ReservationInfoResponse changeStatus(ReservationUpdateServiceRequest request, Member member);
}