package team4.footwithme.stadium.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.stadium.service.CourtService;
import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/court")
public class CourtApi {

    private final CourtService courtService;

    @GetMapping("/")
    public ApiResponse<Slice<CourtsResponse>> getAllCourts(Pageable pageable) {
        Slice<CourtsResponse> courts = courtService.getAllCourts(pageable);
        return ApiResponse.ok(courts);
    }


    @GetMapping("/{stadiumId}/courts")
    public ApiResponse<Slice<CourtsResponse>> getCourtsByStadiumId(@PathVariable Long stadiumId, Pageable pageable) {
        Slice<CourtsResponse> courts = courtService.getCourtsByStadiumId(stadiumId, pageable);
        return ApiResponse.ok(courts);
    }

    @GetMapping("/{courtId}/detail")
    public ApiResponse<CourtDetailResponse> getCourtDetailById(@PathVariable Long courtId) {
        CourtDetailResponse court = courtService.getCourtByCourtId(courtId);
        return ApiResponse.ok(court);
    }

}
