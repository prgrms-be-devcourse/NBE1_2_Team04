package team4.footwithme.stadium.service.response;

import team4.footwithme.stadium.domain.Court;

import java.math.BigDecimal;

public record CourtDetailResponse(
    Long courtId,
    Long stadiumId,
    String name,
    String description,
    BigDecimal price_per_hour
) {
    public static CourtDetailResponse from(Court court) {
        return new CourtDetailResponse(
            court.getCourtId(),
            court.getStadium().getStadiumId(),
            court.getName(),
            court.getDescription(),
            court.getPricePerHour()
        );
    }
}
