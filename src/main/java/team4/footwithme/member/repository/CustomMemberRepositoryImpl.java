package team4.footwithme.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.global.domain.IsDeleted;

import static team4.footwithme.member.domain.QMember.member;

@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long findMemberIdByMemberEmail(String email) {
        return queryFactory.select(member.memberId)
            .from(member)
            .where(member.email.eq(email)
                .and(member.isDeleted.eq(IsDeleted.FALSE)))
            .fetchOne();
    }
}
