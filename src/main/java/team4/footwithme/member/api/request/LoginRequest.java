package team4.footwithme.member.api.request;

import team4.footwithme.member.service.request.LoginServiceRequest;

public record LoginRequest (
        String email,
        String password
){
    public LoginServiceRequest toServiceRequest(){
        return new LoginServiceRequest(email, password);
    }
}
