package team4.footwithme.member.domain

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.springframework.security.crypto.password.PasswordEncoder
import team4.footwithme.global.domain.BaseEntity

@SQLDelete(sql = "UPDATE member SET is_deleted = 'TRUE' WHERE member_id = ?")
@Entity
class Member : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memberId: Long? = null

    @Column(nullable = false, unique = true)
    var email: String? = null

    var password: String? = null

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false)
    var phoneNumber: String? = null

    @Embedded
    var loginType: LoginType? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var gender: Gender? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var memberRole: MemberRole? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var termsAgreed: TermsAgreed? = null

    private constructor(
        email: String?,
        password: String?,
        name: String?,
        phoneNumber: String?,
        loginType: LoginType?,
        gender: Gender?,
        memberRole: MemberRole?,
        termsAgreed: TermsAgreed?
    ) {
        this.email = email
        this.password = password
        this.name = name
        this.phoneNumber = phoneNumber
        this.loginType = loginType
        this.gender = gender
        this.memberRole = memberRole
        this.termsAgreed = termsAgreed
    }

    protected constructor()

    fun update(name: String?, phoneNumber: String?, gender: Gender?) {
        if (name != null) {
            this.name = name
        }

        if (gender != null) {
            this.gender = gender
        }

        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber
        }
    }

    fun encodePassword(passwordEncoder: PasswordEncoder) {
        this.password = passwordEncoder.encode(password)
    }

    fun changePassword(password: String?) {
        this.password = password
    }

    class MemberBuilder internal constructor() {
        private var email: String? = null
        private var password: String? = null
        private var name: String? = null
        private var phoneNumber: String? = null
        private var loginType: LoginType? = null
        private var gender: Gender? = null
        private var memberRole: MemberRole? = null
        private var termsAgreed: TermsAgreed? = null

        fun email(email: String?): MemberBuilder {
            this.email = email
            return this
        }

        fun password(password: String?): MemberBuilder {
            this.password = password
            return this
        }

        fun name(name: String?): MemberBuilder {
            this.name = name
            return this
        }

        fun phoneNumber(phoneNumber: String?): MemberBuilder {
            this.phoneNumber = phoneNumber
            return this
        }

        fun loginType(loginType: LoginType?): MemberBuilder {
            this.loginType = loginType
            return this
        }

        fun gender(gender: Gender?): MemberBuilder {
            this.gender = gender
            return this
        }

        fun memberRole(memberRole: MemberRole?): MemberBuilder {
            this.memberRole = memberRole
            return this
        }

        fun termsAgreed(termsAgreed: TermsAgreed?): MemberBuilder {
            this.termsAgreed = termsAgreed
            return this
        }

        fun build(): Member {
            return Member(
                this.email,
                this.password,
                this.name,
                this.phoneNumber,
                this.loginType,
                this.gender,
                this.memberRole,
                this.termsAgreed
            )
        }

        override fun toString(): String {
            return "Member.MemberBuilder(email=" + this.email + ", password=" + this.password + ", name=" + this.name + ", phoneNumber=" + this.phoneNumber + ", loginType=" + this.loginType + ", gender=" + this.gender + ", memberRole=" + this.memberRole + ", termsAgreed=" + this.termsAgreed + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(
            email: String?,
            password: String?,
            name: String?,
            phoneNumber: String?,
            loginProvider: LoginProvider?,
            snsId: String?,
            gender: Gender?,
            memberRole: MemberRole?,
            termsAgreed: TermsAgreed?
        ): Member {
            return builder()
                .email(email)
                .password(password)
                .name(name)
                .phoneNumber(phoneNumber)
                .loginType(
                    LoginType.Companion.builder()
                        .loginProvider(loginProvider)
                        .snsId(snsId)
                        .build()
                )
                .gender(gender)
                .memberRole(memberRole)
                .termsAgreed(termsAgreed)
                .build()
        }

        @JvmStatic
        fun createTemporary(email: String?, name: String?, loginProvider: LoginProvider?, snsId: String?): Member {
            return builder()
                .email(email)
                .loginType(
                    LoginType.Companion.builder()
                        .loginProvider(loginProvider)
                        .snsId(snsId)
                        .build()
                )
                .name(name)
                .memberRole(MemberRole.GUEST)
                .termsAgreed(TermsAgreed.DISAGREE)
                .build()
        }

        @JvmStatic
        fun builder(): MemberBuilder {
            return MemberBuilder()
        }
    }
}
