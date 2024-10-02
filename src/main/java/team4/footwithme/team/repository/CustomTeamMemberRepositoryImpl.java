package team4.footwithme.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.team.domain.TeamMember;

import static team4.footwithme.team.domain.QTeam.team;
import static team4.footwithme.member.domain.QMember.member;
import static team4.footwithme.team.domain.QTeamMember.teamMember;

@RequiredArgsConstructor
public class CustomTeamMemberRepositoryImpl implements CustomTeamMemberRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public TeamMember findByTeamIdAndMemberId(Long teamId, Long memberId) {
        return queryFactory
                .select(teamMember)
                .from(teamMember)
                .where(teamMember.team.teamId.eq(teamId)
                    .and(teamMember.member.memberId.eq(memberId)))
                .fetchOne();
    }


}
