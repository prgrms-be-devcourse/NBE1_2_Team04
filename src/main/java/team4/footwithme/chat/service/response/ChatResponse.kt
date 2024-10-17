package team4.footwithme.chat.service.response

import team4.footwithme.chat.domain.Chat
import team4.footwithme.chat.domain.ChatType
import java.time.LocalDateTime

@JvmRecord
data class ChatResponse(
    val chatId: Long?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val chatroomResponse: ChatroomResponse,
    val memberInfo: ChatMemberInfo,
    val chatType: ChatType?,
    val text: String?
) {
    constructor(chat: Chat) : this(
        chat.chatId,
        chat.createdAt,
        chat.updatedAt,
        ChatroomResponse(chat.chatRoom),
        ChatMemberInfo(chat.member),
        chat.chatType,
        chat.text
    )
}
