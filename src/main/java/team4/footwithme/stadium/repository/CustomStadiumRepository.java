package team4.footwithme.stadium.repository;

import team4.footwithme.stadium.domain.Stadium;

import java.util.List;

public interface CustomStadiumRepository {
    List<String> findStadiumNamesByStadiumIds(List<Long> stadiumIdList);

    Long countStadiumByStadiumIds(List<Long> stadiumIds);

    List<Stadium> findStadiumsByLocation(Double latitude, Double longitude, Double distance);
}
