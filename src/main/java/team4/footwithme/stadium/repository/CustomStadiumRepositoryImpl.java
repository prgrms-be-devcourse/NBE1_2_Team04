package team4.footwithme.stadium.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.stadium.domain.QStadium;
import team4.footwithme.stadium.domain.Stadium;

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

    @Override
    public List<Stadium> findStadiumsByLocation(Double latitude, Double longitude, Double distance) {
        QStadium stadium = QStadium.stadium;
        NumberTemplate<Double> haversineDistance = Expressions.numberTemplate(Double.class,
                "(6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1}))))",
                latitude, stadium.position.latitude, stadium.position.longitude, longitude);
        return queryFactory
                .selectFrom(stadium)
                .where(haversineDistance.loe(distance)
                        .and(stadium.isDeleted.eq(IsDeleted.FALSE))) // isDeleted 조건 유지
                .fetch();
    }

    @Override
    public String findStadiumNameById(Long stadiumId) {
        QStadium stadium = QStadium.stadium;
        return queryFactory.select(stadium.name)
                .from(stadium)
                .where(stadium.stadiumId.eq(stadiumId)
                        .and(stadium.isDeleted.eq(IsDeleted.FALSE))) // isDeleted 조건 추가
                .fetchOne();
    }
}
