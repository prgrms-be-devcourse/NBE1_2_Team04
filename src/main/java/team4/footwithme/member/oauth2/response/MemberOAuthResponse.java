package team4.footwithme.member.oauth2.response;

import team4.footwithme.member.domain.LoginProvider;
import team4.footwithme.member.domain.Member;

public record MemberOAuthResponse(
        LoginProvider provider,
        String snsId,
        String email
) {
    public static MemberOAuthResponse from(Member member){
        return new MemberOAuthResponse(
                member.getLoginType().getLoginProvider(),
                member.getLoginType().getSnsId(),
                member.getEmail()
        );
    }
}
