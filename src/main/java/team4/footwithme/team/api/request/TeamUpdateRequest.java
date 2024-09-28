package team4.footwithme.team.api.request;

import team4.footwithme.team.service.request.TeamUpdateServiceRequest;

public record TeamUpdateRequest(String name, String description, String location) {
    public TeamUpdateServiceRequest toServiceRequest() {
        return new TeamUpdateServiceRequest(
                name, description, location
        );
    }
}
