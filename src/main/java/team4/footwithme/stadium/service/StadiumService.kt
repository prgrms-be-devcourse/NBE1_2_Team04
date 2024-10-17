package team4.footwithme.stadium.service

import org.springframework.data.domain.Slice
import team4.footwithme.member.domain.Member
import team4.footwithme.stadium.service.request.StadiumRegisterServiceRequest
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest
import team4.footwithme.stadium.service.request.StadiumUpdateServiceRequest
import team4.footwithme.stadium.service.response.StadiumDetailResponse
import team4.footwithme.stadium.service.response.StadiumsResponse

interface StadiumService {
    fun getStadiumList(page: Int?, sort: String?): Slice<StadiumsResponse>

    fun getStadiumDetail(id: Long?): StadiumDetailResponse

    fun getStadiumsByName(query: String?, page: Int?, sort: String?): Slice<StadiumsResponse>

    fun getStadiumsByAddress(address: String?, page: Int?, sort: String?): Slice<StadiumsResponse>

    fun getStadiumsWithinDistance(
        request: StadiumSearchByLocationServiceRequest?,
        page: Int?,
        sort: String?
    ): Slice<StadiumsResponse>

    fun registerStadium(request: StadiumRegisterServiceRequest?, member: Member?): StadiumDetailResponse

    fun updateStadium(request: StadiumUpdateServiceRequest?, member: Member?, stadiumId: Long?): StadiumDetailResponse

    fun deleteStadium(member: Member?, stadiumId: Long?)
}
