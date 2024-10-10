package team4.footwithme.team.api.request;

import jakarta.validation.constraints.Null;
import team4.footwithme.team.service.request.TeamDefaultServiceRequest;

public record TeamUpdateRequest(
    @Null
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
