package team4.footwithme.team.repository;

import team4.footwithme.team.domain.Team;
import team4.footwithme.team.domain.TeamMember;

import java.util.List;

public interface CustomTeamRepository {
    Long countMaleByMemberId();
    Long countFemaleByMemberId();
}
