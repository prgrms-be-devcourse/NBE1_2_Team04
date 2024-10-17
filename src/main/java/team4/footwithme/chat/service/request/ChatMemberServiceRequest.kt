package team4.footwithme.chat.service.request

// TODO Validation 구현하기
@JvmRecord
data class ChatMemberServiceRequest(
    val chatroomId: Long?,
    val memberId: Long?
)
