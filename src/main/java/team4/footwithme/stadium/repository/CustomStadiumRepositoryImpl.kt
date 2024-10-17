package team4.footwithme.stadium.repository

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.NumberTemplate
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.*
import team4.footwithme.global.domain.IsDeleted
import team4.footwithme.stadium.domain.QStadium
import team4.footwithme.stadium.domain.Stadium

@RequiredArgsConstructor
class CustomStadiumRepositoryImpl : CustomStadiumRepository {
    private val queryFactory: JPAQueryFactory? = null

    override fun findStadiumsByLocation(
        latitude: Double,
        longitude: Double,
        distance: Double,
        pageable: Pageable
    ): Slice<Stadium> {
        val stadium = QStadium.stadium

        val haversineDistance = calculateHaversineDistance(latitude, longitude, stadium)

        val stadiums = fetchStadiumsByLocation(haversineDistance, distance, pageable)

        return createSlice(stadiums, pageable)
    }

    private fun calculateHaversineDistance(
        latitude: Double,
        longitude: Double,
        stadium: QStadium
    ): NumberTemplate<Double> {
        return Expressions.numberTemplate(
            Double::class.java,
            "(6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1}))))",
            latitude, stadium.position.latitude, stadium.position.longitude, longitude
        )
    }

    private fun fetchStadiumsByLocation(
        haversineDistance: NumberTemplate<Double>,
        distance: Double,
        pageable: Pageable
    ): List<Stadium> {
        val stadium = QStadium.stadium
        return queryFactory!!
            .selectFrom(stadium)
            .where(
                haversineDistance.loe(distance)
                    .and(stadium.isDeleted.eq(IsDeleted.FALSE))
            )
            .offset(pageable.offset)
            .limit((pageable.pageSize + 1).toLong())
            .fetch()
    }

    private fun createSlice(stadiums: List<Stadium>, pageable: Pageable): Slice<Stadium> {
        val mutableStadiums = stadiums.toMutableList()
        val hasNext = stadiums.size > pageable.pageSize
        if (hasNext) {
            mutableStadiums.removeAt(stadiums.size - 1)
        }
        return SliceImpl(stadiums, pageable, hasNext)
    }
}
