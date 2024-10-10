package team4.footwithme.stadium.repository;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CustomStadiumRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("주어진 위치와 거리 내의 풋살장 목록을 조회한다.")
    @Test
    void findStadiumsByLocation_withinDistance() {
        Member testMember = Member.create("test@example.com", "password", "Test User", "010-1234-5678", LoginProvider.ORIGINAL, "test@example.com", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.save(testMember);

        Stadium stadium1 = Stadium.create(testMember, "Stadium1", "seoul", "010-1111-1111", "Description1", 37.5665, 126.9780);
        Stadium stadium2 = Stadium.create(testMember, "Stadium2", "busan", "010-2222-2222", "Description2", 35.1796, 129.0756);
        Stadium stadium3 = Stadium.create(testMember, "Stadium3", "incheon", "010-3333-3333", "Description3", 37.4563, 126.7052);
        Stadium stadium4 = Stadium.create(testMember, "Stadium4", "daejeon", "010-4444-4444", "Description4", 35.8714, 128.6014);

        stadiumRepository.saveAll(List.of(stadium1, stadium2, stadium3, stadium4));

        Double searchLatitude = 37.5665;
        Double searchLongitude = 126.9780;
        Double distance = 50.0;

        PageRequest pageable = PageRequest.of(0, 10);

        Slice<Stadium> result = stadiumRepository.findStadiumsByLocation(searchLatitude, searchLongitude, distance, pageable);

        assertThat(result.getContent())
            .hasSize(2)
            .extracting(Stadium::getName)
            .containsExactlyInAnyOrder("Stadium1", "Stadium3");
    }

    @DisplayName("주어진 위치와 거리 밖의 풋살장은 조회되지 않는다.")
    @Test
    void findStadiumsByLocation_outsideDistance() {
        Member testMember = Member.create("test@example.com", "password", "Test User", "010-1234-5678", LoginProvider.ORIGINAL, "test@example.com", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.save(testMember);

        Stadium stadium1 = Stadium.create(testMember, "Stadium1", "seoul", "010-1111-1111", "Description1", 37.5665, 126.9780);
        Stadium stadium2 = Stadium.create(testMember, "Stadium2", "busan", "010-2222-2222", "Description2", 35.1796, 129.0756);
        Stadium stadium3 = Stadium.create(testMember, "Stadium3", "incheon", "010-3333-3333", "Description3", 37.4563, 126.7052);
        Stadium stadium4 = Stadium.create(testMember, "Stadium4", "daejeon", "010-4444-4444", "Description4", 35.8714, 128.6014);

        stadiumRepository.saveAll(List.of(stadium1, stadium2, stadium3, stadium4));

        Double searchLatitude = 37.5665;
        Double searchLongitude = 126.9780;
        Double distance = 10.0;

        PageRequest pageable = PageRequest.of(0, 10);


        Slice<Stadium> result = stadiumRepository.findStadiumsByLocation(searchLatitude, searchLongitude, distance, pageable);


        assertThat(result.getContent())
            .hasSize(1)
            .extracting(Stadium::getName)
            .containsExactly("Stadium1");
    }

    @DisplayName("페이지네이션이 정상적으로 동작한다.")
    @Test
    void findStadiumsByLocation_withPagination() {
        Member testMember = Member.create("test@example.com", "password", "Test User", "010-1234-5678", LoginProvider.ORIGINAL, "test@example.com", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        memberRepository.save(testMember);

        Stadium stadium1 = Stadium.create(testMember, "Stadium1", "seoul", "010-1111-1111", "Description1", 37.5665, 126.9780);
        Stadium stadium2 = Stadium.create(testMember, "Stadium2", "busan", "010-2222-2222", "Description2", 35.1796, 129.0756);
        Stadium stadium3 = Stadium.create(testMember, "Stadium3", "incheon", "010-3333-3333", "Description3", 37.4563, 126.7052);
        Stadium stadium4 = Stadium.create(testMember, "Stadium4", "daejeon", "010-4444-4444", "Description4", 35.8714, 128.6014);

        stadiumRepository.saveAll(List.of(stadium1, stadium2, stadium3, stadium4));

        Double searchLatitude = 36.0;
        Double searchLongitude = 127.0;
        Double distance = 500.0;

        PageRequest pageable = PageRequest.of(0, 2);


        Slice<Stadium> result = stadiumRepository.findStadiumsByLocation(searchLatitude, searchLongitude, distance, pageable);


        assertThat(result.getContent())
            .hasSize(2);
        assertThat(result.hasNext()).isTrue();
    }

}