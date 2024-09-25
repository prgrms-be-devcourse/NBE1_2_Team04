package team4.footwithme.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team4.footwithme.global.exception.Stadium.StadiumException;
import team4.footwithme.global.exception.Stadium.StadiumExceptionMessage;
import team4.footwithme.stadium.api.response.StadiumDetailResponse;
import team4.footwithme.stadium.api.response.StadiumsResponse;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;

import java.util.List;
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


    public StadiumDetailResponse getStadiumDetail(Long id) {
        Stadium stadium = findByIdOrThrowStadiumException(id);

        return new StadiumDetailResponse(
                stadium.getMember().getMemberId(),
                stadium.getName(),
                stadium.getAddress(),
                stadium.getPhoneNumber(),
                stadium.getDescription(),
                stadium.getPosition().getLatitude(),
                stadium.getPosition().getLongitude()
        );
    }


    public List<StadiumsResponse> getAutocompleteSuggestions(String query) {
        List<Stadium> stadiums = stadiumRepository.findByNameContainingIgnoreCase(query);
        return stadiums.stream()
                .map(stadium -> new StadiumsResponse(stadium.getStadiumId(), stadium.getName(), stadium.getAddress()))
                .collect(Collectors.toList());
    }



    // 구장 조회 예외처리
    public Stadium findByIdOrThrowStadiumException(long id) {
        return stadiumRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {} <<<<", id, StadiumExceptionMessage.STADIUM_NOT_FOUND);
                    return new StadiumException(StadiumExceptionMessage.STADIUM_NOT_FOUND);
                });
    }
}
