package team4.footwithme.chat.service.event;

import team4.footwithme.team.domain.TeamMember;

import java.util.List;

public record TeamMembersJoinEvent(
    List<TeamMember> members,
    Long teamId
) {
}
