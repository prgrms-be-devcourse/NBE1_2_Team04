package team4.footwithme.vote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomVoteRepositoryImpl implements CustomVoteRepository {

    private final JPAQueryFactory queryFactory;

}
