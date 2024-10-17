package team4.footwithme.stadium.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import team4.footwithme.stadium.domain.Stadium

interface CustomStadiumRepository {
    fun findStadiumsByLocation(
        latitude: Double,
        longitude: Double,
        distance: Double,
        pageable: Pageable
    ): Slice<Stadium>
}
