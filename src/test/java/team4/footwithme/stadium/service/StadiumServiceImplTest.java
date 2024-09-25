package team4.footwithme.stadium.service;

import org.junit.jupiter.api.BeforeEach;
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
import team4.footwithme.global.exception.Stadium.StadiumException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void getStadiumList() {
        List<StadiumsResponse> result = stadiumService.getStadiumList();

        assertEquals(2, result.size());
        assertEquals("Stadium1", result.get(0).name());
        assertEquals("Stadium2", result.get(1).name());
    }

    @Test
    void getStadiumDetail() {
        Stadium stadium = stadiumRepository.findAll().get(0);

        StadiumDetailResponse response = stadiumService.getStadiumDetail(stadium.getStadiumId());

        assertNotNull(response);
        assertEquals("Stadium1", response.name());
        assertEquals("Address1", response.address());
        assertEquals("123-4567", response.phoneNumber());
        assertEquals(1.0, response.latitude());
        assertEquals(2.0, response.longitude());
    }

    @Test
    void findByIdOrThrowStadiumException_whenStadiumExists() {
        Stadium stadium = stadiumRepository.findAll().get(0);

        Stadium result = stadiumService.findByIdOrThrowStadiumException(stadium.getStadiumId());

        assertNotNull(result);
        assertEquals("Stadium1", result.getName());
    }

    @Test
    void findByIdOrThrowStadiumException_whenStadiumDoesNotExist() {

        Long invalidId = 999L;

        Exception exception = assertThrows(StadiumException.class, () -> {
            stadiumService.findByIdOrThrowStadiumException(invalidId);
        });

        assertEquals("해당 구장을 찾을 수 없습니다.", exception.getMessage());
    }
}