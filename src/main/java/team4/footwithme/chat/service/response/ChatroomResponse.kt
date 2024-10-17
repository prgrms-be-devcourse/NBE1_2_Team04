package team4.footwithme.chat.service.response

import team4.footwithme.chat.domain.Chatroom

@JvmRecord
data class ChatroomResponse(
    val chatroomId: Long?,
    val name: String?
) {
    constructor(chatroom: Chatroom?) : this(
        chatroom!!.chatroomId,
        chatroom.name
    )
}
