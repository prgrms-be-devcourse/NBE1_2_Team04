package team4.footwithme.stadium.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.api.response.StadiumDetailResponse;
import team4.footwithme.stadium.api.response.StadiumsResponse;
import team4.footwithme.stadium.domain.Position;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class StadiumServiceImplTest {

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

        Stadium stadium1 = Stadium.builder()
                .name("Stadium1")
                .address("Address1")
                .phoneNumber("123-4567")
                .description("Description1")
                .position(Position.builder().latitude(1.0).longitude(2.0).build())
                .member(testMember)
                .build();

        Stadium stadium2 = Stadium.builder()
                .name("Stadium2")
                .address("Address2")
                .phoneNumber("890-1234")
                .description("Description2")
                .position(Position.builder().latitude(3.0).longitude(4.0).build())
                .member(testMember)
                .build();

        stadiumRepository.save(stadium1);
        stadiumRepository.save(stadium2);
    }

    @Test
    @DisplayName("구장 목록을 정상적으로 반환해야 한다")
    void getStadiumList() {
        List<StadiumsResponse> result = stadiumService.getStadiumList();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Stadium1");
        assertThat(result.get(1).name()).isEqualTo("Stadium2");
    }

    @Test
    @DisplayName("구장 이름이 매우 긴 경우에도 정상적으로 저장되고 반환되어야 한다")
    void getStadiumList_withLongStadiumName() {
        String longName = "A".repeat(255);
        Stadium stadium = Stadium.builder()
                .name(longName)
                .address("LongNameAddress")
                .phoneNumber("123-4567")
                .position(Position.builder().latitude(1.0).longitude(2.0).build())
                .member(testMember)
                .build();
        stadiumRepository.save(stadium);

        List<StadiumsResponse> result = stadiumService.getStadiumList();

        assertThat(result.get(2).name()).isEqualTo(longName);
    }


    @Test
    @DisplayName("특정 구장의 상세 정보를 정상적으로 반환해야 한다")
    void getStadiumDetail() {
        Stadium stadium = stadiumRepository.findAll().get(0);

        StadiumDetailResponse response = stadiumService.getStadiumDetail(stadium.getStadiumId());

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Stadium1");
        assertThat(response.address()).isEqualTo("Address1");
        assertThat(response.phoneNumber()).isEqualTo("123-4567");
        assertThat(response.latitude()).isEqualTo(1.0);
        assertThat(response.longitude()).isEqualTo(2.0);
    }

    @Test
    @DisplayName("검색어로 구장 자동완성 리스트를 정상적으로 반환해야 한다")
    void searchStadiumByName() {
        List<StadiumsResponse> result = stadiumService.searchStadiumByName("2");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Stadium2");
    }

    @Test
    @DisplayName("존재하는 구장을 조회할 때 예외 없이 정상적으로 반환해야 한다")
    void findByIdOrThrowException_whenStadiumExists() {
        Stadium stadium = stadiumRepository.findAll().get(0);

        Stadium result = stadiumService.findByIdOrThrowException(stadium.getStadiumId());

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Stadium1");
    }

    @Test
    @DisplayName("존재하지 않는 구장을 조회할 때 IllegalArgumentException이 발생해야 한다")
    void findByIdOrThrowException_whenStadiumDoesNotExist() {
        Long invalidId = 999L;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stadiumService.findByIdOrThrowException(invalidId);
        });

        assertThat(exception.getMessage()).isEqualTo("해당 구장을 찾을 수 없습니다.");
    }
}
