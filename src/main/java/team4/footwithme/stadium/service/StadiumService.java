package team4.footwithme.stadium.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import team4.footwithme.stadium.service.request.StadiumRegisterServiceRequest;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
import team4.footwithme.stadium.service.request.StadiumUpdateServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

public interface StadiumService {
    Slice<StadiumsResponse> getStadiumList(Pageable pageable);

    StadiumDetailResponse getStadiumDetail(Long id);

    Slice<StadiumsResponse> getStadiumsByName(String query, Pageable pageable);

    Slice<StadiumsResponse> getStadiumsByAddress(String address, Pageable pageable);

    Slice<StadiumsResponse> getStadiumsWithinDistance(StadiumSearchByLocationServiceRequest request, Pageable pageable);

    StadiumDetailResponse registerStadium(StadiumRegisterServiceRequest request, Long memberId);

    StadiumDetailResponse updateStadium(StadiumUpdateServiceRequest request, Long memberId, Long stadiumId);

    void deleteStadium(Long memberId, Long stadiumId);
}
