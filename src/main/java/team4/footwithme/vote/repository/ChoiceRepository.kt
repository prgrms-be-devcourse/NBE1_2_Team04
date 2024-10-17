package team4.footwithme.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team4.footwithme.vote.domain.Choice;

public interface ChoiceRepository extends JpaRepository<Choice, Long>, CustomChoiceRepository {

}
