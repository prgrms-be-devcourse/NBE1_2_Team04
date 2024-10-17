package team4.footwithme.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import team4.footwithme.global.domain.IsDeleted;

import static team4.footwithme.member.domain.QMember.member;

public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;

    public CustomMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long findMemberIdByMemberEmail(String email) {
        return queryFactory.select(member.memberId)
            .from(member)
            .where(member.email.eq(email)
                .and(member.isDeleted.eq(IsDeleted.FALSE)))
            .fetchOne();
    }

    @Override
    public Boolean existByEmail(String email) {
        Integer count = queryFactory
            .selectOne()
            .from(member)
            .where(member.email.eq(email)
                .and(member.isDeleted.eq(IsDeleted.FALSE)))
            .fetchFirst();

        return count != null;
    }
}
