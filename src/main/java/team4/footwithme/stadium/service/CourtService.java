package team4.footwithme.stadium.service;

import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;

import java.util.List;

public interface CourtService {
    List<CourtsResponse> getCourtsByStadiumId(Long stadiumId);
    List<CourtsResponse> getAllCourts();
    CourtDetailResponse getCourtByCourtId(Long courtId);
}
