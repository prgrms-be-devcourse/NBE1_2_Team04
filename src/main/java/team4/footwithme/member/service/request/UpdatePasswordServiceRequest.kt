package team4.footwithme.member.service.request;

public record UpdatePasswordServiceRequest(
    String prePassword,
    String newPassword
) {
}
