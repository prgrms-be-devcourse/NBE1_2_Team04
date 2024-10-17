package team4.footwithme.stadium.api.request

import jakarta.validation.constraints.NotNull
import team4.footwithme.stadium.service.request.CourtDeleteServiceRequest

@JvmRecord
data class CourtDeleteRequest(
    stadiumId: @NotNull(message = "풋살장 아이디는 필수입니다.") Long?
) {
    fun toServiceRequest(): CourtDeleteServiceRequest {
        return CourtDeleteServiceRequest(stadiumId)
    }

    val stadiumId: @NotNull(message = "풋살장 아이디는 필수입니다.") Long? = stadiumId
}
