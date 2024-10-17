package team4.footwithme.team.service.response;

import team4.footwithme.team.domain.TeamMember;
import team4.footwithme.team.domain.TeamMemberRole;

public record TeamResponse(Long teamMemberId, Long teamId, String name, TeamMemberRole role) {
    public static TeamResponse of(TeamMember teamMember) {
        return new TeamResponse(
            teamMember.getTeamMemberId(),
            teamMember.getTeam().getTeamId(),
            teamMember.getMember().getName(),
            teamMember.getRole()
        );
    }
}
