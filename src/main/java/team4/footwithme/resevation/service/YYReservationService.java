package team4.footwithme.resevation.service;

import team4.footwithme.resevation.service.response.YYReservationInfoDetailsResponse;
import team4.footwithme.resevation.service.response.YYReservationInfoResponse;

import java.util.List;

public interface YYReservationService {
    /**
     * 팀 경기 정보 조회
     */
    List<YYReservationInfoResponse> getTeamReservationInfo(Long teamId);
    YYReservationInfoDetailsResponse getTeamReservationInfoDetails(Long reservationId);
}
