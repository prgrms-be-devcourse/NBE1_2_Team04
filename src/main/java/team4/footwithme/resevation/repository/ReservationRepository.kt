package team4.footwithme.resevation.repository

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team4.footwithme.global.repository.CustomGlobalRepository
import team4.footwithme.resevation.domain.Reservation
import team4.footwithme.resevation.domain.ReservationStatus
import team4.footwithme.stadium.domain.Court
import java.time.LocalDateTime
import java.util.*

@Repository
interface ReservationRepository : JpaRepository<Reservation?, Long?>, CustomGlobalRepository<Any?> {
    @Query("SELECT r FROM Reservation r WHERE r.isDeleted = 'false' AND r.reservationId = :id")
    override fun findActiveById(@Param("id") id: Long?): Optional<Any?>?

    @Query("SELECT r FROM Reservation r WHERE r.isDeleted = 'FALSE' AND r.matchDate = :matchDate AND r.court = :court AND r.reservationStatus = :reservationStatus AND r.reservationId != :id")
    fun findByMatchDateAndCourtAndReservationStatus(
        @Param("id") id: Long?,
        @Param("matchDate") matchDate: LocalDateTime?,
        @Param("court") court: Court?,
        @Param("reservationStatus") reservationStatus: ReservationStatus?,
        pageRequest: PageRequest?
    ): Slice<Reservation?>

    @Query("select r from Reservation r where r.isDeleted = 'false' and r.reservationId = :id")
    fun findByReservationId(@Param("id") reservationId: Long?): Optional<Reservation?>

    @Query("select r from Reservation r where r.isDeleted = 'false' and r.team.teamId = :teamId")
    fun findByTeamTeamId(@Param("teamId") teamId: Long?): List<Reservation?>?
}
