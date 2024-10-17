package team4.footwithme.chat.repository

import jakarta.annotation.PostConstruct
import org.springframework.boot.CommandLineRunner
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import team4.footwithme.chat.domain.Chatroom
import java.util.function.Consumer

@Repository
class RedisChatroomRepository(
    private val chatroomRepository: ChatroomRepository,
    private val redisTemplate: RedisTemplate<String, Any>
) : CommandLineRunner {
    private var opsHashChatRoom: HashOperations<String, Long?, Chatroom?>? = null

    @PostConstruct
    private fun start() {
        opsHashChatRoom = redisTemplate.opsForHash()
    }

    override fun run(vararg args: String) {
        init()
    }

    private fun init() {
        // 서버 재시작 시 embeddedRedis 초기화되기 때문
        chatroomRepository.findAll().forEach(Consumer { chatroom: Chatroom? ->
            if (chatroom != null) {
                if (findChatroomFromRedis(chatroom.chatroomId) == null) {
                    createChatRoom(chatroom)
                }
            }
        })
    }

    fun findChatroomFromRedis(chatroomId: Long?): Chatroom? {
        return opsHashChatRoom!![CHAT_ROOMS, chatroomId!!]
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    fun createChatRoom(chatroom: Chatroom?) {
        opsHashChatRoom!!.put(CHAT_ROOMS, chatroom!!.chatroomId!!, chatroom)
    }

    fun deleteChatroomFromRedis(chatroomId: Long?) {
        opsHashChatRoom!!.delete(CHAT_ROOMS, chatroomId)
    }

    companion object {
        private const val CHAT_ROOMS = "CHAT_ROOM"
    }
}
