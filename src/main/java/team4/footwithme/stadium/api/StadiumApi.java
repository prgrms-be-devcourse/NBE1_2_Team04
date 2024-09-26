package team4.footwithme.stadium.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.stadium.api.response.StadiumDetailResponse;
import team4.footwithme.stadium.api.response.StadiumsResponse;
import team4.footwithme.stadium.domain.Stadium;
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
    public ApiResponse<List<StadiumsResponse>> autocomplete(@RequestParam String query) {
        List<StadiumsResponse> stadiums = stadiumService.searchStadiumByName(query);
        return ApiResponse.ok(stadiums);
    }


}
