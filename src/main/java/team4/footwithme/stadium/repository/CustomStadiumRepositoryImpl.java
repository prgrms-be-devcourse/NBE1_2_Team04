package team4.footwithme.stadium.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.global.domain.IsDeleted;

import java.util.List;

import static team4.footwithme.stadium.domain.QStadium.stadium;

@RequiredArgsConstructor
public class CustomStadiumRepositoryImpl implements CustomStadiumRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findStadiumNamesByStadiumIds(List<Long> stadiumIdList) {
        return queryFactory.select(stadium.name)
            .from(stadium)
            .where(stadium.stadiumId.in(stadiumIdList)
                .and(stadium.isDeleted.eq(IsDeleted.FALSE)))
            .fetch();
    }

    @Override
    public Long countStadiumByStadiumIds(List<Long> stadiumIds) {
        return queryFactory.select(stadium.count())
            .from(stadium)
            .where(stadium.stadiumId.in(stadiumIds)
                .and(stadium.isDeleted.eq(IsDeleted.FALSE)))
            .fetchOne();
    }
}
