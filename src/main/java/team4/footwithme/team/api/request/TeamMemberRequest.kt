package team4.footwithme.team.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import team4.footwithme.team.service.request.TeamMemberServiceRequest;

import java.util.List;

public record TeamMemberRequest(
    @Valid
    List<@Email String> emails
) {
    public TeamMemberServiceRequest toServiceRequest() {
        return new TeamMemberServiceRequest(
            emails//List
        );
    }
}
