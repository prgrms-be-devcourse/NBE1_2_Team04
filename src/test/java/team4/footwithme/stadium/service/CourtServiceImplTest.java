package team4.footwithme.stadium.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Position;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.CourtRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class CourtServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CourtServiceImpl courtService;


    @BeforeEach
    void setUp() {
        Member testMember = Member.builder()
            .email("test@example.com")
            .password("password")
            .name("Test User")
            .phoneNumber("010-1234-5678")
            .loginType(LoginType.builder()
                .loginProvider(LoginProvider.ORIGINAL)
                .snsId("example@example.com")
                .build())
            .gender(Gender.MALE)
            .memberRole(MemberRole.USER)
            .termsAgreed(TermsAgreed.AGREE)
            .build();
        memberRepository.save(testMember);

        Position position1 = Position.builder()
            .latitude(37.5642135)
            .longitude(127.0016985)
            .build();

        Stadium stadium = Stadium.builder()
            .name("Stadium1")
            .address("seoul")
            .phoneNumber("123-4567")
            .description("Description1")
            .position(position1)
            .member(testMember)
            .build();
        stadiumRepository.save(stadium);

        Court court1 = Court.builder()
            .stadium(stadium)
            .name("Court 1")
            .description("Test Court 1")
            .pricePerHour(BigDecimal.valueOf(10000))
            .build();

        Court court2 = Court.builder()
            .stadium(stadium)
            .name("Court 2")
            .description("Test Court 2")
            .pricePerHour(BigDecimal.valueOf(150000))
            .build();

        courtRepository.save(court1);
        courtRepository.save(court2);
    }

    @Test
    @DisplayName("유효한 stadiumId가 제공되면 Court 목록을 반환해야 한다")
    void testGetCourtsByStadiumId_ReturnsCourts() {
        Long stadiumId = 3L;

        List<CourtsResponse> result = courtService.getCourtsByStadiumId(stadiumId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result)
            .extracting(CourtsResponse::name)
            .containsExactly("Court 1", "Court 2");
    }

    @Test
    @DisplayName("요청 시 모든 Court를 반환해야 한다")
    void testGetAllCourts_ReturnsAllCourts() {
        List<CourtsResponse> result = courtService.getAllCourts();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result)
            .extracting(CourtsResponse::name)
            .containsExactly("Court 1", "Court 2");
    }

    @Test
    @DisplayName("Court의 상세 정보를 반환해야 한다")
    void testGetCourtBycourtId_ReturnsCourtByCourtDetail() {
        Court court = courtRepository.findAll().get(0); // 첫 번째 Court를 가져옴
        Long courtId = court.getCourtId();

        CourtDetailResponse result = courtService.getCourtByCourtId(courtId);

        assertThat(result).isNotNull();
        assertThat(result.courtId()).isEqualTo(courtId);
        assertThat(result.name()).isEqualTo(court.getName());
        assertThat(result.price_per_hour()).isEqualTo(court.getPricePerHour());
    }

    @Test
    @DisplayName("존재하지 않는 courtId로 조회하면 예외를 발생시켜야 한다")
    void testGetCourtBycourtId_ThrowsException_WhenCourtByCourtNotFound() {
        Long nonExistentCourtId = 999L;

        assertThatThrownBy(() -> courtService.getCourtByCourtId(nonExistentCourtId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("해당 구장을 찾을 수 없습니다.");
    }
}