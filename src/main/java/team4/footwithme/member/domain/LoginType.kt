package team4.footwithme.member.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class LoginType {
    @Enumerated(EnumType.STRING)
    var loginProvider: LoginProvider? = null

    @Column(nullable = true)
    var snsId: String? = null

    private constructor(loginProvider: LoginProvider?, snsId: String?) {
        this.loginProvider = loginProvider
        this.snsId = snsId
    }

    protected constructor()

    class LoginTypeBuilder internal constructor() {
        private var loginProvider: LoginProvider? = null
        private var snsId: String? = null

        fun loginProvider(loginProvider: LoginProvider?): LoginTypeBuilder {
            this.loginProvider = loginProvider
            return this
        }

        fun snsId(snsId: String?): LoginTypeBuilder {
            this.snsId = snsId
            return this
        }

        fun build(): LoginType {
            return LoginType(this.loginProvider, this.snsId)
        }

        override fun toString(): String {
            return "LoginType.LoginTypeBuilder(loginProvider=" + this.loginProvider + ", snsId=" + this.snsId + ")"
        }
    }

    companion object {
        @JvmStatic
        fun builder(): LoginTypeBuilder {
            return LoginTypeBuilder()
        }
    }
}
