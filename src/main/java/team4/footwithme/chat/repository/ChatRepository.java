package team4.footwithme.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team4.footwithme.chat.domain.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
