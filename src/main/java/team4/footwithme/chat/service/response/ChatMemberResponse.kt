package team4.footwithme.chat.service.response

import team4.footwithme.chat.domain.ChatMember

@JvmRecord
data class ChatMemberResponse(
    val chatroomId: Long?,
    val memberId: Long?
) {
    constructor(chatMember: ChatMember) : this(
        chatMember.chatRoom!!.chatroomId,
        chatMember.member!!.memberId
    )
}
