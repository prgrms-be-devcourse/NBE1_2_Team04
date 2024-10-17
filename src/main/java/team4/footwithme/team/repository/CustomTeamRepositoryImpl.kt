package team4.footwithme.team.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import team4.footwithme.global.domain.IsDeleted
import team4.footwithme.member.domain.Gender
import team4.footwithme.member.domain.QMember
import team4.footwithme.team.domain.QTeamMember

class CustomTeamRepositoryImpl(private val queryFactory: JPAQueryFactory) : CustomTeamRepository {
    override fun countMaleByMemberId(teamId: Long?): Long? {
        return queryFactory
            .select(QTeamMember.teamMember.count())
            .from(QTeamMember.teamMember).join(QMember.member)
            .on(QTeamMember.teamMember.member.memberId.eq(QMember.member.memberId))
            .where(
                QMember.member.gender.eq(Gender.MALE)
                    .and(QTeamMember.teamMember.isDeleted.eq(IsDeleted.FALSE))
                    .and(QTeamMember.teamMember.team.teamId.eq(teamId))
            )
            .fetchOne()
    }

    override fun countFemaleByMemberId(teamId: Long?): Long? {
        return queryFactory
            .select(QTeamMember.teamMember.count())
            .from(QTeamMember.teamMember).join(QMember.member)
            .on(QTeamMember.teamMember.member.memberId.eq(QMember.member.memberId))
            .where(
                QMember.member.gender.eq(Gender.FEMALE)
                    .and(QTeamMember.teamMember.isDeleted.eq(IsDeleted.FALSE))
                    .and(QTeamMember.teamMember.team.teamId.eq(teamId))
            )
            .fetchOne()
    }
}
