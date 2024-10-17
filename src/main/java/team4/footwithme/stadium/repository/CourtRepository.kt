package team4.footwithme.stadium.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team4.footwithme.global.repository.CustomGlobalRepository
import team4.footwithme.stadium.domain.Court
import java.util.*

@Repository
interface CourtRepository : JpaRepository<Court?, Long?>, CustomGlobalRepository<Any?>, CustomCourtRepository {
    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false' AND c.stadium.stadiumId = :stadiumId")
    fun findByStadium_StadiumId(@Param("stadiumId") stadiumId: Long?, pageable: Pageable?): Slice<Court?>?

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false'")
    fun findAllActive(pageable: Pageable?): Slice<Court?>?

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false' AND c.courtId = :id")
    fun findByCourtId(@Param("id") id: Long?): Optional<Court?>?

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false' AND c.courtId = :id")
    override fun findActiveById(@Param("id") id: Long?): Optional<Any?>

    @Query("SELECT c FROM Court c WHERE c.isDeleted = 'false' AND c.stadium.stadiumId = :id")
    fun findActiveByStadiumId(@Param("id") id: Long?): List<Court?>?
}
