package team4.footwithme.resevation.service.request

@JvmRecord
data class MercenaryServiceRequest(
    val reservationId: Long?,
    val description: String
) {
}
