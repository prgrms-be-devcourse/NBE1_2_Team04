package team4.footwithme.team.service;

import team4.footwithme.team.service.request.TeamMemberServiceRequest;
import team4.footwithme.team.service.response.TeamResponse;

import java.util.List;

public interface TeamMemberService {
    List<TeamResponse> addTeamMembers(Long teamId, TeamMemberServiceRequest request);
    List<TeamResponse> getTeamMembers(Long teamId);
}
