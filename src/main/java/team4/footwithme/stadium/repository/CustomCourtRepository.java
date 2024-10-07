package team4.footwithme.stadium.repository;

import java.util.List;

public interface CustomCourtRepository {

    String findCourtNameByCourtId(Long courtId);

    Long countCourtByCourtIds(List<Long> courtIds);
}
