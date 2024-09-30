package team4.footwithme.team.api.request;

import team4.footwithme.team.service.request.TeamMemberServiceRequest;

import java.util.List;

public record TeamMemberRequest(
        List<String> emails
)
{
    public TeamMemberServiceRequest toServiceRequest(){
        return new TeamMemberServiceRequest(
            emails//List
        );
    }
}
