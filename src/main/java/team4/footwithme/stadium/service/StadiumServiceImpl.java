package team4.footwithme.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.stadium.service.request.StadiumRegisterServiceRequest;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
import team4.footwithme.stadium.service.request.StadiumUpdateServiceRequest;
import team4.footwithme.stadium.service.response.StadiumDetailResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

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
                .map(stadium -> StadiumsResponse.of(stadium))
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


    @Transactional
    public StadiumDetailResponse registerStadium(StadiumRegisterServiceRequest request, Long memberId) {
        Member member = findMemberByIdOrThrowException(memberId);

        Stadium stadium = Stadium.create(
                member,
                request.name(),
                request.address(),
                request.phoneNumber(),
                request.description(),
                request.latitude(),
                request.longitude()
        );

        stadiumRepository.save(stadium);

        return StadiumDetailResponse.of(stadium);
    }


    @Transactional
    public StadiumDetailResponse updateStadium(StadiumUpdateServiceRequest request, Long memberId, Long stadiumId) {
        findMemberByIdOrThrowException(memberId);
        Stadium stadium = findStadiumByIdOrThrowException(stadiumId);
        if (!stadium.getMember().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException(ExceptionMessage.STADIUM_NOT_OWNED_BY_MEMBER.getText());
        }
        stadium.updateStadium(request.name(), request.address(), request.phoneNumber(), request.description(), request.latitude(), request.longitude());
        return StadiumDetailResponse.of(stadium);
    }

    @Transactional
    public void deleteStadium(Long stadiumId) {
    }

    // 풋살장 조회 예외처리
    public Stadium findStadiumByIdOrThrowException(long id) {
        return stadiumRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, ExceptionMessage.STADIUM_NOT_FOUND);
                    return new IllegalArgumentException(ExceptionMessage.STADIUM_NOT_FOUND.getText());
                });
    }

    //맴버 조회 예외처리
    public Member findMemberByIdOrThrowException(long id) {
        return memberRepository.findByMemberId(id)
                .orElseThrow(()-> {
                    log.warn(">>>> {} : {} <<<<", id, ExceptionMessage.MEMBER_NOT_FOUND);
                    return new IllegalArgumentException(ExceptionMessage.MEMBER_NOT_FOUND.getText());
                });
    }
}
