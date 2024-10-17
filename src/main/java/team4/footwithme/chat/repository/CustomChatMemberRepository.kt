package team4.footwithme.chat.repository

import team4.footwithme.chat.domain.ChatMember
import team4.footwithme.chat.domain.Chatroom
import team4.footwithme.member.domain.Member

interface CustomChatMemberRepository {
    fun existByMemberAndChatroom(member: Member, chatroom: Chatroom): Boolean

    fun findByChatroom(chatroom: Chatroom): List<ChatMember>?
}
