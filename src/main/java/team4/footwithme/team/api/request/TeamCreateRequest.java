package team4.footwithme.team.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import team4.footwithme.team.service.request.TeamDefaultServiceRequest;

public record TeamCreateRequest(
        @NotNull(message = "팀 명은 필수입니다.")
        String name,
        @Null
        String description,
        @Null
        String location
) {
    public TeamDefaultServiceRequest toServiceRequest() {
        return new TeamDefaultServiceRequest(
            name, description, location
        );
    }
}
