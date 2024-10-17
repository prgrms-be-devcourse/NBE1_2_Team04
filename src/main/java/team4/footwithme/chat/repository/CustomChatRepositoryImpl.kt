package team4.footwithme.chat.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.*
import team4.footwithme.chat.domain.Chat
import team4.footwithme.chat.domain.Chatroom
import team4.footwithme.chat.domain.QChat
import team4.footwithme.chat.service.response.ChatResponse
import team4.footwithme.global.domain.IsDeleted

class CustomChatRepositoryImpl(private val queryFactory: JPAQueryFactory) : CustomChatRepository {
    override fun findChatByChatroom(chatroom: Chatroom, pageable: Pageable): Slice<ChatResponse> {
        val pageSize = pageable.pageSize
        val chats = getChatList(chatroom, pageable).toMutableList()
        var hasNext = false
        if (chats.size > pageSize) {
            chats.removeAt(pageSize)
            hasNext = true
        }

        val content = chats.stream().map { chat: Chat -> ChatResponse(chat) }.toList()

        //        Long count = getCount(chatroom);
        return SliceImpl(content, pageable, hasNext)
    }

    //Page<> 형태로 반환할 때 PageImpl에 사용
    private fun getCount(chatroom: Chatroom): Long? {
        return queryFactory
            .select(QChat.chat.count())
            .from(QChat.chat)
            .where(
                QChat.chat.isDeleted.eq(IsDeleted.FALSE)
                    .and(QChat.chat.chatRoom.eq(chatroom))
            )
            .fetchOne()
    }

    private fun getChatList(chatroom: Chatroom, pageable: Pageable): List<Chat> {
        return queryFactory
            .select(QChat.chat)
            .from(QChat.chat)
            .where(
                QChat.chat.isDeleted.eq(IsDeleted.FALSE)
                    .and(QChat.chat.chatRoom.eq(chatroom))
            )
            .orderBy(QChat.chat.createdAt.desc())
            .offset(pageable.offset) // 페이지 번호
            .limit((pageable.pageSize + 1).toLong()) // 페이지 사이즈
            .fetch()
    }
}
