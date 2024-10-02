package team4.footwithme.team.repository;

import lombok.RequiredArgsConstructor;
import team4.footwithme.team.domain.TeamMember;

public interface CustomTeamMemberRepository {
    TeamMember findByTeamIdAndMemberId(Long teamId, Long memberId);
}
