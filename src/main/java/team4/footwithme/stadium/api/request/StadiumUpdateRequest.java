package team4.footwithme.stadium.api.request;

import jakarta.validation.constraints.*;
import team4.footwithme.stadium.service.request.StadiumUpdateServiceRequest;

public record StadiumUpdateRequest(
        @NotBlank(message = "풋살장 이름은 필수입니다.")
        @Size(max = 100, message = "풋살장 이름은 최대 100자까지 가능합니다.")
        String name,

        @NotBlank(message = "풋살장 주소는 필수입니다.")
        @Size(max = 100, message = "풋살장 주소는 최대 100자까지 가능합니다.")
        String address,

        @NotBlank(message = "풋살장 연락처는 필수입니다.")
        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식을 입력해주세요.")
        String phoneNumber,

        String description,

        @NotNull(message = "위도 값은 필수 입력 항목입니다.")
        @Min(value = -90, message = "위도 값은 -90도 이상이어야 합니다.")
        @Max(value = 90, message = "위도 값은 90도 이하이어야 합니다.")
        Double latitude,

        @NotNull(message = "경도 값은 필수 입력 항목입니다.")
        @Min(value = -180, message = "경도 값은 -180도 이상이어야 합니다.")
        @Max(value = 180, message = "경도 값은 180도 이하이어야 합니다.")
        Double longitude
) {
    public StadiumUpdateServiceRequest toServiceRequest() {
        return new StadiumUpdateServiceRequest(name, address, phoneNumber, description, latitude, longitude);
    }
}