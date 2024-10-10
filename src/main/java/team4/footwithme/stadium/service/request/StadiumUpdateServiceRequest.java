package team4.footwithme.stadium.service.request;

public record StadiumUpdateServiceRequest(
    String name,
    String address,
    String phoneNumber,
    String description,
    Double latitude,
    Double longitude
) {
}
