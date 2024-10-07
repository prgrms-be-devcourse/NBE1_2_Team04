package team4.footwithme.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.team.domain.TeamMember;

import java.util.Optional;

import static team4.footwithme.team.domain.QTeam.team;
import static team4.footwithme.member.domain.QMember.member;
import static team4.footwithme.team.domain.QTeamMember.teamMember;

@RequiredArgsConstructor
public class CustomTeamMemberRepositoryImpl implements CustomTeamMemberRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<TeamMember> findByTeamIdAndMemberId(Long teamId, Long memberId) {
        return Optional.ofNullable(
                queryFactory
                .select(teamMember)
                .from(teamMember)
                .where(teamMember.team.teamId.eq(teamId)
                    .and(teamMember.member.memberId.eq(memberId))
                        .and(teamMember.team.isDeleted.eq(IsDeleted.FALSE)))
                .fetchOne()
            );
    }

}
