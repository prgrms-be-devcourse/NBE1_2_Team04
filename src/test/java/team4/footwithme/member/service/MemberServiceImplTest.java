package team4.footwithme.member.service;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.config.SecurityConfig;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.member.service.request.JoinServiceRequest;
import team4.footwithme.member.service.request.LoginServiceRequest;
import team4.footwithme.member.service.response.LoginResponse;
import team4.footwithme.member.service.response.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Mockito.mock;

@Transactional
class MemberServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @DisplayName("회원 가입을 진행한다.")
    @Test
    void join() {
        //given
        JoinServiceRequest request = new JoinServiceRequest("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "",  Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        //when
        MemberResponse memberResponse = memberService.join(request);
        Member member = memberRepository.findByEmail("test@naver.com").get();

        //then
        assertThat(member).isNotNull()
                        .extracting( "email" ,"name", "phoneNumber", "gender", "memberRole", "termsAgreed")
                                .containsExactlyInAnyOrder(
                                        request.email(), request.name(), request.phoneNumber(), request.gender(), request.memberRole(), request.termsAgree()
                                );

        assertThat(memberResponse).isNotNull()
                .extracting(  "memberId", "email", "name", "phoneNumber", "gender", "memberRole", "termsAgreed")
                .containsExactlyInAnyOrder(
                        member.getMemberId(), member.getEmail(), member.getName(), member.getPhoneNumber(), member.getGender(), member.getMemberRole(), member.getTermsAgreed()
                );
    }

    @DisplayName("로그인을 진행한다.")
    @Test
    void login() {
        //given
        Member member = Member.create("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "",  Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        member.encodePassword(securityConfig.passwordEncoder());
        memberRepository.save(member);

        LoginServiceRequest request = new LoginServiceRequest("test@naver.com", "!test1234");
        //when
        LoginResponse response = memberService.login(request);

        //then
        assertThat(response).isNotNull();
    }
}