package team4.footwithme.stadium.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team4.footwithme.global.repository.CustomGlobalRepository
import team4.footwithme.stadium.domain.Stadium
import java.util.*

@Repository
interface StadiumRepository : JpaRepository<Stadium?, Long?>, CustomStadiumRepository, CustomGlobalRepository<Any?> {
    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND s.stadiumId = :id")
    fun findByStadiumId(@Param("id") id: Long?): Optional<Stadium?>?

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND s.stadiumId = :id")
    override fun findActiveById(@Param("id") id: Long?): Optional<Any?>

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND s.name = :name")
    fun findActiveByName(@Param("name") name: String?): Optional<Stadium?>?

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false'")
    fun findAllActiveStadiums(pageable: Pageable?): Slice<Stadium?>

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    fun findByNameContainingIgnoreCase(@Param("query") query: String?, pageable: Pageable?): Slice<Stadium?>

    @Query("SELECT s FROM Stadium s WHERE s.isDeleted = 'false' AND LOWER(s.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    fun findByAddressContainingIgnoreCase(@Param("address") address: String?, pageable: Pageable?): Slice<Stadium?>
}
