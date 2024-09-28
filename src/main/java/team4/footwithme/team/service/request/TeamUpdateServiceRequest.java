package team4.footwithme.team.service.request;

public record TeamUpdateServiceRequest(
        String name,
        String description,
        String location
) {}
