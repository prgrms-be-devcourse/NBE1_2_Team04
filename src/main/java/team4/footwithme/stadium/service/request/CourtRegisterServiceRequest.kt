package team4.footwithme.stadium.service.request

import java.math.BigDecimal

@JvmRecord
data class CourtRegisterServiceRequest(
    val stadiumId: Long?,
    val name: String?,
    val description: String,
    val price_per_hour: BigDecimal?
)
