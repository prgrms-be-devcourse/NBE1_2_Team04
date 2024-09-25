package team4.footwithme.stadium.service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team4.footwithme.global.exception.Stadium.StadiumException;
import team4.footwithme.global.exception.Stadium.StadiumExceptionMessage;
import team4.footwithme.stadium.domain.Stadium;
import team4.footwithme.stadium.repository.StadiumRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;

    // 구장 목록 조회
    public List<Stadium> getStadiums() {
        return stadiumRepository.findAll();
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
