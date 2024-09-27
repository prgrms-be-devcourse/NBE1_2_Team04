package team4.footwithme.team.api.request;

import team4.footwithme.team.service.request.TeamCreateServiceRequest;

public record TeamCreateRequest(String name, String description, String location) {
    public TeamCreateServiceRequest toServiceRequest() {
        return new TeamCreateServiceRequest(
            name, description, location
        );
    }
}
