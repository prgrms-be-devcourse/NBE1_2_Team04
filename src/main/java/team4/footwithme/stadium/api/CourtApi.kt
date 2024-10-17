package team4.footwithme.stadium.api;

import org.slf4j.Logger;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.stadium.api.request.validation.CourtAllowedValues;
import team4.footwithme.stadium.service.CourtService;
import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;

@RestController
@RequestMapping("/api/v1/court")
public class CourtApi {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CourtApi.class);
    private final CourtService courtService;

    public CourtApi(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping("/")
    public ApiResponse<Slice<CourtsResponse>> getAllCourts(
        @RequestParam(defaultValue = "0", required = false) Integer page,
        @RequestParam(defaultValue = "COURT", required = false) @CourtAllowedValues String sort) {
        Slice<CourtsResponse> courts = courtService.getAllCourts(page, sort);
        return ApiResponse.ok(courts);
    }


    @GetMapping("/{stadiumId}/courts")
    public ApiResponse<Slice<CourtsResponse>> getCourtsByStadiumId(
        @PathVariable Long stadiumId,
        @RequestParam(defaultValue = "0", required = false) Integer page,
        @RequestParam(defaultValue = "COURT", required = false) @CourtAllowedValues String sort) {
        Slice<CourtsResponse> courts = courtService.getCourtsByStadiumId(stadiumId, page, sort);
        return ApiResponse.ok(courts);
    }

    @GetMapping("/{courtId}/detail")
    public ApiResponse<CourtDetailResponse> getCourtDetailById(@PathVariable Long courtId) {
        CourtDetailResponse court = courtService.getCourtByCourtId(courtId);
        return ApiResponse.ok(court);
    }

}
