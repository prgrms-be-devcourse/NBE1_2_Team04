package team4.footwithme.stadium.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.service.CourtService;
import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/court")
public class CourtApi {

    private final CourtService courtService;

    @GetMapping("/")
    public ApiResponse<List<CourtsResponse>> getAllCourts() {
        List<CourtsResponse> courts = courtService.getAllCourts();
        return ApiResponse.ok(courts);
    }


    @GetMapping("/{stadiumId}/courts")
    public ApiResponse<List<CourtsResponse>> getCourtsByStadiumId(@PathVariable Long stadiumId) {
        List<CourtsResponse> courts = courtService.getCourtsByStadiumId(stadiumId);
        return ApiResponse.ok(courts);
    }

    @GetMapping("/{courtId}/detail")
    public ApiResponse<CourtDetailResponse> getCourtDetailById(@PathVariable Long courtId) {
        CourtDetailResponse court = courtService.getCourtBycourtId(courtId);
        return ApiResponse.ok(court);
    }

}
