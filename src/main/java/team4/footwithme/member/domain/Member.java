package team4.footwithme.member.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.crypto.password.PasswordEncoder;
import team4.footwithme.global.domain.BaseEntity;


@SQLDelete(sql = "UPDATE member SET is_deleted = 'TRUE' WHERE member_id = ?")
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Embedded
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TermsAgreed termsAgreed;

    private Member(String email, String password, String name, String phoneNumber, LoginType loginType, Gender gender, MemberRole memberRole, TermsAgreed termsAgreed) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.loginType = loginType;
        this.gender = gender;
        this.memberRole = memberRole;
        this.termsAgreed = termsAgreed;
    }

    protected Member() {
    }

    public static Member create(String email, String password, String name, String phoneNumber, LoginProvider loginProvider, String snsId, Gender gender, MemberRole memberRole, TermsAgreed termsAgreed) {
        return Member.builder()
            .email(email)
            .password(password)
            .name(name)
            .phoneNumber(phoneNumber)
            .loginType(LoginType.builder()
                .loginProvider(loginProvider)
                .snsId(snsId)
                .build())
            .gender(gender)
            .memberRole(memberRole)
            .termsAgreed(termsAgreed)
            .build();
    }

    public static Member createTemporary(String email, String name, LoginProvider loginProvider, String snsId) {
        return Member.builder()
            .email(email)
            .loginType(LoginType.builder()
                .loginProvider(loginProvider)
                .snsId(snsId)
                .build())
            .name(name)
            .memberRole(MemberRole.GUEST)
            .termsAgreed(TermsAgreed.DISAGREE)
            .build();
    }

    public static MemberBuilder builder() {
        return new MemberBuilder();
    }


    public void update(String name, String phoneNumber, Gender gender) {
        if (name != null) {
            this.name = name;
        }

        if (gender != null) {
            this.gender = gender;
        }

        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void changePassword(String password) {
        this.password = password;

    }

    public Long getMemberId() {
        return this.memberId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public LoginType getLoginType() {
        return this.loginType;
    }

    public Gender getGender() {
        return this.gender;
    }

    public MemberRole getMemberRole() {
        return this.memberRole;
    }

    public TermsAgreed getTermsAgreed() {
        return this.termsAgreed;
    }

    public static class MemberBuilder {
        private String email;
        private String password;
        private String name;
        private String phoneNumber;
        private LoginType loginType;
        private Gender gender;
        private MemberRole memberRole;
        private TermsAgreed termsAgreed;

        MemberBuilder() {
        }

        public MemberBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MemberBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public MemberBuilder loginType(LoginType loginType) {
            this.loginType = loginType;
            return this;
        }

        public MemberBuilder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public MemberBuilder memberRole(MemberRole memberRole) {
            this.memberRole = memberRole;
            return this;
        }

        public MemberBuilder termsAgreed(TermsAgreed termsAgreed) {
            this.termsAgreed = termsAgreed;
            return this;
        }

        public Member build() {
            return new Member(this.email, this.password, this.name, this.phoneNumber, this.loginType, this.gender, this.memberRole, this.termsAgreed);
        }

        public String toString() {
            return "Member.MemberBuilder(email=" + this.email + ", password=" + this.password + ", name=" + this.name + ", phoneNumber=" + this.phoneNumber + ", loginType=" + this.loginType + ", gender=" + this.gender + ", memberRole=" + this.memberRole + ", termsAgreed=" + this.termsAgreed + ")";
        }
    }
}
