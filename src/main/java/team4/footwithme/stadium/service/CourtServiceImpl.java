package team4.footwithme.stadium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team4.footwithme.stadium.domain.Court;
import team4.footwithme.stadium.repository.CourtRepository;
import team4.footwithme.stadium.service.response.CourtsResponse;
import team4.footwithme.stadium.service.response.StadiumsResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourtServiceImpl implements CourtService{

    private final CourtRepository courtRepository;

    public List<CourtsResponse> getCourtsByStadiumId(Long stadiumId) {
        List<Court> courts = courtRepository.findByStadium_StadiumId(stadiumId);
        return Optional.ofNullable(courts)
                .orElse(Collections.emptyList())
                .stream()
                .map(court -> CourtsResponse.of(court.getStadium(), court))
                .collect(Collectors.toList());
    }

    public List<CourtsResponse> getAllCourts() {
        List<Court> courts = courtRepository.findAll();
        return Optional.ofNullable(courts)
                .orElse(Collections.emptyList())
                .stream()
                .map(court -> CourtsResponse.of(court.getStadium(), court))
                .collect(Collectors.toList());
    }
}
