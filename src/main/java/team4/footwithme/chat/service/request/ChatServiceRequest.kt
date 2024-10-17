package team4.footwithme.chat.service.request

// TODO Validation 구현하기
@JvmRecord
data class ChatServiceRequest(
    val ChatroomId: Long?,
    val message: String?
)
