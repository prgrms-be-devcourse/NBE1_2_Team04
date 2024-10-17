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
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.stadium.service.request.StadiumRegisterServiceRequest;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
import team4.footwithme.stadium.service.request.StadiumUpdateServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        Stadium stadium1 = Stadium.create(testMember, "Stadium1", "seoul", "010-1111-1111", "Description1",
            37.5642135, 127.0016985);

        Stadium stadium2 = Stadium.create(testMember, "Stadium2", "busan", "010-2222-2222", "Description2",
            35.1379222, 129.05562775);

        Stadium stadium3 = Stadium.create(testMember, "Stadium3", "daejeon", "010-3333-3333", "Description3",
            36.3504119, 127.3845475);

        stadiumRepository.save(stadium1);
        stadiumRepository.save(stadium2);
        stadiumRepository.save(stadium3);
    }

    @Test
    @DisplayName("풋살장 목록을 정상적으로 반환해야 한다")
    void getStadiumList() {
        Slice<StadiumsResponse> result = stadiumService.getStadiumList(0, "name");

        assertThat(result.getContent())
            .hasSize(3)
            .extracting(StadiumsResponse::name)
            .containsExactly("Stadium1", "Stadium2", "Stadium3");
    }

    @Test
    @DisplayName("특정 풋살장의 상세 정보를 정상적으로 반환해야 한다")
    void getStadiumDetail() {
        Stadium stadium = stadiumRepository.findAll().get(0);

        StadiumDetailResponse response = stadiumService.getStadiumDetail(stadium.stadiumId);

        assertThat(response).isNotNull();
        assertThat(response)
            .extracting(
                StadiumDetailResponse::name,
                StadiumDetailResponse::address,
                StadiumDetailResponse::phoneNumber,
                StadiumDetailResponse::latitude,
                StadiumDetailResponse::longitude
            )
            .containsExactly("Stadium1", "seoul", "010-1111-1111", 37.5642135, 127.0016985);
    }

    @Test
    @DisplayName("이름 검색어로 풋살장 리스트를 정상적으로 반환해야 한다")
    void searchStadiumByName() {
        Slice<StadiumsResponse> result = stadiumService.getStadiumsByName("2", 0, "name");

        assertThat(result.getContent())
            .hasSize(1)
            .extracting(StadiumsResponse::name)
            .containsExactly("Stadium2");
    }

    @Test
    @DisplayName("주소 검색어로 풋살장 리스트를 정상적으로 반환해야 한다")
    void searchStadiumByAddress() {
        Slice<StadiumsResponse> result = stadiumService.getStadiumsByAddress("seo", 0, "name");

        assertThat(result.getContent())
            .hasSize(1)
            .extracting(StadiumsResponse::name)
            .containsExactly("Stadium1");
    }

    @Test
    @DisplayName("주어진 거리 내의 풋살장 목록을 반환해야 한다")
    void getStadiumsWithinDistance_shouldReturnStadiumsWithinGivenDistance() {
        Double searchLatitude = 37.5665;
        Double searchLongitude = 126.9780;
        Double distance = 100.0;

        StadiumSearchByLocationServiceRequest request = new StadiumSearchByLocationServiceRequest(
            searchLatitude,
            searchLongitude,
            distance
        );

        Slice<StadiumsResponse> result = stadiumService.getStadiumsWithinDistance(request, 0, "name");

        assertThat(result.getContent())
            .hasSize(1)
            .extracting(StadiumsResponse::name)
            .containsExactly("Stadium1");
    }

    @Test
    @DisplayName("주어진 거리 외의 풋살장이 반환되지 않아야 한다")
    void getStadiumsWithinDistance_shouldReturnEmptyListWhenTooFar() {
        Double searchLatitude = 33.450701;
        Double searchLongitude = 126.570667;
        Double distance = 1.0;

        StadiumSearchByLocationServiceRequest request = new StadiumSearchByLocationServiceRequest(
            searchLatitude,
            searchLongitude,
            distance
        );

        Slice<StadiumsResponse> result = stadiumService.getStadiumsWithinDistance(request, 0, "name");

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("풋살장을 정상적으로 등록해야 한다")
    void registerStadium() {
        StadiumRegisterServiceRequest request = new StadiumRegisterServiceRequest(
            "New Stadium", "Incheon", "010-4444-4444", "New Description",
            37.456, 126.705
        );

        StadiumDetailResponse response = stadiumService.registerStadium(request, testMember);

        assertThat(response).isNotNull();
        assertThat(response)
            .extracting(
                StadiumDetailResponse::name,
                StadiumDetailResponse::address
            )
            .containsExactly("New Stadium", "Incheon");
        assertThat(stadiumRepository.findById(response.stadiumId())).isPresent();
    }

    @Test
    @DisplayName("풋살장을 정상적으로 수정해야 한다")
    void updateStadium() {
        Stadium stadium = stadiumRepository.findAll().get(0);

        StadiumUpdateServiceRequest request = new StadiumUpdateServiceRequest(
            "Updated Stadium", "Updated Address", "010-9999-9999", "Updated Description",
            38.0, 128.0
        );

        StadiumDetailResponse response = stadiumService.updateStadium(request, testMember, stadium.stadiumId);

        assertThat(response).isNotNull();
        assertThat(response)
            .extracting(
                StadiumDetailResponse::name,
                StadiumDetailResponse::address
            )
            .containsExactly("Updated Stadium", "Updated Address");

        Stadium updatedStadium = stadiumRepository.findById(stadium.stadiumId).orElseThrow();
        assertThat(updatedStadium)
            .extracting(
                Stadium::getName,
                Stadium::getAddress
            )
            .containsExactly("Updated Stadium", "Updated Address");
    }

    @Test
    @DisplayName("풋살장을 정상적으로 삭제해야 한다")
    void deleteStadium() {
        Stadium stadium = stadiumRepository.findAll().get(0);

        stadiumService.deleteStadium(testMember, stadium.stadiumId);

        Optional<Stadium> deletedStadium = stadiumRepository.findById(stadium.stadiumId);
        assertThat(deletedStadium).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 풋살장을 조회할 때 IllegalArgumentException이 발생해야 한다")
    void getStadiumDetail_whenStadiumDoesNotExist() {
        Long invalidId = -1L;

        assertThatThrownBy(() -> stadiumService.getStadiumDetail(invalidId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ExceptionMessage.STADIUM_NOT_FOUND.text);
    }

    @Test
    @DisplayName("존재하지 않는 풋살장을 수정할 때 IllegalArgumentException이 발생해야 한다")
    void updateStadium_whenStadiumDoesNotExist() {
        StadiumUpdateServiceRequest request = new StadiumUpdateServiceRequest(
            "Non-existent Stadium", "Nowhere", "000-0000-0000", "No Description",
            0.0, 0.0
        );

        Long invalidStadiumId = -1L;

        assertThatThrownBy(() -> stadiumService.updateStadium(request, testMember, invalidStadiumId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ExceptionMessage.STADIUM_NOT_FOUND.text);
    }

    @Test
    @DisplayName("다른 회원의 풋살장을 수정할 때 IllegalArgumentException이 발생해야 한다")
    void updateStadium_whenMemberDoesNotOwnStadium() {
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

        Stadium stadium = stadiumRepository.findAll().get(0);

        StadiumUpdateServiceRequest request = new StadiumUpdateServiceRequest(
            "Unauthorized Update", "Unauthorized Address", "000-0000-0000", "Unauthorized",
            0.0, 0.0
        );

        assertThatThrownBy(() -> stadiumService.updateStadium(request, anotherMember, stadium.stadiumId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.text);
    }

    @Test
    @DisplayName("존재하지 않는 풋살장을 삭제할 때 IllegalArgumentException이 발생해야 한다")
    void deleteStadium_whenStadiumDoesNotExist() {
        Long invalidStadiumId = -1L;

        assertThatThrownBy(() -> stadiumService.deleteStadium(testMember, invalidStadiumId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ExceptionMessage.STADIUM_NOT_FOUND.text);
    }

    @Test
    @DisplayName("다른 회원의 풋살장을 삭제할 때 IllegalArgumentException이 발생해야 한다")
    void deleteStadium_whenMemberDoesNotOwnStadium() {
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

        Stadium stadium = stadiumRepository.findAll().get(0);

        assertThatThrownBy(() -> stadiumService.deleteStadium(anotherMember, stadium.stadiumId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.text);
    }
}
