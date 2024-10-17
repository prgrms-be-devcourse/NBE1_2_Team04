package team4.footwithme.member.oauth2

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import team4.footwithme.member.domain.Member.Companion.createTemporary
import team4.footwithme.member.repository.MemberRepository

@Service
class CustomOAuth2UserService(private val memberRepository: MemberRepository) : DefaultOAuth2UserService() {
    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        log.info("getAttributes : {}", oAuth2User.attributes)

        val provider = userRequest.clientRegistration.registrationId

        var oAuth2MemberDetails: OAuth2MemberDetails? = null

        // 언젠간 진행할 다른 소셜 서비스 로그인을 위해 구분 => 구글
        if (provider == "google") {
            log.info("구글 로그인")
            oAuth2MemberDetails = OAuth2GoogleMemberDetails(oAuth2User.attributes)
        }

        val snsId = oAuth2MemberDetails?.sNSId
        val email = oAuth2MemberDetails?.email
        val name = oAuth2MemberDetails?.name
        val loginProvider = oAuth2MemberDetails?.provider

        val member = memberRepository.findByEmail(email)
            .orElseGet {
                val newMember = createTemporary(email, name, loginProvider, snsId)
                newMember
            } // 없을 시 임시 유저 생성 (저장 x)

        return CustomOAuth2UserDetails(member, oAuth2User.attributes)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CustomOAuth2UserService::class.java)
    }
}