package team4.footwithme.member.jwt

import lombok.RequiredArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import team4.footwithme.member.repository.MemberRepository

@Service
@RequiredArgsConstructor
class PrincipalDetailsService : UserDetailsService {
    private val memberRepository: MemberRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val member = memberRepository!!.findByEmail(email)
            .orElseThrow { IllegalArgumentException("존재하지 않는 이메일 입니다.") }

        val grantedAuthority: GrantedAuthority = SimpleGrantedAuthority(member!!.memberRole!!.text)

        val userDetails = PrincipalDetails(member, setOf(grantedAuthority))

        return userDetails
    }
}
