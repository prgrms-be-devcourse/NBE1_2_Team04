package team4.footwithme.stadium.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team4.footwithme.stadium.domain.QStadium;
import team4.footwithme.stadium.domain.Stadium;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StadiumRepositoryCustomImpl implements StadiumRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Stadium> findStadiumsByLocation(Double latitude, Double longitude, Double distance) {
        QStadium stadium = QStadium.stadium;

        double earthRadius = 6371;

        NumberTemplate<Double> haversineDistance = Expressions.numberTemplate(Double.class,
                "(6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1}))))",
                latitude, stadium.position.latitude, stadium.position.longitude, longitude);

        return queryFactory
                .selectFrom(stadium)
                .where(haversineDistance.loe(distance))
                .fetch();
    }
}