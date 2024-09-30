package team4.footwithme.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.exception.CourtExceptionMessage;
import team4.footwithme.stadium.exception.StadiumExceptionMessage;
import team4.footwithme.stadium.repository.CourtRepository;
import team4.footwithme.stadium.repository.StadiumRepository;
import team4.footwithme.stadium.service.response.CourtDetailResponse;
import team4.footwithme.stadium.service.response.CourtsResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourtServiceImpl implements CourtService{

    private final CourtRepository courtRepository;

    private final StadiumService stadiumService;

    public List<CourtsResponse> getCourtsByStadiumId(Long stadiumId) {
        stadiumService.findByIdOrThrowException(stadiumId);
        List<Court> courts = courtRepository.findByStadium_StadiumId(stadiumId);
        return Optional.ofNullable(courts)
                .orElse(Collections.emptyList())
                .stream()
                .map(CourtsResponse::from)
                .collect(Collectors.toList());
    }

    public List<CourtsResponse> getAllCourts() {
        List<Court> courts = courtRepository.findAll();
        return Optional.of(courts)
                .orElse(Collections.emptyList())
                .stream()
                .map(CourtsResponse::from)
                .collect(Collectors.toList());
    }

    public CourtDetailResponse getCourtBycourtId(Long courtId){
        Court court = findByIdOrThrowException(courtId);
        return CourtDetailResponse.from(court);
    }

    // 구장 조회 예외처리
    public Court findByIdOrThrowException(long id) {
        return courtRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, CourtExceptionMessage.COURT_NOT_FOUND);
                    return new IllegalArgumentException(CourtExceptionMessage.COURT_NOT_FOUND.getText());
                });
    }
}
