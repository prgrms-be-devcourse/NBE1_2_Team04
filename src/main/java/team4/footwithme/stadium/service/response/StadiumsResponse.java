package team4.footwithme.stadium.service.response;

import team4.footwithme.stadium.domain.Stadium;

public record StadiumsResponse(Long stadiumId, String name, String address) {
    public static StadiumsResponse from(Stadium stadium) {
        return new StadiumsResponse(
            stadium.getStadiumId(),
            stadium.getName(),
            stadium.getAddress()
        );
    }
}
