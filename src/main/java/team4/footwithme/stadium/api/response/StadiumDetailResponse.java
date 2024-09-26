package team4.footwithme.stadium.api.response;

public record StadiumDetailResponse(
        Long memberId,
        String name,
        String address,
        String phoneNumber,
        String description,
        Double latitude,
        Double longitude
) {}
