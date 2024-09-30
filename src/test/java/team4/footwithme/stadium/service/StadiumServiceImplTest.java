package team4.footwithme.stadium.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.IntegrationTestSupport;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Position;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class StadiumServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private StadiumServiceImpl stadiumService;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
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

        Position position2 = Position.builder()
            .latitude(35.1379222)
            .longitude(129.05562775)
            .build();

        Position position3 = Position.builder()
            .latitude(36.3504119)
            .longitude(127.3845475)
            .build();

        Stadium stadium1 = Stadium.builder()
            .name("Stadium1")
            .address("seoul")
            .phoneNumber("123-4567")
            .description("Description1")
            .position(position1)
            .member(testMember)
            .build();

        Stadium stadium2 = Stadium.builder()
            .name("Stadium2")
            .address("busan")
            .phoneNumber("890-1234")
            .description("Description2")
            .position(position2)
            .member(testMember)
            .build();

        Stadium stadium3 = Stadium.builder()
            .name("Stadium3")
            .address("daegu")
            .phoneNumber("321-6547")
            .description("Description3")
            .position(position3)
            .member(testMember)
            .build();

        stadiumRepository.save(stadium1);
        stadiumRepository.save(stadium2);
        stadiumRepository.save(stadium3);
    }

    @Test
    @DisplayName("구장 목록을 정상적으로 반환해야 한다")
    void getStadiumList() {
        List<StadiumsResponse> result = stadiumService.getStadiumList();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).name()).isEqualTo("Stadium1");
        assertThat(result.get(1).name()).isEqualTo("Stadium2");
        assertThat(result.get(2).name()).isEqualTo("Stadium3");
    }

    @Test
    @DisplayName("구장 이름이 매우 긴 경우에도 정상적으로 저장되고 반환되어야 한다")
    void getStadiumList_withLongStadiumName() {
        String longName = "A".repeat(255);
        Position position = Position.builder()
            .latitude(37.5665)
            .longitude(126.9780)
            .build();
        Stadium stadium = Stadium.builder()
            .name(longName)
            .address("LongNameAddress")
            .phoneNumber("123-4567")
            .position(position)
            .member(testMember)
            .build();
        stadiumRepository.save(stadium);

        List<StadiumsResponse> result = stadiumService.getStadiumList();

        assertThat(result.get(3).name()).isEqualTo(longName);
    }

    @Test
    @DisplayName("특정 구장의 상세 정보를 정상적으로 반환해야 한다")
    void getStadiumDetail() {
        Stadium stadium = stadiumRepository.findAll().get(0);

        StadiumDetailResponse response = stadiumService.getStadiumDetail(stadium.getStadiumId());

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Stadium1");
        assertThat(response.address()).isEqualTo("seoul");
        assertThat(response.phoneNumber()).isEqualTo("123-4567");
        assertThat(response.latitude()).isEqualTo(37.5642135);
        assertThat(response.longitude()).isEqualTo(127.0016985);
    }

    @Test
    @DisplayName("이름 검색어로 구장 리스트를 정상적으로 반환해야 한다")
    void searchStadiumByName() {
        List<StadiumsResponse> result = stadiumService.getStadiumsByName("2");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Stadium2");
    }

    @Test
    @DisplayName("주어진 거리 내의 구장 목록을 반환해야 한다")
    void getStadiumsWithinDistance_shouldReturnStadiumsWithinGivenDistance() {
        Double searchLatitude = 37.5665;
        Double searchLongitude = 126.9780;
        Double distance = 100.0;

        StadiumSearchByLocationServiceRequest request = new StadiumSearchByLocationServiceRequest(
            searchLatitude,
            searchLongitude,
            distance
        );

        List<StadiumsResponse> result = stadiumService.getStadiumsWithinDistance(request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Stadium1");
    }

    @Test
    @DisplayName("주어진 거리 외의 구장이 반환되지 않아야 한다")
    void getStadiumsWithinDistance_shouldReturnEmptyListWhenTooFar() {
        Double searchLatitude = 33.450701;
        Double searchLongitude = 126.570667;
        Double distance = 1.0;
        StadiumSearchByLocationServiceRequest request = new StadiumSearchByLocationServiceRequest(
            searchLatitude,
            searchLongitude,
            distance
        );

        List<StadiumsResponse> result = stadiumService.getStadiumsWithinDistance(request);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("존재하는 구장을 조회할 때 예외 없이 정상적으로 반환해야 한다")
    void findByIdOrThrowException_whenStadiumExists() {
        Stadium stadium = stadiumRepository.findAll().get(0);

        Stadium result = stadiumService.findStadiumByIdOrThrowException(stadium.getStadiumId());

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Stadium1");
    }

    @Test
    @DisplayName("존재하지 않는 구장을 조회할 때 IllegalArgumentException이 발생해야 한다")
    void findByIdOrThrowException_whenStadiumDoesNotExist() {
        Long invalidId = 999L;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stadiumService.findStadiumByIdOrThrowException(invalidId);
        });

        assertThat(exception.getMessage()).isEqualTo("해당 풋살장을 찾을 수 없습니다.");
    }
}
