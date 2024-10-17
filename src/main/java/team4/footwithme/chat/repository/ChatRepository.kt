package team4.footwithme.chat.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import team4.footwithme.chat.domain.Chat
import java.util.*

@Repository
interface ChatRepository : JpaRepository<Chat?, Long?>, CustomChatRepository {
    @Query("select c from Chat c where c.isDeleted = 'false' and c.chatRoom.chatroomId = ?1")
    fun findByChatRoom_ChatroomId(chatroomId: Long?): List<Chat?>?

    @Query("select c from Chat c where c.isDeleted = 'false' and c.chatId = ?1")
    fun findByChatId(chatId: Long?): Optional<Chat>
}
