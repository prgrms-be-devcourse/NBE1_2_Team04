package team4.footwithme.resevation.service.request

@JvmRecord
data class GameRegisterServiceRequest(
    val firstReservationId: Long?,
    val secondReservationId: Long?
) {
}
