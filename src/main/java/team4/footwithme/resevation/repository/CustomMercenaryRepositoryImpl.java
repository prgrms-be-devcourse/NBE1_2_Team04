package team4.footwithme.resevation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.resevation.domain.Mercenary;
import team4.footwithme.resevation.domain.ReservationStatus;

import java.util.List;

import static team4.footwithme.resevation.domain.QMercenary.mercenary;

@RequiredArgsConstructor
public class CustomMercenaryRepositoryImpl implements CustomMercenaryRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Mercenary> findAllToPage(Pageable pageable) {

        List<Mercenary> mercenaries = getMercenaryList(pageable);
        Long count = getCount();

        return new PageImpl<>(mercenaries, pageable, count);
    }

    private Long getCount() {
        return queryFactory
            .select(mercenary.count())
            .from(mercenary)
            .where(mercenary.isDeleted.eq(IsDeleted.FALSE)
                .and(mercenary.reservation.reservationStatus.eq(ReservationStatus.RECRUITING))
            )
            .fetchOne();
    }

    private List<Mercenary> getMercenaryList(Pageable pageable) {
        return queryFactory
            .select(mercenary)
            .from(mercenary)
            .where(mercenary.isDeleted.eq(IsDeleted.FALSE)
                .and(mercenary.reservation.reservationStatus.eq(ReservationStatus.RECRUITING))
            )
            .orderBy(mercenary.createdAt.desc())
            .offset(pageable.getOffset())   // 페이지 번호
            .limit(pageable.getPageSize() + 1)  // 페이지 사이즈
            .fetch();
    }
}
