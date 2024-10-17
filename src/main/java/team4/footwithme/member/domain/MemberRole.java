package team4.footwithme.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum MemberRole {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MERCHANT("ROLE_MERCHANT");

    private final String text;

    private MemberRole(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
