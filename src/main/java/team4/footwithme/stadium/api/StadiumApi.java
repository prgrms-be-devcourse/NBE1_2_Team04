package team4.footwithme.stadium.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.stadium.api.request.StadiumSearchByLocationRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;
import team4.footwithme.stadium.service.StadiumService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stadium")
public class StadiumApi {

    private final StadiumService stadiumService;

    @GetMapping("/stadiums")
    public ApiResponse<List<StadiumsResponse>> stadiums() {
        List<StadiumsResponse> stadiumList = stadiumService.getStadiumList();
        return ApiResponse.ok(stadiumList);
    }

    @GetMapping("/stadiums/{stadiumId}/detail")
    public ApiResponse<StadiumDetailResponse> getStadiumDetailById(@PathVariable Long stadiumId) {
        StadiumDetailResponse stadiumDetailResponse = stadiumService.getStadiumDetail(stadiumId);
        return ApiResponse.ok(stadiumDetailResponse);
    }

    @GetMapping("/stadiums/search/name")
    public ApiResponse<List<StadiumsResponse>> getStadiumsByName(@RequestParam String query) {
        List<StadiumsResponse> stadiums = stadiumService.getStadiumsByName(query);
        return ApiResponse.ok(stadiums);
    }

    @GetMapping("/stadiums/search/address")
    public ApiResponse<List<StadiumsResponse>> getStadiumsByAddress(@RequestParam String query) {
        List<StadiumsResponse> stadiums = stadiumService.getStadiumsByAddress(query);
        return ApiResponse.ok(stadiums);
    }

    @PostMapping("/stadiums/search/location")
    public ApiResponse<List<StadiumsResponse>> getStadiumsByLocation(@Validated @RequestBody StadiumSearchByLocationRequest request) {
        List<StadiumsResponse> stadiums = stadiumService.getStadiumsWithinDistance(
                request.latitude(), request.longitude(), request.distance());
        return ApiResponse.ok(stadiums);
    }
}
