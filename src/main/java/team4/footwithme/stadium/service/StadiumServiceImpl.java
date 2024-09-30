package team4.footwithme.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.exception.StadiumExceptionMessage;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.stadium.service.request.StadiumSearchByLocationServiceRequest;
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

    // 구장 목록 조회
    public List<StadiumsResponse> getStadiumList() {
        return stadiumRepository.findAll().stream()
            .map(stadium -> new StadiumsResponse(stadium.getStadiumId(), stadium.getName(), stadium.getAddress()))
            .toList();
    }

    // 구장 상세 정보 조회
    public StadiumDetailResponse getStadiumDetail(Long id) {
        Stadium stadium = findByIdOrThrowException(id);

        return StadiumDetailResponse.of(stadium);
    }

    // 이름으로 구장 검색
    public List<StadiumsResponse> getStadiumsByName(String query) {
        List<Stadium> stadiums = stadiumRepository.findByNameContainingIgnoreCase(query);
        return Optional.ofNullable(stadiums)
            .orElse(Collections.emptyList())
            .stream()
            .map(StadiumsResponse::of)
            .collect(Collectors.toList());
    }

    // 주소로 구장 검색
    public List<StadiumsResponse> getStadiumsByAddress(String address) {
        List<Stadium> stadiums = stadiumRepository.findByAddressContainingIgnoreCase(address);
        return Optional.ofNullable(stadiums)
            .orElse(Collections.emptyList())
            .stream()
            .map(StadiumsResponse::of)  // of 메서드 사용
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

    // 풋살장 조회 예외처리
    public Stadium findByIdOrThrowException(long id) {
        return stadiumRepository.findById(id)
            .orElseThrow(() -> {
                log.warn(">>>> {} : {} <<<<", id, StadiumExceptionMessage.STADIUM_NOT_FOUND);
                return new IllegalArgumentException(StadiumExceptionMessage.STADIUM_NOT_FOUND.getText());
            });
    }
}
