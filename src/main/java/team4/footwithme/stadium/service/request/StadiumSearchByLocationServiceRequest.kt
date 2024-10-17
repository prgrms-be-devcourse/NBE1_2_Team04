package team4.footwithme.stadium.service.request

@JvmRecord
data class StadiumSearchByLocationServiceRequest(
    val latitude: Double?,
    val longitude: Double?,
    val distance: Double?
)
