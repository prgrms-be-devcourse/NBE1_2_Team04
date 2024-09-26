package team4.footwithme.stadium.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static team4.footwithme.stadium.domain.QStadium.stadium;

@RequiredArgsConstructor
public class CustomStadiumRepositoryImpl implements CustomStadiumRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findStadiumNamesByStadiumIds(List<Long> stadiumIdList) {
        return queryFactory.select(stadium.name)
            .from(stadium)
            .where(stadium.stadiumId.in(stadiumIdList))
            .fetch();
    }

}
