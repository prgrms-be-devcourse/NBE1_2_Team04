package team4.footwithme.stadium.service

import org.springframework.data.domain.Slice
import team4.footwithme.member.domain.Member
import team4.footwithme.stadium.service.request.CourtDeleteServiceRequest
import team4.footwithme.stadium.service.request.CourtRegisterServiceRequest
import team4.footwithme.stadium.service.request.CourtUpdateServiceRequest
import team4.footwithme.stadium.service.response.CourtDetailResponse
import team4.footwithme.stadium.service.response.CourtsResponse

interface CourtService {
    fun getCourtsByStadiumId(stadiumId: Long?, page: Int?, sort: String?): Slice<CourtsResponse>

    fun getAllCourts(page: Int?, sort: String?): Slice<CourtsResponse>

    fun getCourtByCourtId(courtId: Long?): CourtDetailResponse

    fun registerCourt(request: CourtRegisterServiceRequest?, member: Member?): CourtDetailResponse

    fun updateCourt(request: CourtUpdateServiceRequest?, member: Member?, courtId: Long?): CourtDetailResponse

    fun deleteCourt(request: CourtDeleteServiceRequest?, member: Member?, courtId: Long?)
}
