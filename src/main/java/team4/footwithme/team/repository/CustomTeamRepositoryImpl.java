package team4.footwithme.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.member.domain.Gender;
import team4.footwithme.member.domain.QMember;
import static team4.footwithme.team.domain.QTeamMember.teamMember;
import static team4.footwithme.member.domain.QMember.member;

import java.util.List;

@RequiredArgsConstructor
public class CustomTeamRepositoryImpl implements CustomTeamRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countMaleByMemberId() {
        return queryFactory
                .select(teamMember.count())
                .from(teamMember).join(member).on(teamMember.member.memberId.eq(member.memberId))
                .where(member.gender.eq(Gender.MALE).and(teamMember.isDeleted.eq(IsDeleted.FALSE)))
                .fetchOne();
    }

    @Override
    public Long countFemaleByMemberId() {
        return queryFactory
                .select(teamMember.count())
                .from(teamMember).join(member).on(teamMember.member.memberId.eq(member.memberId))
                .where(member.gender.eq(Gender.FEMALE).and(teamMember.isDeleted.eq(IsDeleted.FALSE)))
                .fetchOne();
    }
}
