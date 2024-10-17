package team4.footwithme.stadium.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.global.domain.IsDeleted;

import java.util.List;

import static team4.footwithme.stadium.domain.QCourt.court;

@RequiredArgsConstructor
public class CustomCourtRepositoryImpl implements CustomCourtRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public String findCourtNameByCourtId(Long courtId) {
        return queryFactory.select(court.name)
            .from(court)
            .where(court.courtId.eq(courtId)
                .and(court.isDeleted.eq(IsDeleted.FALSE)))
            .fetchOne();
    }

    @Override
    public Long countCourtByCourtIds(List<Long> courtIds) {
        return queryFactory.select(court.count())
            .from(court)
            .where(court.courtId.in(courtIds)
                .and(court.isDeleted.eq(IsDeleted.FALSE)))
            .fetchOne();
    }

}
