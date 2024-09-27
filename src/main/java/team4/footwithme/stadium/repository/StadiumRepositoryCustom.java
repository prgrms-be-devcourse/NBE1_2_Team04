package team4.footwithme.stadium.repository;

import team4.footwithme.stadium.domain.Stadium;

import java.util.List;

public interface StadiumRepositoryCustom {
    List<Stadium> findStadiumsByLocation(Double latitude, Double longitude, Double distance);
}