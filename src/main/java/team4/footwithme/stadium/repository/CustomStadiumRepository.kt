package team4.footwithme.stadium.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import team4.footwithme.stadium.domain.Stadium;

public interface CustomStadiumRepository {

    Slice<Stadium> findStadiumsByLocation(Double latitude, Double longitude, Double distance, Pageable pageable);

}
