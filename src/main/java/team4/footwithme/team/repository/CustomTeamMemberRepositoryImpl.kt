package team4.footwithme.team.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import team4.footwithme.global.domain.IsDeleted
import team4.footwithme.team.domain.QTeamMember
import team4.footwithme.team.domain.TeamMember
import java.util.*

class CustomTeamMemberRepositoryImpl(private val queryFactory: JPAQueryFactory) : CustomTeamMemberRepository {
    override fun findByTeamIdAndMemberId(teamId: Long?, memberId: Long?): Optional<TeamMember> {
        return Optional.ofNullable(
            queryFactory
                .select(QTeamMember.teamMember)
                .from(QTeamMember.teamMember)
                .where(
                    QTeamMember.teamMember.team.teamId.eq(teamId)
                        .and(QTeamMember.teamMember.member.memberId.eq(memberId))
                        .and(QTeamMember.teamMember.team.isDeleted.eq(IsDeleted.FALSE))
                )
                .fetchOne()
        )
    }
}
