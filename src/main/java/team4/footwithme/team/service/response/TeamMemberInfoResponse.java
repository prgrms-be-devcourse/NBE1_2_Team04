package team4.footwithme.team.service.response;

import team4.footwithme.member.domain.Member;
import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMemberRole;

public record TeamMemberInfoResponse(
        /**
         * teamMember-id
         * member-이름
         * member-이메일
         * member-성별
         * teamMember - role
         */
        Long teamMemberId,
        String teamName,
        String name,
        String email,
        TeamMemberRole role
) {
    public static TeamMemberInfoResponse of(Team team, Long teamMemberId, Member member, TeamMemberRole role){
        return new TeamMemberInfoResponse(
                teamMemberId,
                team.getName(),
                member.getName(),
                member.getEmail(),
                role
        );
    }
}
