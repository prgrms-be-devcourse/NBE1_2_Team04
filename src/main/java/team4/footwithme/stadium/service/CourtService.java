package team4.footwithme.stadium.service;

import team4.footwithme.stadium.service.request.CourtDeleteServiceRequest;
import team4.footwithme.stadium.service.request.CourtRegisterServiceRequest;
import team4.footwithme.stadium.service.request.CourtUpdateServiceRequest;
import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;

import java.util.List;

public interface CourtService {
    List<CourtsResponse> getCourtsByStadiumId(Long stadiumId);

    List<CourtsResponse> getAllCourts();

    CourtDetailResponse getCourtByCourtId(Long courtId);

    CourtDetailResponse registerCourt(CourtRegisterServiceRequest request, Long memberId);

    CourtDetailResponse updateCourt(CourtUpdateServiceRequest request, Long memberId, Long courtId);

    void deleteCourt(CourtDeleteServiceRequest request, Long memberId, Long courtId);
}
