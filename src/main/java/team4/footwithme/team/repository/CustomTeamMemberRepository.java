package team4.footwithme.team.repository;

import lombok.RequiredArgsConstructor;
import team4.footwithme.team.domain.TeamMember;

import java.util.Optional;

public interface CustomTeamMemberRepository {
    Optional<TeamMember> findByTeamIdAndMemberId(Long teamId, Long memberId);
}
