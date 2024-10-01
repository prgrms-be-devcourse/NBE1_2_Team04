package team4.footwithme.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team4.footwithme.chat.domain.Chat;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, CustomChatRepository {

    @Query("select c from Chat c where c.isDeleted = 'false' and c.chatRoom.chatroomId = ?1")
    List<Chat> findByChatRoom_ChatroomId(Long chatroomId);
}
