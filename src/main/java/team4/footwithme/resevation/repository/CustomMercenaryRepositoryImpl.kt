package team4.footwithme.resevation.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import team4.footwithme.global.domain.IsDeleted
import team4.footwithme.resevation.domain.Mercenary
import team4.footwithme.resevation.domain.QMercenary
import team4.footwithme.resevation.domain.ReservationStatus

class CustomMercenaryRepositoryImpl(private val queryFactory: JPAQueryFactory) : CustomMercenaryRepository {
    override fun findAllToPage(pageable: Pageable): Page<Mercenary> {
        val mercenaries = getMercenaryList(pageable)
        val count = count

        return PageImpl(mercenaries, pageable, count!!)
    }

    private val count: Long?
        get() = queryFactory
            .select(QMercenary.mercenary.count())
            .from(QMercenary.mercenary)
            .where(
                QMercenary.mercenary.isDeleted.eq(IsDeleted.FALSE)
                    .and(QMercenary.mercenary.reservation.reservationStatus.eq(ReservationStatus.RECRUITING))
            )
            .fetchOne()

    private fun getMercenaryList(pageable: Pageable): List<Mercenary> {
        return queryFactory
            .select(QMercenary.mercenary)
            .from(QMercenary.mercenary)
            .where(
                QMercenary.mercenary.isDeleted.eq(IsDeleted.FALSE)
                    .and(QMercenary.mercenary.reservation.reservationStatus.eq(ReservationStatus.RECRUITING))
            )
            .orderBy(QMercenary.mercenary.createdAt.desc())
            .offset(pageable.offset) // 페이지 번호
            .limit((pageable.pageSize + 1).toLong()) // 페이지 사이즈
            .fetch()
    }
}
