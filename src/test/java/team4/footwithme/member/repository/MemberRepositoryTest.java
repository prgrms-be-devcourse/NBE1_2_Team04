package team4.footwithme.member.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class MemberRepositoryTest extends IntegrationTestSupport  {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 멤버 조회")
    void findByEmail() {
        //given
        String testEmail = "test@naver.com";
        Member testMember = Member.create(testEmail, "password", "Test user", "010-1234-1234", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.save(testMember);

        //when
        Member member = memberRepository.findByEmail(testEmail).get();

        //then
        assertThat(member.getEmail()).isEqualTo(testEmail);

    }
}