package team4.footwithme.member.oauth2;

import team4.footwithme.member.domain.LoginProvider;

import java.util.Map;

public class OAuth2GoogleMemberDetails implements OAuth2MemberDetails {

    private Map<String, Object> attributes;

    public OAuth2GoogleMemberDetails(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public LoginProvider getProvider() {
        return LoginProvider.GOOGLE;
    }

    @Override
    public String getSNSId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
