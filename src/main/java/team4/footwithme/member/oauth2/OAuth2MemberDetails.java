package team4.footwithme.member.oauth2;

import team4.footwithme.member.domain.LoginProvider;

public interface OAuth2MemberDetails {
    LoginProvider getProvider();
    String getSNSId();
    String getEmail();
    String getName();
}
