package team4.footwithme.member.service.response;

import team4.footwithme.member.jwt.response.TokenResponse;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
    public static LoginResponse from(TokenResponse tokenResponse){
        return new LoginResponse(tokenResponse.accessToken(), tokenResponse.refreshToken());
    }
}
