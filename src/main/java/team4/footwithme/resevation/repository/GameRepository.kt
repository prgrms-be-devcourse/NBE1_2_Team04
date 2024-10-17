package team4.footwithme.resevation.repository

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team4.footwithme.global.repository.CustomGlobalRepository
import team4.footwithme.resevation.domain.Game
import team4.footwithme.resevation.domain.GameStatus
import team4.footwithme.resevation.domain.Reservation
import java.util.*

@Repository
interface GameRepository : JpaRepository<Game?, Long?>, CustomGlobalRepository<Any?> {
    @Query("SELECT g FROM Game g WHERE g.isDeleted = 'FALSE' AND g.secondTeamReservation = :reservation AND g.gameStatus = :status")
    fun findBySecondReservationAndStatus(
        @Param("reservation") reservation: Reservation?,
        @Param("status") status: GameStatus?,
        pageRequest: PageRequest?
    ): Slice<Game?>

    @Query("SELECT g FROM Game g WHERE g.isDeleted = 'false' AND g.gameId = :id")
    override fun findActiveById(@Param("id") id: Long?): Optional<Any?>?

    @Modifying
    @Query("UPDATE Game g SET g.isDeleted = 'TRUE' WHERE g.secondTeamReservation = :reservation")
    fun softDeleteBySecondTeamReservation(@Param("reservation") reservation: Reservation?)

    @Query("select g.firstTeamReservation from Game g where g.isDeleted = 'false' and g.secondTeamReservation.reservationId = :reservationId")
    fun findFirstTeamReservationBySecondTeamReservationId(@Param("reservationId") secondTeamReservationId: Long?): Optional<Reservation?>

    @Query("select g from Game g where g.isDeleted = 'false' and g.firstTeamReservation.reservationId = :reservationId")
    fun findAllByReservationId(reservationId: Long?): List<Game?>?
}
