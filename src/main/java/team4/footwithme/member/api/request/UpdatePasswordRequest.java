package team4.footwithme.member.api.request;

import team4.footwithme.member.api.request.validation.PasswordMatches;
import team4.footwithme.member.service.request.UpdatePasswordServiceRequest;

@PasswordMatches(passwordField = "newPassword", passwordConfirmField = "newPasswordConfirm")
public record UpdatePasswordRequest(
        String prePassword,
        String newPassword,
        String newPasswordConfirm
) {

    public UpdatePasswordServiceRequest toServiceRequest(){
        return new UpdatePasswordServiceRequest(prePassword, newPassword);
    }
}
