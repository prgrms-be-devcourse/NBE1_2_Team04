package team4.footwithme.stadium.repository;

import org.assertj.core.api.Assertions;
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

    @DisplayName("구장 이름을 IN절을 사용해 구장ID로 조회한다.")
    @Test
    void findStadiumNamesByStadiumIds() {
        //given
        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium savedStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium2 = Stadium.create(savedMember, "열정 풋살장", "서울시 강서구 어딘가", "01099999999", "열정 있음", 78.90, 9.876);
        Stadium savedStadium3 = Stadium.create(savedMember, "우주 풋살장", "서울시 동작구 어딘가", "01055555555", "우주에 있음", 65.4321, 12.345);
        Stadium savedStadium4 = Stadium.create(savedMember, "미친 풋살장", "서울시 강북구 어딘가", "01044444444", "강북에 있음", 19.8374, 67.765);
        stadiumRepository.saveAll(List.of(savedStadium1, savedStadium2, savedStadium3, savedStadium4));

        //when
        List<String> stadiumNames = stadiumRepository.findStadiumNamesByStadiumIds(List.of(savedStadium1.getStadiumId(), savedStadium4.getStadiumId()));

        //then
        Assertions.assertThat(stadiumNames).hasSize(2)
                .containsExactly("최강 풋살장", "미친 풋살장");
    }

    @DisplayName("구장 개수를 IN절을 사용해 구장ID로 조회한다.")
    @Test
    void countStadiumByStadiumIds() {
        //given
        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium savedStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium2 = Stadium.create(savedMember, "열정 풋살장", "서울시 강서구 어딘가", "01099999999", "열정 있음", 78.90, 9.876);
        Stadium savedStadium3 = Stadium.create(savedMember, "우주 풋살장", "서울시 동작구 어딘가", "01055555555", "우주에 있음", 65.4321, 12.345);
        Stadium savedStadium4 = Stadium.create(savedMember, "미친 풋살장", "서울시 강북구 어딘가", "01044444444", "강북에 있음", 19.8374, 67.765);
        List<Stadium> stadiums = stadiumRepository.saveAll(List.of(savedStadium1, savedStadium2, savedStadium3, savedStadium4));
        //when
        Long count = stadiumRepository.countStadiumByStadiumIds(List.of(savedStadium1.getStadiumId(), savedStadium4.getStadiumId()));

        //then
        Assertions.assertThat(count).isEqualTo(2);
    }

    @DisplayName("구장 이름을 구장ID로 조회한다.")
    @Test
    void findStadiumNameById() {
        //given
        Member givenMember = Member.create("test@gmail.com", "1234", "test", "010-1234-5678", LoginProvider.ORIGINAL, "test", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);
        Member savedMember = memberRepository.save(givenMember);

        Stadium savedStadium1 = Stadium.create(savedMember, "최강 풋살장", "서울시 강남구 어딘가", "01010101010", "최고임", 54.123, 10.123);
        Stadium savedStadium2 = Stadium.create(savedMember, "열정 풋살장", "서울시 강서구 어딘가", "01099999999", "열정 있음", 78.90, 9.876);
        Stadium savedStadium3 = Stadium.create(savedMember, "우주 풋살장", "서울시 동작구 어딘가", "01055555555", "우주에 있음", 65.4321, 12.345);
        Stadium savedStadium4 = Stadium.create(savedMember, "미친 풋살장", "서울시 강북구 어딘가", "01044444444", "강북에 있음", 19.8374, 67.765);
        List<Stadium> stadiums = stadiumRepository.saveAll(List.of(savedStadium1, savedStadium2, savedStadium3, savedStadium4));
        //when
        String stadiumName = stadiumRepository.findStadiumNameById(savedStadium1.getStadiumId());
        //then
        Assertions.assertThat(stadiumName).isEqualTo("최강 풋살장");
    }

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