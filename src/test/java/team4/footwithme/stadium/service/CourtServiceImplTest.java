package team4.footwithme.stadium.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.CourtRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.stadium.service.request.CourtDeleteServiceRequest;
import team4.footwithme.stadium.service.request.CourtRegisterServiceRequest;
import team4.footwithme.stadium.service.request.CourtUpdateServiceRequest;
import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class CourtServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private CourtServiceImpl courtService;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;
    private Stadium testStadium;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .email("test@example.com")
                .password("password")
                .name("Test User")
                .phoneNumber("010-1234-5678")
                .loginType(LoginType.builder()
                        .loginProvider(LoginProvider.ORIGINAL)
                        .snsId("test@example.com")
                        .build())
                .gender(Gender.MALE)
                .memberRole(MemberRole.USER)
                .termsAgreed(TermsAgreed.AGREE)
                .build();
        memberRepository.save(testMember);

        testStadium = Stadium.create(testMember, "Test Stadium", "Test Address", "010-1111-2222", "Test Description", 37.5665, 126.9780);
        stadiumRepository.save(testStadium);

        Court court1 = Court.create(testStadium, "Court1", "Description1", new BigDecimal("10000"));
        Court court2 = Court.create(testStadium, "Court2", "Description2", new BigDecimal("15000"));
        courtRepository.save(court1);
        courtRepository.save(court2);
    }

    @Test
    @DisplayName("특정 스타디움의 코트 목록을 정상적으로 반환해야 한다")
    void getCourtsByStadiumId() {
        Slice<CourtsResponse> result = courtService.getCourtsByStadiumId(testStadium.getStadiumId(), 0, "name");

        assertThat(result.getContent())
                .hasSize(2)
                .extracting(CourtsResponse::name)
                .containsExactly("Court1", "Court2");
    }

    @Test
    @DisplayName("전체 코트 목록을 정상적으로 반환해야 한다")
    void getAllCourts() {
        Slice<CourtsResponse> result = courtService.getAllCourts(0, "name");

        assertThat(result.getContent())
                .hasSize(2)
                .extracting(CourtsResponse::name)
                .containsExactly("Court1", "Court2");
    }

    @Test
    @DisplayName("코트 ID로 코트 상세 정보를 정상적으로 반환해야 한다")
    void getCourtByCourtId() {
        Court court = courtRepository.findAll().get(0);

        CourtDetailResponse response = courtService.getCourtByCourtId(court.getCourtId());

        assertThat(response).isNotNull();
        assertThat(response)
                .extracting(
                        CourtDetailResponse::name,
                        CourtDetailResponse::description,
                        CourtDetailResponse::price_per_hour
                )
                .containsExactly(
                        court.getName(),
                        court.getDescription(),
                        court.getPricePerHour()
                );
    }

    @Test
    @DisplayName("코트를 정상적으로 등록해야 한다")
    void registerCourt() {
        BigDecimal price = new BigDecimal("20000");
        CourtRegisterServiceRequest request = new CourtRegisterServiceRequest(
                testStadium.getStadiumId(),
                "New Court",
                "New Description",
                price
        );

        CourtDetailResponse response = courtService.registerCourt(request, testMember.getMemberId());

        assertThat(response).isNotNull();
        assertThat(response)
                .extracting(
                        CourtDetailResponse::name,
                        CourtDetailResponse::description,
                        CourtDetailResponse::price_per_hour
                )
                .containsExactly(
                        "New Court",
                        "New Description",
                        price
                );

        assertThat(courtRepository.findById(response.courtId())).isPresent();
    }

    @Test
    @DisplayName("코트를 정상적으로 수정해야 한다")
    void updateCourt() {
        Court court = courtRepository.findAll().get(0);

        BigDecimal updatedPrice = new BigDecimal("25000");
        CourtUpdateServiceRequest request = new CourtUpdateServiceRequest(
                testStadium.getStadiumId(),
                "Updated Court",
                "Updated Description",
                updatedPrice
        );

        CourtDetailResponse response = courtService.updateCourt(request, testMember.getMemberId(), court.getCourtId());

        assertThat(response).isNotNull();
        assertThat(response)
                .extracting(
                        CourtDetailResponse::name,
                        CourtDetailResponse::description,
                        CourtDetailResponse::price_per_hour
                )
                .containsExactly(
                        "Updated Court",
                        "Updated Description",
                        updatedPrice
                );

        Court updatedCourt = courtRepository.findById(court.getCourtId()).orElseThrow();
        assertThat(updatedCourt)
                .extracting(
                        Court::getName,
                        Court::getDescription,
                        Court::getPricePerHour
                )
                .containsExactly(
                        "Updated Court",
                        "Updated Description",
                        updatedPrice
                );
    }

    @Test
    @DisplayName("코트를 정상적으로 삭제해야 한다")
    void deleteCourt() {
        Court court = courtRepository.findAll().get(0);

        CourtDeleteServiceRequest request = new CourtDeleteServiceRequest(
                testStadium.getStadiumId()
        );

        courtService.deleteCourt(request, testMember.getMemberId(), court.getCourtId());

        Optional<Court> deletedCourt = courtRepository.findById(court.getCourtId());
        assertThat(deletedCourt).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 코트를 조회할 때 IllegalArgumentException이 발생해야 한다")
    void getCourtByCourtId_whenCourtDoesNotExist() {
        Long invalidCourtId = -1L;

        assertThatThrownBy(() -> courtService.getCourtByCourtId(invalidCourtId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.COURT_NOT_FOUND.getText());
    }

    @Test
    @DisplayName("존재하지 않는 스타디움에 코트를 등록할 때 IllegalArgumentException이 발생해야 한다")
    void registerCourt_whenStadiumDoesNotExist() {
        BigDecimal price = new BigDecimal("20000");
        CourtRegisterServiceRequest request = new CourtRegisterServiceRequest(
                -1L,
                "New Court",
                "New Description",
                price
        );

        assertThatThrownBy(() -> courtService.registerCourt(request, testMember.getMemberId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.STADIUM_NOT_FOUND.getText());
    }

    @Test
    @DisplayName("존재하지 않는 코트를 수정할 때 IllegalArgumentException이 발생해야 한다")
    void updateCourt_whenCourtDoesNotExist() {
        BigDecimal updatedPrice = new BigDecimal("25000");
        CourtUpdateServiceRequest request = new CourtUpdateServiceRequest(
                testStadium.getStadiumId(),
                "Updated Court",
                "Updated Description",
                updatedPrice
        );

        Long invalidCourtId = -1L;

        assertThatThrownBy(() -> courtService.updateCourt(request, testMember.getMemberId(), invalidCourtId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.COURT_NOT_FOUND.getText());
    }

    @Test
    @DisplayName("존재하지 않는 코트를 삭제할 때 IllegalArgumentException이 발생해야 한다")
    void deleteCourt_whenCourtDoesNotExist() {
        CourtDeleteServiceRequest request = new CourtDeleteServiceRequest(
                testStadium.getStadiumId()
        );

        Long invalidCourtId = -1L;

        assertThatThrownBy(() -> courtService.deleteCourt(request, testMember.getMemberId(), invalidCourtId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.COURT_NOT_FOUND.getText());
    }

    @Test
    @DisplayName("다른 회원의 스타디움에 코트를 등록할 때 IllegalArgumentException이 발생해야 한다")
    void registerCourt_whenMemberDoesNotOwnStadium() {
        Member anotherMember = Member.builder()
                .email("another@example.com")
                .password("password")
                .name("Another User")
                .phoneNumber("010-5678-1234")
                .loginType(LoginType.builder()
                        .loginProvider(LoginProvider.ORIGINAL)
                        .snsId("another@example.com")
                        .build())
                .gender(Gender.FEMALE)
                .memberRole(MemberRole.USER)
                .termsAgreed(TermsAgreed.AGREE)
                .build();
        memberRepository.save(anotherMember);

        BigDecimal price = new BigDecimal("20000");
        CourtRegisterServiceRequest request = new CourtRegisterServiceRequest(
                testStadium.getStadiumId(),
                "New Court",
                "New Description",
                price
        );

        assertThatThrownBy(() -> courtService.registerCourt(request, anotherMember.getMemberId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.getText());
    }

    @Test
    @DisplayName("다른 회원의 스타디움의 코트를 수정할 때 IllegalArgumentException이 발생해야 한다")
    void updateCourt_whenMemberDoesNotOwnStadium() {
        Member anotherMember = Member.builder()
                .email("another@example.com")
                .password("password")
                .name("Another User")
                .phoneNumber("010-5678-1234")
                .loginType(LoginType.builder()
                        .loginProvider(LoginProvider.ORIGINAL)
                        .snsId("another@example.com")
                        .build())
                .gender(Gender.FEMALE)
                .memberRole(MemberRole.USER)
                .termsAgreed(TermsAgreed.AGREE)
                .build();
        memberRepository.save(anotherMember);

        Court court = courtRepository.findAll().get(0);

        BigDecimal updatedPrice = new BigDecimal("0");
        CourtUpdateServiceRequest request = new CourtUpdateServiceRequest(
                testStadium.getStadiumId(),
                "Unauthorized Update",
                "Unauthorized Description",
                updatedPrice
        );

        assertThatThrownBy(() -> courtService.updateCourt(request, anotherMember.getMemberId(), court.getCourtId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.getText());
    }

    @Test
    @DisplayName("다른 회원의 스타디움의 코트를 삭제할 때 IllegalArgumentException이 발생해야 한다")
    void deleteCourt_whenMemberDoesNotOwnStadium() {
        Member anotherMember = Member.builder()
                .email("another@example.com")
                .password("password")
                .name("Another User")
                .phoneNumber("010-5678-1234")
                .loginType(LoginType.builder()
                        .loginProvider(LoginProvider.ORIGINAL)
                        .snsId("another@example.com")
                        .build())
                .gender(Gender.FEMALE)
                .memberRole(MemberRole.USER)
                .termsAgreed(TermsAgreed.AGREE)
                .build();
        memberRepository.save(anotherMember);

        Court court = courtRepository.findAll().get(0);

        CourtDeleteServiceRequest request = new CourtDeleteServiceRequest(
                testStadium.getStadiumId()
        );

        assertThatThrownBy(() -> courtService.deleteCourt(request, anotherMember.getMemberId(), court.getCourtId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.getText());
    }
}
