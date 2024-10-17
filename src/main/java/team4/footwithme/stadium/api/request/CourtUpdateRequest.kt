package team4.footwithme.stadium.api.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import team4.footwithme.stadium.service.request.CourtUpdateServiceRequest
import java.math.BigDecimal

@JvmRecord
data class CourtUpdateRequest(
    stadiumId: @NotNull(message = "풋살장 아이디는 필수입니다.") Long?,

    name: @NotBlank(message = "구장 이름은 필수입니다.") @Size(max = 100, message = "구장 이름은 최대 100자까지 가능합니다.") String?,

    description: String,

    price_per_hour: @NotNull(message = "시간당 요금은 필수입니다.") @PositiveOrZero(message = "요금은 음수가 될 수 없습니다.") BigDecimal?
) {
    fun toServiceRequest(): CourtUpdateServiceRequest {
        return CourtUpdateServiceRequest(stadiumId, name, description, price_per_hour)
    }

    val stadiumId: @NotNull(message = "풋살장 아이디는 필수입니다.") Long? = stadiumId
    val name: @NotBlank(message = "구장 이름은 필수입니다.") @Size(max = 100, message = "구장 이름은 최대 100자까지 가능합니다.") String? =
        name
    val description: String = description
    val price_per_hour: @NotNull(message = "시간당 요금은 필수입니다.") @PositiveOrZero(message = "요금은 음수가 될 수 없습니다.") BigDecimal? =
        price_per_hour
}
