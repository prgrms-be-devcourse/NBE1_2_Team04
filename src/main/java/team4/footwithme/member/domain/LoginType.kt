package team4.footwithme.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class LoginType {

    @Enumerated(EnumType.STRING)
    private LoginProvider loginProvider;

    @Column(nullable = true)
    private String snsId;

    private LoginType(LoginProvider loginProvider, String snsId) {
        this.loginProvider = loginProvider;
        this.snsId = snsId;
    }

    protected LoginType() {
    }

    public static LoginTypeBuilder builder() {
        return new LoginTypeBuilder();
    }

    public LoginProvider getLoginProvider() {
        return this.loginProvider;
    }

    public String getSnsId() {
        return this.snsId;
    }

    public static class LoginTypeBuilder {
        private LoginProvider loginProvider;
        private String snsId;

        LoginTypeBuilder() {
        }

        public LoginTypeBuilder loginProvider(LoginProvider loginProvider) {
            this.loginProvider = loginProvider;
            return this;
        }

        public LoginTypeBuilder snsId(String snsId) {
            this.snsId = snsId;
            return this;
        }

        public LoginType build() {
            return new LoginType(this.loginProvider, this.snsId);
        }

        public String toString() {
            return "LoginType.LoginTypeBuilder(loginProvider=" + this.loginProvider + ", snsId=" + this.snsId + ")";
        }
    }
}
