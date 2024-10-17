package team4.footwithme.member.service.request;

import team4.footwithme.member.domain.Gender;

public record UpdateServiceRequest(
    String name,
    String phoneNumber,
    Gender gender
) {
}
