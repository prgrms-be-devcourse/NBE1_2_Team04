package team4.footwithme.stadium.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Stadium;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class CustomCourtRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("구장이름을 구장 ID로 조회한다.")
    @Test
    void findCourtNameByCourtId() {
        //given
        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);
        Stadium stadium = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium = stadiumRepository.save(stadium);
        Court court1 = Court.create(savedStadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        Court court2 = Court.create(savedStadium, "야외 구장 B", "다양한 물품 제공", BigDecimal.TEN);
        Court court3 = Court.create(savedStadium, "야외 구장 C", "다양한 물품 제공", BigDecimal.TEN);

        List<Court> savedCourts = courtRepository.saveAll(List.of(court1, court2, court3));
        //when
        String courtName = courtRepository.findCourtNameByCourtId(savedCourts.get(0).getCourtId());

        //then
        assertThat(courtName).isEqualTo("야외 구장 A");
    }

    @DisplayName("구장 개수를 IN절을 사용해 구장ID로 조회한다.")
    @Test
    void countCourtByCourtIds() {
        //given
        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);
        Stadium stadium = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium = stadiumRepository.save(stadium);
        Court court1 = Court.create(savedStadium, "야외 구장 A", "다양한 물품 제공", BigDecimal.TEN);
        Court court2 = Court.create(savedStadium, "야외 구장 B", "다양한 물품 제공", BigDecimal.TEN);
        Court court3 = Court.create(savedStadium, "야외 구장 C", "다양한 물품 제공", BigDecimal.TEN);

        List<Court> savedCourts = courtRepository.saveAll(List.of(court1, court2, court3));
        //when
        Long count = courtRepository.countCourtByCourtIds(List.of(savedCourts.get(0).getCourtId(), savedCourts.get(1).getCourtId()));
        //then
        assertThat(count).isEqualTo(2);
    }

}