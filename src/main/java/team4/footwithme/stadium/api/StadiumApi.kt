package team4.footwithme.stadium.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Slice
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.stadium.api.request.StadiumSearchByLocationRequest
import team4.footwithme.stadium.api.request.validation.StadiumAllowedValues
import team4.footwithme.stadium.service.StadiumService
import team4.footwithme.stadium.service.response.StadiumDetailResponse
import team4.footwithme.stadium.service.response.StadiumsResponse

@RestController
@RequestMapping("/api/v1/stadium")
class StadiumApi(private val stadiumService: StadiumService) {
    @GetMapping("/stadiums")
    fun stadiums(
        @RequestParam(defaultValue = "0", required = false) page: Int?,
        @RequestParam(defaultValue = "STADIUM", required = false) @StadiumAllowedValues sort: String?
    ): ApiResponse<Slice<StadiumsResponse>?> {
        val stadiumList = stadiumService.getStadiumList(page, sort)
        return ApiResponse.ok(stadiumList)
    }

    @GetMapping("/stadiums/{stadiumId}/detail")
    fun getStadiumDetailById(@PathVariable stadiumId: Long?): ApiResponse<StadiumDetailResponse?> {
        val stadiumDetailResponse = stadiumService.getStadiumDetail(stadiumId)
        return ApiResponse.ok(stadiumDetailResponse)
    }

    @GetMapping("/stadiums/search/name")
    fun getStadiumsByName(
        @RequestParam query: String?,
        @RequestParam(defaultValue = "0", required = false) page: Int?,
        @RequestParam(defaultValue = "STADIUM", required = false) @StadiumAllowedValues sort: String?
    ): ApiResponse<Slice<StadiumsResponse>?> {
        val stadiums = stadiumService.getStadiumsByName(query, page, sort)
        return ApiResponse.ok(stadiums)
    }

    @GetMapping("/stadiums/search/address")
    fun getStadiumsByAddress(
        @RequestParam query: String?, @RequestParam(defaultValue = "0", required = false) page: Int?,
        @RequestParam(defaultValue = "STADIUM", required = false) @StadiumAllowedValues sort: String?
    ): ApiResponse<Slice<StadiumsResponse>?> {
        val stadiums = stadiumService.getStadiumsByAddress(query, page, sort)
        return ApiResponse.ok(stadiums)
    }

    @PostMapping("/stadiums/search/location")
    fun getStadiumsByLocation(
        @Validated @RequestBody request: StadiumSearchByLocationRequest,
        @RequestParam(defaultValue = "0", required = false) page: Int?,
        @RequestParam(defaultValue = "STADIUM", required = false) @StadiumAllowedValues sort: String?
    ): ApiResponse<Slice<StadiumsResponse>?> {
        val stadiums = stadiumService.getStadiumsWithinDistance(request.toServiceRequest(), page, sort)
        return ApiResponse.ok(stadiums)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(StadiumApi::class.java)
    }
}
