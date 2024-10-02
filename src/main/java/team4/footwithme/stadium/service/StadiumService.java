package team4.footwithme.stadium.service;

import org.springframework.data.domain.Slice;
import team4.footwithme.stadium.service.request.StadiumRegisterServiceRequest;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
import team4.footwithme.stadium.service.request.StadiumUpdateServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

public interface StadiumService {
    Slice<StadiumsResponse> getStadiumList(Integer page, String sort);

    StadiumDetailResponse getStadiumDetail(Long id);

    Slice<StadiumsResponse> getStadiumsByName(String query, Integer page, String sort);

    Slice<StadiumsResponse> getStadiumsByAddress(String address, Integer page, String sort);

    Slice<StadiumsResponse> getStadiumsWithinDistance(StadiumSearchByLocationServiceRequest request, Integer page, String sort);

    StadiumDetailResponse registerStadium(StadiumRegisterServiceRequest request, Long memberId);

    StadiumDetailResponse updateStadium(StadiumUpdateServiceRequest request, Long memberId, Long stadiumId);

    void deleteStadium(Long memberId, Long stadiumId);
}
