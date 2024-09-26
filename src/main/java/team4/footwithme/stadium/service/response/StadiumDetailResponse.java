package team4.footwithme.stadium.service.response;

import org.locationtech.jts.geom.Point;
import team4.footwithme.stadium.domain.Stadium;

public record StadiumDetailResponse(
        Long memberId,
        String name,
        String address,
        String phoneNumber,
        String description,
        Double latitude,
        Double longitude
) {
    public static StadiumDetailResponse of(Stadium stadium, Point position) {
        return new StadiumDetailResponse(
                stadium.getMember().getMemberId(),
                stadium.getName(),
                stadium.getAddress(),
                stadium.getPhoneNumber(),
                stadium.getDescription(),
                position.getY(),
                position.getX()
        );
    }
}
