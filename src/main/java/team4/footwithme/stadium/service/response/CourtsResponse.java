package team4.footwithme.stadium.service.response;

import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Stadium;

public record CourtsResponse(
        Long courtId,
        Long stadiumId,
        String name
) {
    public static CourtsResponse from(Court court) {
        return new CourtsResponse(
                court.getCourtId(),
                court.getStadium().getStadiumId(),
                court.getName()
        );
    }
}
