package team4.footwithme.stadium.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import team4.footwithme.stadium.service.request.CourtRegisterServiceRequest;

import java.math.BigDecimal;

public record CourtRegisterRequest(
        @NotNull(message = "풋살장 아이디는 필수입니다.")
        Long stadiumId,

        @NotBlank(message = "구장 이름은 필수입니다.")
        @Size(max = 100, message = "구장 이름은 최대 100자까지 가능합니다.")
        String name,

        String description,

        @NotNull(message = "시간당 요금은 필수입니다.")
        @PositiveOrZero(message = "요금은 음수가 될 수 없습니다.")
        BigDecimal price_per_hour
) {
    public CourtRegisterServiceRequest toServiceRequest() {
        return new CourtRegisterServiceRequest(stadiumId, name, description, price_per_hour);
    }
}
