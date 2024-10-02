package team4.footwithme.stadium.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.stadium.api.request.StadiumSearchByLocationRequest;
import team4.footwithme.stadium.service.StadiumService;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stadium")
public class StadiumApi {

    private final StadiumService stadiumService;

    @GetMapping("/stadiums")
    public ApiResponse<Slice<StadiumsResponse>> stadiums(Pageable pageable) {
        Slice<StadiumsResponse> stadiumList = stadiumService.getStadiumList(pageable);
        return ApiResponse.ok(stadiumList);
    }

    @GetMapping("/stadiums/{stadiumId}/detail")
    public ApiResponse<StadiumDetailResponse> getStadiumDetailById(@PathVariable Long stadiumId) {
        StadiumDetailResponse stadiumDetailResponse = stadiumService.getStadiumDetail(stadiumId);
        return ApiResponse.ok(stadiumDetailResponse);
    }

    @GetMapping("/stadiums/search/name")
    public ApiResponse<Slice<StadiumsResponse>> getStadiumsByName(@RequestParam String query, Pageable pageable) {
        Slice<StadiumsResponse> stadiums = stadiumService.getStadiumsByName(query, pageable);
        return ApiResponse.ok(stadiums);
    }

    @GetMapping("/stadiums/search/address")
    public ApiResponse<Slice<StadiumsResponse>> getStadiumsByAddress(@RequestParam String query, Pageable pageable) {
        Slice<StadiumsResponse> stadiums = stadiumService.getStadiumsByAddress(query, pageable);
        return ApiResponse.ok(stadiums);
    }

    @PostMapping("/stadiums/search/location")
    public ApiResponse<Slice<StadiumsResponse>> getStadiumsByLocation(@Validated @RequestBody StadiumSearchByLocationRequest request, Pageable pageable) {
        Slice<StadiumsResponse> stadiums = stadiumService.getStadiumsWithinDistance(request.toServiceRequest(), pageable);
        return ApiResponse.ok(stadiums);
    }
}
