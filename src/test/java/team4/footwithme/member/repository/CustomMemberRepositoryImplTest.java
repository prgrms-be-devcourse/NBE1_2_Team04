package team4.footwithme.member.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CustomMemberRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이메일을 통해서 회원 아이디를 조회한다.")
    @Test
    void findMemberIdByEmail() {
        //given
        String target = "test@gmail.com";

        Member givenMember = Member.create(target, "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);
        //when
        Long findId = memberRepository.findMemberIdByMemberEmail(target);

        //then
        assertThat(findId).isEqualTo(savedMember.memberId);

    }

    @DisplayName("존재하지 않는 이메일을 통해 회원 아이디를 조회할 수 없는 경우 null을 반환한다.")
    @Test
    void findMemberIdByEmailWhenNotFoundIsReturnNull() {
        //given
        Member givenMember = Member.create("fail@naver.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        //when
        Long findId = memberRepository.findMemberIdByMemberEmail("hello@g.dot");

        //then
        assertThat(findId).isNull();
    }

}