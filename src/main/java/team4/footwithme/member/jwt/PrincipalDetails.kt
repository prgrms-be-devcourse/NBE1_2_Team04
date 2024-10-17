package team4.footwithme.member.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import team4.footwithme.member.domain.Member
import team4.footwithme.member.domain.MemberRole

class PrincipalDetails(@JvmField val member: Member?, private val authorities: Collection<GrantedAuthority>) : UserDetails {
    val memberRole: MemberRole?
        get() = member!!.memberRole

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return member!!.password!!
    }

    override fun getUsername(): String {
        return member!!.email!!
    }
}
