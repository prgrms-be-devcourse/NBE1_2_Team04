package team4.footwithme.global.repository;

import java.util.Optional;

public interface CustomGlobalRepository<T> {
    Optional<T> findActiveById(Long id);
}
