package team4.footwithme.stadium.service;

import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;
import team4.footwithme.stadium.domain.Stadium;

import java.util.List;

public interface StadiumService {
    List<StadiumsResponse> getStadiumList();
    StadiumDetailResponse getStadiumDetail(Long id);
    List<StadiumsResponse> getStadiumsByName(String query);
    List<StadiumsResponse> getStadiumsByAddress(String address);
    List<StadiumsResponse> getStadiumsWithinDistance(StadiumSearchByLocationServiceRequest request);




    Stadium findByIdOrThrowException(long id);
}
