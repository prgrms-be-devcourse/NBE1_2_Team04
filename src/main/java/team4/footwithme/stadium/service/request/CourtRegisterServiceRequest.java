package team4.footwithme.stadium.service.request;

import java.math.BigDecimal;

public record CourtRegisterServiceRequest(
        Long stadiumId,
        String name,
        String description,
        BigDecimal price_per_hour
) {
}
