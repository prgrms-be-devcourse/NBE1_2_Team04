package team4.footwithme.stadium.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Slice
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.stadium.api.request.validation.CourtAllowedValues
import team4.footwithme.stadium.service.CourtService
import team4.footwithme.stadium.service.response.CourtDetailResponse
import team4.footwithme.stadium.service.response.CourtsResponse

@RestController
@RequestMapping("/api/v1/court")
class CourtApi(private val courtService: CourtService) {
    @GetMapping("/")
    fun getAllCourts(
        @RequestParam(defaultValue = "0", required = false) page: Int?,
        @RequestParam(defaultValue = "COURT", required = false) @CourtAllowedValues sort: String?
    ): ApiResponse<Slice<CourtsResponse>?> {
        val courts = courtService.getAllCourts(page, sort)
        return ApiResponse.ok(courts)
    }


    @GetMapping("/{stadiumId}/courts")
    fun getCourtsByStadiumId(
        @PathVariable stadiumId: Long?,
        @RequestParam(defaultValue = "0", required = false) page: Int?,
        @RequestParam(defaultValue = "COURT", required = false) @CourtAllowedValues sort: String?
    ): ApiResponse<Slice<CourtsResponse>?> {
        val courts = courtService.getCourtsByStadiumId(stadiumId, page, sort)
        return ApiResponse.ok(courts)
    }

    @GetMapping("/{courtId}/detail")
    fun getCourtDetailById(@PathVariable courtId: Long?): ApiResponse<CourtDetailResponse?> {
        val court = courtService.getCourtByCourtId(courtId)
        return ApiResponse.ok(court)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CourtApi::class.java)
    }
}
