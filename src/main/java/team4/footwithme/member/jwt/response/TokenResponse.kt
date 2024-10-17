package team4.footwithme.member.jwt.response;

import lombok.Builder;

@Builder
public record TokenResponse(
    String accessToken,
    String refreshToken,
    Long refreshTokenExpirationTime
) {

    public static TokenResponse of(String accessToken, String refreshToken, Long refreshTokenExpirationTime) {
        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .refreshTokenExpirationTime(refreshTokenExpirationTime)
            .build();
    }
}
