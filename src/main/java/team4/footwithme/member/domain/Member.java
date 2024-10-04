package team4.footwithme.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.crypto.password.PasswordEncoder;
import team4.footwithme.global.domain.BaseEntity;
import org.hibernate.annotations.SQLDelete;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
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


    public void update(String name, String phoneNumber, Gender gender){
        if(name != null) {
            this.name = name;
        }

        if(gender != null) {
            this.gender = gender;
        }

        if(phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    public void changePassword(String password){
        this.password = password;

    }
}
