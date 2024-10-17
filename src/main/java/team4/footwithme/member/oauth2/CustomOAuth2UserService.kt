package team4.footwithme.member.oauth2;

import org.slf4j.Logger;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import team4.footwithme.member.domain.LoginProvider;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2MemberDetails oAuth2MemberDetails = null;

        // 언젠간 진행할 다른 소셜 서비스 로그인을 위해 구분 => 구글
        if (provider.equals("google")) {
            log.info("구글 로그인");
            oAuth2MemberDetails = new OAuth2GoogleMemberDetails(oAuth2User.getAttributes());

        }

        String snsId = oAuth2MemberDetails.getSNSId();
        String email = oAuth2MemberDetails.getEmail();
        String name = oAuth2MemberDetails.getName();
        LoginProvider loginProvider = oAuth2MemberDetails.getProvider();

        Member member = memberRepository.findByEmail(email)
            .orElseGet(() -> {
                Member newMember = Member.createTemporary(email, name, loginProvider, snsId);
                return newMember;
            }); // 없을 시 임시 유저 생성 (저장 x)

        return new CustomOAuth2UserDetails(member, oAuth2User.getAttributes());
    }
}