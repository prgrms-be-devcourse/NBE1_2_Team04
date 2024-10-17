package team4.footwithme.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.member.domain.Gender;

import static team4.footwithme.member.domain.QMember.member;
import static team4.footwithme.team.domain.QTeamMember.teamMember;

public class CustomTeamRepositoryImpl implements CustomTeamRepository {

    private final JPAQueryFactory queryFactory;

    public CustomTeamRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long countMaleByMemberId(Long teamId) {
        return queryFactory
            .select(teamMember.count())
            .from(teamMember).join(member).on(teamMember.member.memberId.eq(member.memberId))
            .where(member.gender.eq(Gender.MALE)
                .and(teamMember.isDeleted.eq(IsDeleted.FALSE))
                .and(teamMember.team.teamId.eq(teamId)))
            .fetchOne();
    }

    @Override
    public Long countFemaleByMemberId(Long teamId) {
        return queryFactory
            .select(teamMember.count())
            .from(teamMember).join(member).on(teamMember.member.memberId.eq(member.memberId))
            .where(member.gender.eq(Gender.FEMALE)
                .and(teamMember.isDeleted.eq(IsDeleted.FALSE))
                .and(teamMember.team.teamId.eq(teamId)))
            .fetchOne();
    }
}
