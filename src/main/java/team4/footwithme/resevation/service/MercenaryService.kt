package team4.footwithme.resevation.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import team4.footwithme.member.domain.Member
import team4.footwithme.resevation.service.request.MercenaryServiceRequest
import team4.footwithme.resevation.service.response.MercenaryResponse

interface MercenaryService {
    fun createMercenary(request: MercenaryServiceRequest?, member: Member?): MercenaryResponse

    fun getMercenary(mercenaryId: Long?): MercenaryResponse

    fun getMercenaries(pageable: Pageable?): Page<MercenaryResponse>

    fun deleteMercenary(mercenaryId: Long?, member: Member?): Long?
}
