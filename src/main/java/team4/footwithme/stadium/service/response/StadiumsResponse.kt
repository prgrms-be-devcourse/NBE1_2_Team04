package team4.footwithme.stadium.service.response

import team4.footwithme.stadium.domain.Stadium

@JvmRecord
data class StadiumsResponse(
    val stadiumId: Long?,
    val name: String?,
    val address: String?) {

    companion object {
        fun from(stadium: Stadium?): StadiumsResponse {
            return StadiumsResponse(
                stadium!!.stadiumId,
                stadium.name,
                stadium.address
            )
        }
    }
}
