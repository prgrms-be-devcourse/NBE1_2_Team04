package team4.footwithme.resevation.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team4.footwithme.resevation.domain.Mercenary
import java.util.*

@Repository
interface MercenaryRepository : JpaRepository<Mercenary?, Long?>, CustomMercenaryRepository {
    @Query("select m from Mercenary m where m.isDeleted = 'false' and m.mercenaryId = :id")
    fun findByMercenaryId(@Param("id") mercenaryId: Long?): Optional<Mercenary>

    @Query("select mer from Mercenary mer where mer.isDeleted = 'false' and mer.reservation.reservationId = :reservationId")
    fun findAllMercenaryByReservationId(reservationId: Long?): List<Mercenary?>?
}
