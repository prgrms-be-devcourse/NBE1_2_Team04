package team4.footwithme.stadium.repository;

import java.util.List;

public interface CustomStadiumRepository {
    List<String> findStadiumNamesByStadiumIds(List<Long> stadiumIdList);

    Long countStadiumByStadiumIds(List<Long> stadiumIds);
}
