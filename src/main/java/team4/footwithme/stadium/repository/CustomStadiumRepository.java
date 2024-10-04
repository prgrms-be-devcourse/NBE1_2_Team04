package team4.footwithme.stadium.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import team4.footwithme.stadium.domain.Stadium;

import java.util.List;

public interface CustomStadiumRepository {
    List<String> findStadiumNamesByStadiumIds(List<Long> stadiumIdList);

    Long countStadiumByStadiumIds(List<Long> stadiumIds);

    Slice<Stadium> findStadiumsByLocation(Double latitude, Double longitude, Double distance, Pageable pageable);

    String findStadiumNameById(Long stadiumId);
}
