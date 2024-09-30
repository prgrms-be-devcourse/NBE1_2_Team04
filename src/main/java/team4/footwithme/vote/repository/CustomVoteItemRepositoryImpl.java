package team4.footwithme.vote.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.vote.domain.VoteItem;

import java.util.List;

import static team4.footwithme.vote.domain.QVoteItem.voteItem;

@RequiredArgsConstructor
public class CustomVoteItemRepositoryImpl implements CustomVoteItemRepository {

    private final JPAQueryFactory queryFactory;

}
