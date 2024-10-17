package team4.footwithme.stadium.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.stadium.domain.QStadium;
import team4.footwithme.stadium.domain.Stadium;

import java.util.List;

@RequiredArgsConstructor
public class CustomStadiumRepositoryImpl implements CustomStadiumRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Stadium> findStadiumsByLocation(Double latitude, Double longitude, Double distance, Pageable pageable) {
        QStadium stadium = QStadium.stadium;

        NumberTemplate<Double> haversineDistance = calculateHaversineDistance(latitude, longitude, stadium);

        List<Stadium> stadiums = fetchStadiumsByLocation(haversineDistance, distance, pageable);

        return createSlice(stadiums, pageable);
    }

    private NumberTemplate<Double> calculateHaversineDistance(Double latitude, Double longitude, QStadium stadium) {
        return Expressions.numberTemplate(Double.class,
            "(6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1}))))",
            latitude, stadium.position.latitude, stadium.position.longitude, longitude);
    }

    private List<Stadium> fetchStadiumsByLocation(NumberTemplate<Double> haversineDistance, Double distance, Pageable pageable) {
        QStadium stadium = QStadium.stadium;
        return queryFactory
            .selectFrom(stadium)
            .where(haversineDistance.loe(distance)
                .and(stadium.isDeleted.eq(IsDeleted.FALSE)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();
    }

    private Slice<Stadium> createSlice(List<Stadium> stadiums, Pageable pageable) {
        boolean hasNext = stadiums.size() > pageable.getPageSize();
        if (hasNext) {
            stadiums.remove(stadiums.size() - 1);
        }
        return new SliceImpl<>(stadiums, pageable, hasNext);
    }
}
