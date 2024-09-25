package team4.footwithme.stadium.api.response;

public record StadiumDetailResponse(
        String name,
        String address,
        String phoneNumber,
        String description,
        double latitude,
        double longitude
) {}
