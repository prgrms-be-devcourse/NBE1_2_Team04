package team4.footwithme.stadium.service.response;

import team4.footwithme.stadium.domain.Stadium;

public record StadiumDetailResponse(
        Long stadiumId,
        Long memberId,
        String name,
        String address,
        String phoneNumber,
        String description,
        Double latitude,
        Double longitude
) {
    public static StadiumDetailResponse from(Stadium stadium) {
        return new StadiumDetailResponse(
                stadium.getStadiumId(),
                stadium.getMember().getMemberId(),
                stadium.getName(),
                stadium.getAddress(),
                stadium.getPhoneNumber(),
                stadium.getDescription(),
                stadium.getPosition().getLatitude(),
                stadium.getPosition().getLongitude()
        );
    }
}
