package team4.footwithme.chat.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team4.footwithme.chat.domain.ChatMember
import team4.footwithme.chat.domain.Chatroom
import team4.footwithme.member.domain.Member

@Repository
interface ChatMemberRepository : JpaRepository<ChatMember?, Long?>, CustomChatMemberRepository {
    fun deleteByMemberAndChatRoom(member: Member?, chatroom: Chatroom?)

    @Modifying
    @Query("UPDATE ChatMember cm SET cm.isDeleted = 'true' WHERE cm.chatRoom = :chatRoom")
    fun updateIsDeletedForChatRoom(@Param("chatRoom") chatroom: Chatroom?)

    // 성능상 이슈가 존재해 QueryDSL로 대체함
    @Query("select COUNT(c.ChatMemberId) > 0 from ChatMember c where c.isDeleted='false' and c.member = :member and c.chatRoom = :chatroom")
    fun existsByMemberAndChatRoom(@Param("member") member: Member?, @Param("chatroom") chatroom: Chatroom?): Boolean
}
