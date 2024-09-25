package team4.footwithme.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class LoginType {

    @Enumerated(EnumType.STRING)
    private LoginProvider loginProvider;

    @Column(nullable = false)
    private String snsId;

    @Builder
    private LoginType(LoginProvider loginProvider, String snsId) {
        this.loginProvider = loginProvider;
        this.snsId = snsId;
    }
}
