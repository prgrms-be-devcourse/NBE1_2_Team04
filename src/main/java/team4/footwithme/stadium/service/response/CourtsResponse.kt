package team4.footwithme.stadium.service.response

import team4.footwithme.stadium.domain.Court

@JvmRecord
data class CourtsResponse(
    val courtId: Long?,
    val stadiumId: Long?,
    val name: String?
) {
    companion object {
        fun from(court: Court?): CourtsResponse {
            return CourtsResponse(
                court!!.courtId,
                court.stadium!!.stadiumId,
                court.name
            )
        }
    }
}
