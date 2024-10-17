package team4.footwithme.stadium.api.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;

public record StadiumSearchByLocationRequest(
    @NotNull(message = "위도 값은 필수 입력 항목입니다.")
    @Min(value = -90, message = "위도 값은 -90도 이상이어야 합니다.")
    @Max(value = 90, message = "위도 값은 90도 이하이어야 합니다.")
    Double latitude,

    @NotNull(message = "경도 값은 필수 입력 항목입니다.")
    @Min(value = -180, message = "경도 값은 -180도 이상이어야 합니다.")
    @Max(value = 180, message = "경도 값은 180도 이하이어야 합니다.")
    Double longitude,

    @NotNull(message = "거리 값은 필수 입력 항목입니다.")
    @Min(value = 0, message = "거리는 0 이상이어야 합니다.")
    Double distance
) {
    public StadiumSearchByLocationServiceRequest toServiceRequest() {
        return new StadiumSearchByLocationServiceRequest(latitude, longitude, distance);
    }
}
