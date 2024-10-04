package team4.footwithme.vote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.vote.domain.Vote;

import java.util.List;
import java.util.Optional;

import static team4.footwithme.global.domain.IsDeleted.*;
import static team4.footwithme.vote.domain.QVote.vote;
import static team4.footwithme.vote.domain.VoteStatus.*;

@RequiredArgsConstructor
public class CustomVoteRepositoryImpl implements CustomVoteRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Vote> findNotDeletedVoteById(Long id) {
        return Optional.ofNullable(queryFactory.select(vote)
            .from(vote)
            .where(vote.voteId.eq(id)
                .and(vote.isDeleted.eq(FALSE)))
            .fetchOne());
    }

    @Override
    public List<Vote> findOpenedVotes() {
        return queryFactory.select(vote)
            .from(vote)
            .where(vote.isDeleted.eq(FALSE)
                .and(vote.voteStatus.eq(OPENED)))
            .fetch();
    }
}
