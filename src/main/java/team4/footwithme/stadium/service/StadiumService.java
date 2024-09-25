package team4.footwithme.stadium.service;

import team4.footwithme.stadium.domain.Stadium;

import java.util.List;

public interface StadiumService {
    List<Stadium> getStadiums();







    Stadium findByIdOrThrowStadiumException(long id);
}
