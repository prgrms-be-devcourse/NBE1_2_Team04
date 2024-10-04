package team4.footwithme.stadium.service;

import org.springframework.data.domain.Slice;
import team4.footwithme.stadium.service.request.CourtDeleteServiceRequest;
import team4.footwithme.stadium.service.request.CourtRegisterServiceRequest;
import team4.footwithme.stadium.service.request.CourtUpdateServiceRequest;
import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;

public interface CourtService {
    Slice<CourtsResponse> getCourtsByStadiumId(Long stadiumId, Integer page, String sort);

    Slice<CourtsResponse> getAllCourts(Integer page, String sort);

    CourtDetailResponse getCourtByCourtId(Long courtId);

    CourtDetailResponse registerCourt(CourtRegisterServiceRequest request, Long memberId);

    CourtDetailResponse updateCourt(CourtUpdateServiceRequest request, Long memberId, Long courtId);

    void deleteCourt(CourtDeleteServiceRequest request, Long memberId, Long courtId);
}
