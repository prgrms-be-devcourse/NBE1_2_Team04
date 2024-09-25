package team4.footwithme.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team4.footwithme.chat.domain.Chatroom;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
