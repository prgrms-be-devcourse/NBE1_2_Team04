package team4.footwithme.stadium.service.response

import team4.footwithme.stadium.domain.Court
import java.math.BigDecimal

@JvmRecord
data class CourtDetailResponse(
    val courtId: Long?,
    val stadiumId: Long?,
    val name: String?,
    val description: String?,
    val price_per_hour: BigDecimal?
) {
    companion object {
        fun from(court: Court): CourtDetailResponse {
            return CourtDetailResponse(
                court.courtId,
                court.stadium!!.stadiumId,
                court.name,
                court.description,
                court.pricePerHour
            )
        }
    }
}
