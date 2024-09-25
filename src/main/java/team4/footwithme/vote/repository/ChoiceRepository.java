package team4.footwithme.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team4.footwithme.vote.domain.Choice;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {
}
