package team4.footwithme.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import team4.footwithme.global.domain.IsDeleted;
import team4.footwithme.team.domain.TeamMember;

import java.util.Optional;

import static team4.footwithme.team.domain.QTeamMember.teamMember;

public class CustomTeamMemberRepositoryImpl implements CustomTeamMemberRepository {

    private final JPAQueryFactory queryFactory;

    public CustomTeamMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

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
