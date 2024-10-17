package team4.footwithme.stadium.repository

interface CustomCourtRepository {
    fun findCourtNameByCourtId(courtId: Long?): String?

    fun countCourtByCourtIds(courtIds: List<Long?>?): Long?
}
