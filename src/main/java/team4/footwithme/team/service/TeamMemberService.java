package team4.footwithme.team.service;

import team4.footwithme.team.service.request.TeamMemberServiceRequest;
import team4.footwithme.team.service.response.TeamMemberInfoResponse;

import java.util.List;

public interface TeamMemberService {
    List<TeamMemberInfoResponse> addTeamMembers(Long teamId, TeamMemberServiceRequest request);
}
