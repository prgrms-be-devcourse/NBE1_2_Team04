package team4.footwithme.chat.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import team4.footwithme.chat.domain.Chat
import team4.footwithme.chat.service.response.ChatResponse

@Service
class RedisSubscriber(
    private val objectMapper: ObjectMapper,
    private val messagingTemplate: SimpMessageSendingOperations
) {
    fun sendMessage(publishMessage: String?) {
        try {
            // ChatMessage 객채로 맵핑
            val chat = objectMapper.readValue(publishMessage, Chat::class.java)

            // Websocket 구독자에게 채팅 메시지 Send
            messagingTemplate.convertAndSend("/sub/api/v1/chat/room/" + chat.chatRoom!!.chatroomId, ChatResponse(chat))
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
            throw IllegalArgumentException("메세지 수신 오류입니다.")
        }
    }
}
