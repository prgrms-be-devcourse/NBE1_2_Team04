package team4.footwithme.stadium.service.request

@JvmRecord
data class StadiumRegisterServiceRequest(
    val name: String?,
    val address: String?,
    val phoneNumber: String?,
    val description: String,
    val latitude: Double?,
    val longitude: Double?
)