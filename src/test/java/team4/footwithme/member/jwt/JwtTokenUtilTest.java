package team4.footwithme.member.jwt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.jwt.response.TokenResponse;
import team4.footwithme.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtTokenUtilTest {

    @Autowired
    private  MemberRepository memberRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private Member testMember;

    @BeforeEach
    void setUp(){
        testMember = Member.create("test@naver.com", "password", "Test user", "010-1234-1234", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.save(testMember);
    }

    // 고민인 점 @SQLDelete 를 사용하게 되면 해당 테스트가 오류가 나서 일단 @SQLDelete를 주석 처리 했는데 어떤 방식으로 가능할까요?
    @AfterEach
    void afterDelete(){
        memberRepository.delete(testMember);
    }

    @DisplayName("토큰 생성 테스트")
    @Test
    void createToken() {
        String email = "test@naver.com";
        TokenResponse tokenResponse = jwtTokenUtil.createToken(email);

        assertThat(tokenResponse).isNotNull();
    }

    @DisplayName("토큰 추출 테스트")
    @Test
    void getEmailFromToken() {
        String email = "test@naver.com";
        TokenResponse tokenResponse = jwtTokenUtil.createToken(email);

        assertThat(tokenResponse).isNotNull();
        String emailFromAccessToken = jwtTokenUtil.getEmailFromToken(tokenResponse.accessToken());
        String emailFromRefreshToken = jwtTokenUtil.getEmailFromToken(tokenResponse.refreshToken());

        assertThat(emailFromAccessToken).isEqualTo(email);
        assertThat(emailFromRefreshToken).isEqualTo(email);
    }

    @DisplayName("RefreshToken 으로 AccessToken 생성")
    @Test
    void reCreateAccessTokenByRefreshToken() {
        String email = "test@naver.com";
        TokenResponse tokenResponse = jwtTokenUtil.createToken(email);

        String reCreateAccessToken = jwtTokenUtil.reCreateAccessToken(tokenResponse.refreshToken());

        assertThat(reCreateAccessToken).isNotNull();
    }

}