package team4.footwithme.vote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.vote.domain.QChoice;

import java.util.List;

import static team4.footwithme.vote.domain.QChoice.*;

@RequiredArgsConstructor
public class CustomChoiceRepositoryImpl implements CustomChoiceRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countByVoteItemId(Long voteItemId) {
        return queryFactory.select(choice.count())
            .from(choice)
            .where(choice.voteItemId.eq(voteItemId))
            .fetchOne();
    }
}