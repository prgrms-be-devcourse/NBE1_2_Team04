package team4.footwithme.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.service.request.StadiumRegisterServiceRequest;
import team4.footwithme.stadium.service.request.StadiumUpdateServiceRequest;
import team4.footwithme.stadium.exception.StadiumExceptionMessage;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;

    private final MemberRepository memberRepository;

    // 구장 목록 조회
    public List<StadiumsResponse> getStadiumList() {
        return stadiumRepository.findAll().stream()
                .map(stadium -> new StadiumsResponse(stadium.getStadiumId(), stadium.getName(), stadium.getAddress()))
                .toList();
    }

    // 구장 상세 정보 조회
    public StadiumDetailResponse getStadiumDetail(Long id) {
        Stadium stadium = findStadiumByIdOrThrowException(id);

        return StadiumDetailResponse.of(stadium);
    }

    // 이름으로 구장 검색
    public List<StadiumsResponse> getStadiumsByName(String query) {
        List<Stadium> stadiums = stadiumRepository.findByNameContainingIgnoreCase(query);
        return Optional.ofNullable(stadiums)
                .orElse(Collections.emptyList())
                .stream()
                .map(stadium -> StadiumsResponse.of(stadium))
                .collect(Collectors.toList());
    }

    // 주소로 구장 검색
    public List<StadiumsResponse> getStadiumsByAddress(String address) {
        List<Stadium> stadiums = stadiumRepository.findByAddressContainingIgnoreCase(address);
        return Optional.ofNullable(stadiums)
                .orElse(Collections.emptyList())
                .stream()
                .map(stadium -> StadiumsResponse.of(stadium))  // of 메서드 사용
                .collect(Collectors.toList());
    }

    // 위도, 경도의 일정 거리 내의 구장 목록 반환
    public List<StadiumsResponse> getStadiumsWithinDistance(StadiumSearchByLocationServiceRequest request) {
        System.out.println(request.latitude());
        System.out.println(request.longitude());
        System.out.println(request.distance());
        List<Stadium> stadiums = stadiumRepository.findStadiumsByLocation(request.latitude(), request.longitude(), request.distance());
        return stadiums.stream()
                .map(stadium -> new StadiumsResponse(stadium.getStadiumId(), stadium.getName(), stadium.getAddress()))
                .collect(Collectors.toList());
    }


    public StadiumDetailResponse registerStadium(StadiumRegisterServiceRequest request, Long memberId) {
        Member member = findMemberByIdOrThrowException(memberId);

        // Stadium 엔티티 생성
        Stadium stadium = Stadium.create(
                member,
                request.name(),
                request.address(),
                request.phoneNumber(),
                request.description(),
                request.latitude(),
                request.longitude()
        );

        // Stadium 저장
        stadiumRepository.save(stadium);

        // StadiumDetailResponse로 변환하여 반환
        return StadiumDetailResponse.of(stadium);
    }


    public StadiumDetailResponse updateStadium(Long stadiumId, StadiumUpdateServiceRequest request) {
        return null;
    }


    public void deleteStadium(Long stadiumId) {

    }

    // 풋살장 조회 예외처리
    public Stadium findStadiumByIdOrThrowException(long id) {
        return stadiumRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, StadiumExceptionMessage.STADIUM_NOT_FOUND);
                    return new IllegalArgumentException(StadiumExceptionMessage.STADIUM_NOT_FOUND.getText());
                });
    }

    //맴버 조회 예외처리
    public Member findMemberByIdOrThrowException(long id) {
        return memberRepository.findByMemberId(id)
                .orElseThrow(()-> {
                    log.warn(">>>> {} : {} <<<<", id, "없는 맴버입니다.");
                    return new IllegalArgumentException("없는 맴버입니다.");
                });
    }
}
