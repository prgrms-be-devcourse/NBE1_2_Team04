package team4.footwithme.stadium.service.response

import team4.footwithme.stadium.domain.Stadium

@JvmRecord
data class StadiumDetailResponse(
    val stadiumId: Long?,
    val memberId: Long?,
    val name: String?,
    val address: String?,
    val phoneNumber: String?,
    val description: String?,
    val latitude: Double,
    val longitude: Double
) {

    companion object {
        fun from(stadium: Stadium): StadiumDetailResponse {
            return StadiumDetailResponse(
                stadium.stadiumId,
                stadium.member!!.memberId,
                stadium.name,
                stadium.address,
                stadium.phoneNumber,
                stadium.description,
                stadium.position!!.latitude,
                stadium.position!!.longitude
            )
        }
    }
}
