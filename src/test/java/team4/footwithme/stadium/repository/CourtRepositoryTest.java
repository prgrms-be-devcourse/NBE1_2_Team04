package team4.footwithme.stadium.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Stadium;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CourtRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;
    private Stadium testStadium;
    private Court court1;
    private Court court2;
    private Court court3;

    @BeforeEach
    void setUp() {
        testMember = Member.create("test@example.com", "password", "Test User", "010-1234-5678",
                LoginProvider.ORIGINAL, "test@example.com", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.save(testMember);

        testStadium = Stadium.create(testMember, "Test Stadium", "seoul", "010-1111-1111", "Description", 37.5665, 126.9780);
        stadiumRepository.save(testStadium);

        court1 = Court.create(testStadium, "Court1", "Description1", new BigDecimal("10000"));
        court2 = Court.create(testStadium, "Court2", "Description2", new BigDecimal("15000"));
        court3 = Court.create(testStadium, "Court3", "Description3", new BigDecimal("20000"));

        courtRepository.save(court1);
        courtRepository.save(court2);
        courtRepository.save(court3);
    }

    @Test
    @DisplayName("findByStadium_StadiumId는 특정 풋살장의 구장를 조회한다")
    void findByStadium_StadiumId_returnsCourtsOfStadium() {
        Slice<Court> courts = courtRepository.findByStadium_StadiumId(testStadium.getStadiumId(), PageRequest.of(0, 10));

        assertThat(courts.getContent())
                .hasSize(3)
                .extracting(Court::getName)
                .containsExactlyInAnyOrder("Court1", "Court2", "Court3");
    }

    @Test
    @DisplayName("findByStadium_StadiumId는 삭제된 구장을 조회하지 않는다")
    void findByStadium_StadiumId_doesNotReturnDeletedCourts() {
        courtRepository.delete(court2);

        Slice<Court> courts = courtRepository.findByStadium_StadiumId(testStadium.getStadiumId(), PageRequest.of(0, 10));

        assertThat(courts.getContent())
                .hasSize(2)
                .extracting(Court::getName)
                .containsExactlyInAnyOrder("Court1", "Court3");
    }

    @Test
    @DisplayName("findAllActive는 모든 활성화된 구장을 조회한다")
    void findAllActive_returnsAllActiveCourts() {
        Slice<Court> courts = courtRepository.findAllActive(PageRequest.of(0, 10));

        assertThat(courts.getContent())
                .hasSize(3)
                .extracting(Court::getName)
                .containsExactlyInAnyOrder("Court1", "Court2", "Court3");
    }

    @Test
    @DisplayName("findAllActive는 삭제된 구장을 조회하지 않는다")
    void findAllActive_doesNotReturnDeletedCourts() {
        courtRepository.delete(court1);

        Slice<Court> courts = courtRepository.findAllActive(PageRequest.of(0, 10));

        assertThat(courts.getContent())
                .hasSize(2)
                .extracting(Court::getName)
                .containsExactlyInAnyOrder("Court2", "Court3");
    }

    @Test
    @DisplayName("findActiveById는 활성화된 구장을 ID로 조회한다")
    void findActiveById_returnsActiveCourt() {
        Optional<Court> foundCourt = courtRepository.findActiveById(court3.getCourtId());

        assertThat(foundCourt).isPresent();
        assertThat(foundCourt.get()).isEqualTo(court3);
    }

    @Test
    @DisplayName("findActiveById는 삭제된 구장을 조회하지 않는다")
    void findActiveById_doesNotReturnDeletedCourt() {
        courtRepository.delete(court3);

        Optional<Court> foundCourt = courtRepository.findActiveById(court3.getCourtId());

        assertThat(foundCourt).isNotPresent();
    }
}
