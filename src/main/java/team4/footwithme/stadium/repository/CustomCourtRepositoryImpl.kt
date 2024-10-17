package team4.footwithme.stadium.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import team4.footwithme.global.domain.IsDeleted
import team4.footwithme.stadium.domain.QCourt

@RequiredArgsConstructor
class CustomCourtRepositoryImpl : CustomCourtRepository {
    private val queryFactory: JPAQueryFactory? = null

    override fun findCourtNameByCourtId(courtId: Long?): String? {
        return queryFactory!!.select(QCourt.court.name)
            .from(QCourt.court)
            .where(
                QCourt.court.courtId.eq(courtId)
                    .and(QCourt.court.isDeleted.eq(IsDeleted.FALSE))
            )
            .fetchOne()
    }

    override fun countCourtByCourtIds(courtIds: List<Long?>?): Long? {
        return queryFactory!!.select(QCourt.court.count())
            .from(QCourt.court)
            .where(
                QCourt.court.courtId.`in`(courtIds)
                    .and(QCourt.court.isDeleted.eq(IsDeleted.FALSE))
            )
            .fetchOne()
    }
}
