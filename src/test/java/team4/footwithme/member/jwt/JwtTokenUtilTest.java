package team4.footwithme.member.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.jwt.response.TokenResponse;
import team4.footwithme.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class JwtTokenUtilTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private Member testMember;

    @DisplayName("토큰 생성 테스트")
    @Test
    void createToken() {
        //given
        String email = "test@naver.com";
        saveMember(email);

        //when
        TokenResponse tokenResponse = jwtTokenUtil.createToken(email);

        //then
        assertThat(tokenResponse).isNotNull();
    }

    @DisplayName("토큰 추출 테스트")
    @Test
    void getEmailFromToken() {
        //given
        String email = "test@naver.com";
        saveMember(email);
        TokenResponse tokenResponse = jwtTokenUtil.createToken(email);

        //when
        String emailFromAccessToken = jwtTokenUtil.getEmailFromToken(tokenResponse.accessToken());
        String emailFromRefreshToken = jwtTokenUtil.getEmailFromToken(tokenResponse.refreshToken());

        //then
        assertThat(emailFromAccessToken).isEqualTo(email);
        assertThat(emailFromRefreshToken).isEqualTo(email);
    }

    @DisplayName("RefreshToken 으로 AccessToken 생성")
    @Test
    void reCreateAccessTokenByRefreshToken() {
        //given
        String email = "test@naver.com";
        saveMember(email);
        TokenResponse tokenResponse = jwtTokenUtil.createToken(email);

        //when
        String reCreateAccessToken = jwtTokenUtil.reCreateAccessToken(tokenResponse.refreshToken());

        //then
        assertThat(reCreateAccessToken).isNotNull();
    }

    private void saveMember(String email) {
        testMember = Member.create(email, "password", "Test user", "010-1234-1234", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.save(testMember);
    }

}