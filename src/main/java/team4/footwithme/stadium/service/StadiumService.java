package team4.footwithme.stadium.service;

import team4.footwithme.stadium.api.response.StadiumDetailResponse;
import team4.footwithme.stadium.api.response.StadiumsResponse;
import team4.footwithme.stadium.domain.Stadium;

import java.util.List;

public interface StadiumService {
    List<StadiumsResponse> getStadiumList();
    StadiumDetailResponse getStadiumDetail(Long id);
    List<StadiumsResponse> getAutocompleteSuggestions(String query);






    Stadium findByIdOrThrowStadiumException(long id);
}
