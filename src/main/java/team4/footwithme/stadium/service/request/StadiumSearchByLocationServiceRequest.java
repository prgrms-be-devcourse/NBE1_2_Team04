package team4.footwithme.stadium.service.request;

public record StadiumSearchByLocationServiceRequest(
        Double latitude,
        Double longitude,
        Double distance
) {
}
