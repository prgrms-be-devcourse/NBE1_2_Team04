package team4.footwithme.chat.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import team4.footwithme.chat.service.request.ChatServiceRequest
import team4.footwithme.chat.service.request.ChatUpdateServiceRequest
import team4.footwithme.chat.service.response.ChatResponse
import team4.footwithme.member.domain.Member

interface ChatService {
    fun sendMessage(request: ChatServiceRequest?, token: String)

    fun getChatList(chatroomId: Long?, pageRequest: PageRequest?, member: Member?): Slice<ChatResponse>

    fun updateChat(request: ChatUpdateServiceRequest?, member: Member?, chatId: Long?): ChatResponse

    fun deleteChat(member: Member?, chatId: Long?): Long?
}
