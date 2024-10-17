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
import team4.footwithme.stadium.domain.Stadium;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class StadiumRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;
    private Stadium stadium1;
    private Stadium stadium2;
    private Stadium stadium3;

    @BeforeEach
    void setUp() {
        testMember = Member.create("test@example.com", "password", "Test User", "010-1234-5678",
            LoginProvider.ORIGINAL, "test@example.com", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.save(testMember);

        stadium1 = Stadium.create(testMember, "Stadium1", "seoul", "010-1111-1111", "Description1", 37.5665, 126.9780);
        stadium2 = Stadium.create(testMember, "Stadium2", "busan", "010-2222-2222", "Description2", 35.1796, 129.0756);
        stadium3 = Stadium.create(testMember, "Stadium3", "daejeon", "010-3333-3333", "Description3", 36.3504, 127.3845);

        stadiumRepository.save(stadium1);
        stadiumRepository.save(stadium2);
        stadiumRepository.save(stadium3);
    }

    @Test
    @DisplayName("findActiveById는 삭제되지 않은 풋살장을 조회한다")
    void findActiveById_returnsActiveStadium() {
        Optional<Stadium> foundStadium = stadiumRepository.findActiveById(stadium1.stadiumId);

        assertThat(foundStadium).isPresent();
        assertThat(foundStadium.get()).isEqualTo(stadium1);
    }

    @Test
    @DisplayName("findActiveById는 삭제된 풋살장을 조회하지 않는다")
    void findActiveById_doesNotReturnDeletedStadium() {
        stadiumRepository.delete(stadium1);

        Optional<Stadium> foundStadium = stadiumRepository.findActiveById(stadium1.stadiumId);

        assertThat(foundStadium).isNotPresent();
    }

    @Test
    @DisplayName("findAllActiveStadiums는 삭제되지 않은 모든 풋살장을 조회한다")
    void findAllActiveStadiums_returnsAllActiveStadiums() {
        Slice<Stadium> stadiums = stadiumRepository.findAllActiveStadiums(PageRequest.of(0, 10));

        assertThat(stadiums.getContent())
            .hasSize(3)
            .extracting(Stadium::getName)
            .containsExactlyInAnyOrder("Stadium1", "Stadium2", "Stadium3");
    }

    @Test
    @DisplayName("findAllActiveStadiums는 삭제된 풋살장을 조회하지 않는다")
    void findAllActiveStadiums_doesNotReturnDeletedStadiums() {
        stadiumRepository.delete(stadium2);

        Slice<Stadium> stadiums = stadiumRepository.findAllActiveStadiums(PageRequest.of(0, 10));

        assertThat(stadiums.getContent())
            .hasSize(2)
            .extracting(Stadium::getName)
            .containsExactlyInAnyOrder("Stadium1", "Stadium3");
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase는 이름에 특정 문자열이 포함된 풋살장을 조회한다")
    void findByNameContainingIgnoreCase_returnsMatchingStadiums() {
        Slice<Stadium> stadiums = stadiumRepository.findByNameContainingIgnoreCase("stadium", PageRequest.of(0, 10));

        assertThat(stadiums.getContent())
            .hasSize(3)
            .extracting(Stadium::getName)
            .containsExactlyInAnyOrder("Stadium1", "Stadium2", "Stadium3");
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase는 삭제된 풋살장을 조회하지 않는다")
    void findByNameContainingIgnoreCase_doesNotReturnDeletedStadiums() {
        stadiumRepository.delete(stadium3);

        Slice<Stadium> stadiums = stadiumRepository.findByNameContainingIgnoreCase("stadium", PageRequest.of(0, 10));

        assertThat(stadiums.getContent())
            .hasSize(2)
            .extracting(Stadium::getName)
            .containsExactlyInAnyOrder("Stadium1", "Stadium2");
    }

    @Test
    @DisplayName("findByAddressContainingIgnoreCase는 주소에 특정 문자열이 포함된 풋살장을 조회한다")
    void findByAddressContainingIgnoreCase_returnsMatchingStadiums() {
        Slice<Stadium> stadiums = stadiumRepository.findByAddressContainingIgnoreCase("Seoul", PageRequest.of(0, 10));

        assertThat(stadiums.getContent())
            .hasSize(1)
            .extracting(Stadium::getName)
            .containsExactly("Stadium1");
    }

    @Test
    @DisplayName("findByAddressContainingIgnoreCase는 삭제된 풋살장을 조회하지 않는다")
    void findByAddressContainingIgnoreCase_doesNotReturnDeletedStadiums() {
        stadiumRepository.delete(stadium1);

        Slice<Stadium> stadiums = stadiumRepository.findByAddressContainingIgnoreCase("Seoul", PageRequest.of(0, 10));

        assertThat(stadiums.getContent()).isEmpty();
    }
}
