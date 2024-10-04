package team4.footwithme.vote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.vote.domain.Vote;

import java.util.Optional;

import static team4.footwithme.vote.domain.QVote.vote;

@RequiredArgsConstructor
public class CustomVoteRepositoryImpl implements CustomVoteRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Vote> findNotDeletedVoteById(Long id) {
        return Optional.ofNullable(queryFactory.select(vote)
            .from(vote)
            .where(vote.voteId.eq(id)
                .and(vote.isDeleted.eq(IsDeleted.FALSE)))
            .fetchOne());
    }

}
