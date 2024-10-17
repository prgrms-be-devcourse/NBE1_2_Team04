package team4.footwithme.stadium.service.request

@JvmRecord
data class StadiumUpdateServiceRequest(
    val name: String?,
    val address: String?,
    val phoneNumber: String?,
    val description: String,
    val latitude: Double?,
    val longitude: Double?
)
