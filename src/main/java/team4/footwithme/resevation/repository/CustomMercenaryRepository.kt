package team4.footwithme.resevation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team4.footwithme.resevation.domain.Mercenary;

public interface CustomMercenaryRepository {
    Page<Mercenary> findAllToPage(Pageable pageable);
}
