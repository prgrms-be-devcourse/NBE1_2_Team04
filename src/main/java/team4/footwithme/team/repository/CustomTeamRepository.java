package team4.footwithme.team.repository;

import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;

import java.util.List;

public interface CustomTeamRepository {
    Long countMaleByMemberId(List<TeamMember> teamMembers);
    Long countFemaleByMemberId(List<TeamMember> teamMembers);
}
