package team4.footwithme.stadium.api.request;

import jakarta.validation.constraints.NotNull;
import team4.footwithme.stadium.service.request.CourtDeleteServiceRequest;

public record CourtDeleteRequest(
    @NotNull(message = "풋살장 아이디는 필수입니다.")
    Long stadiumId
) {
    public CourtDeleteServiceRequest toServiceRequest() {
        return new CourtDeleteServiceRequest(stadiumId);
    }
}
