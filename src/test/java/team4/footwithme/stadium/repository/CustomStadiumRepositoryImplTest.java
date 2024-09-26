package team4.footwithme.stadium.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Stadium;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class CustomStadiumRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("구장 이름을 IN절을 사용해 구장ID로 조회한다.")
    @Test
    void findStadiumNamesByStadiumIds() {
        //given
        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium savedStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium2 = Stadium.create(savedMember, "열정 풋살장", "서울시 강서구 어딘가", "01099999999", "열정 있음", 78.90, 9.876);
        Stadium savedStadium3 = Stadium.create(savedMember, "우주 풋살장", "서울시 동작구 어딘가", "01055555555", "우주에 있음", 65.4321, 12.345);
        Stadium savedStadium4 = Stadium.create(savedMember, "미친 풋살장", "서울시 강북구 어딘가", "01044444444", "강북에 있음", 19.8374, 67.765);
        stadiumRepository.saveAll(List.of(savedStadium1, savedStadium2, savedStadium3, savedStadium4));

        //when
        List<String> stadiumNames = stadiumRepository.findStadiumNamesByStadiumIds(List.of(savedStadium1.getStadiumId(), savedStadium4.getStadiumId()));

        //then
        Assertions.assertThat(stadiumNames).hasSize(2)
            .containsExactly("최강 풋살장", "미친 풋살장");
    }

}