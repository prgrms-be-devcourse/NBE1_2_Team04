package team4.footwithme.member.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.config.SecurityConfig;
import team4.footwithme.member.api.request.UpdatePasswordRequest;
import team4.footwithme.member.api.request.UpdateRequest;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.member.jwt.response.TokenResponse;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.member.service.request.JoinServiceRequest;
import team4.footwithme.member.service.request.LoginServiceRequest;
import team4.footwithme.member.service.request.UpdateServiceRequest;
import team4.footwithme.member.service.response.LoginResponse;
import team4.footwithme.member.service.response.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Transactional
class MemberServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CookieService cookieService;

    @DisplayName("회원 가입을 진행한다.")
    @Test
    void join() {
        //given
        JoinServiceRequest request = new JoinServiceRequest("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        //when
        MemberResponse memberResponse = memberService.join(request);
        Member member = memberRepository.findByEmail("test@naver.com").get();

        //then
        assertThat(member).isNotNull()
            .extracting("email", "name", "phoneNumber", "gender", "memberRole", "termsAgreed")
            .containsExactlyInAnyOrder(
                request.email(), request.name(), request.phoneNumber(), request.gender(), request.memberRole(), request.termsAgree()
            );

        assertThat(memberResponse).isNotNull()
            .extracting("memberId", "email", "name", "phoneNumber", "gender", "memberRole", "termsAgreed")
            .containsExactlyInAnyOrder(
                member.getMemberId(), member.getEmail(), member.getName(), member.getPhoneNumber(), member.getGender(), member.getMemberRole(), member.getTermsAgreed()
            );
    }

    @DisplayName("로그인을 진행한다.")
    @Test
    void login() {
        //given
        Member member = Member.create("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        member.encodePassword(securityConfig.passwordEncoder());
        memberRepository.save(member);

        LoginServiceRequest request = new LoginServiceRequest("test@naver.com", "!test1234");
        //when
        LoginResponse response = memberService.login(request);

        //then
        assertThat(response).isNotNull();
    }

    @DisplayName("로그아웃을 진행한다.")
    @Test
    void logout() {
        //given
        Member member = Member.create("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "",  Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        member.encodePassword(securityConfig.passwordEncoder());
        memberRepository.save(member);

        LoginServiceRequest loginServiceRequest = new LoginServiceRequest("test@naver.com", "!test1234");
        LoginResponse loginResponse = memberService.login(loginServiceRequest);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(loginResponse.accessToken()); // 헤더 설정

        //when
        String response = memberService.logout(request);

        //then
        assertThat(redisTemplate.opsForValue().get(loginResponse.accessToken())).isEqualTo("logout"); // redis 에 잘 저장 되었는지 확인
        assertThat(response).isEqualTo("Success Logout"); // 결과 확인
    }

    @DisplayName("AccessToken 을 갱신한다. (헤더에 있을 경우)")
    @Test
    void reissueWithHeader(){
        //given
        Member member = Member.create("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "",  Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        member.encodePassword(securityConfig.passwordEncoder());
        memberRepository.save(member);

        LoginServiceRequest loginServiceRequest = new LoginServiceRequest("test@naver.com", "!test1234");
        LoginResponse loginResponse = memberService.login(loginServiceRequest);

        HttpServletRequest request = mock(HttpServletRequest.class);

        //when
        TokenResponse tokenResponse = memberService.reissue(request, loginResponse.refreshToken());

        //then
        assertThat(tokenResponse).isNotNull()
                .extracting(  "accessToken","refreshToken", "refreshTokenExpirationTime")
                .containsExactlyInAnyOrder(
                        tokenResponse.accessToken() ,loginResponse.refreshToken(), tokenResponse.refreshTokenExpirationTime()
                );


    }

    @DisplayName("AccessToken 을 갱신한다. (헤더에 없을 경우)")
    @Test
    void reissueWithoutHeader(){
        //given
        Member member = Member.create("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "",  Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        member.encodePassword(securityConfig.passwordEncoder());
        memberRepository.save(member);

        LoginServiceRequest loginServiceRequest = new LoginServiceRequest("test@naver.com", "!test1234");
        LoginResponse loginResponse = memberService.login(loginServiceRequest);

        Cookie[] cookies = new Cookie[] {
                new Cookie("refreshToken", loginResponse.refreshToken())
        };

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(cookies); // 쿠키 반환 설정

        //when
        TokenResponse tokenResponse = memberService.reissue(request, "");

        //then
        assertThat(tokenResponse).isNotNull()
                .extracting(  "accessToken","refreshToken", "refreshTokenExpirationTime")
                .containsExactlyInAnyOrder(
                        tokenResponse.accessToken() ,loginResponse.refreshToken(), tokenResponse.refreshTokenExpirationTime()
                );


    }

    @DisplayName("이름, 전화번호, 성별을 수정한다.")
    @Test
    void update(){
        //given
        Member member = Member.create("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "",  Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        member.encodePassword(securityConfig.passwordEncoder());
        memberRepository.save(member);


        UpdateRequest updateRequest = new UpdateRequest("TestUser", "010-2222-2222", Gender.FEMALE);

        //when
        MemberResponse response = memberService.update(member, updateRequest.toServiceRequest());

        //then
        assertThat(response).isNotNull()
                .extracting("name", "phoneNumber", "gender")
                .containsExactlyInAnyOrder(
                        "TestUser", "010-2222-2222", Gender.FEMALE
                );
    }

    @DisplayName("비밀번호를 수정한다.")
    @Test
    void updatePassword(){
        //given
        Member member = Member.create("test@naver.com", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "",  Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        member.encodePassword(securityConfig.passwordEncoder());
        memberRepository.save(member);


        UpdatePasswordRequest updateRequest = new UpdatePasswordRequest("!test1234", "!Test123456", "!Test123456");

        //when
        String response = memberService.updatePassword(member, updateRequest.toServiceRequest());

        //then
        assertThat(response).isEqualTo("Success Change Password");
        assertThat(securityConfig.passwordEncoder().matches(updateRequest.newPassword(), member.getPassword())).isTrue();

    }

}