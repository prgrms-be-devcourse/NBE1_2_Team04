package team4.footwithme.chat.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import team4.footwithme.chat.domain.ChatMember
import team4.footwithme.chat.domain.Chatroom
import team4.footwithme.chat.domain.QChatMember
import team4.footwithme.global.domain.IsDeleted
import team4.footwithme.member.domain.Member

class CustomChatMemberRepositoryImpl(private val queryFactory: JPAQueryFactory) : CustomChatMemberRepository {
    override fun existByMemberAndChatroom(member: Member, chatroom: Chatroom): Boolean {
        val count = queryFactory
            .selectOne()
            .from(QChatMember.chatMember)
            .where(
                QChatMember.chatMember.isDeleted.eq(IsDeleted.FALSE)
                    .and(QChatMember.chatMember.chatRoom.eq(chatroom))
                    .and(QChatMember.chatMember.member.eq(member))
            )
            .fetchFirst()

        return count != null
    }

    override fun findByChatroom(chatroom: Chatroom): List<ChatMember>? {
        return queryFactory.select(QChatMember.chatMember)
            .from(QChatMember.chatMember)
            .where(
                QChatMember.chatMember.isDeleted.eq(IsDeleted.FALSE)
                    .and(QChatMember.chatMember.chatRoom.eq(chatroom))
            )
            .fetch()
    }
}
