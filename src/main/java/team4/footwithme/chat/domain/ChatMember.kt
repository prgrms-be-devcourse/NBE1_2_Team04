package team4.footwithme.chat.domain

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity
import team4.footwithme.member.domain.Member

@Entity
@SQLDelete(sql = "UPDATE chat_member SET is_deleted = 'true' WHERE chat_member_id = ?")
class ChatMember : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val chatMemberId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member? = null
        

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    var chatRoom: Chatroom? = null
        

    private constructor(member: Member?, chatRoom: Chatroom?) {
        this.member = member
        this.chatRoom = chatRoom
    }

    protected constructor()

    class ChatMemberBuilder internal constructor() {
        private var member: Member? = null
        private var chatRoom: Chatroom? = null

        fun member(member: Member?): ChatMemberBuilder {
            this.member = member
            return this
        }

        fun chatRoom(chatRoom: Chatroom?): ChatMemberBuilder {
            this.chatRoom = chatRoom
            return this
        }

        fun build(): ChatMember {
            return ChatMember(this.member, this.chatRoom)
        }

        override fun toString(): String {
            return "ChatMember.ChatMemberBuilder(member=" + this.member + ", chatRoom=" + this.chatRoom + ")"
        }
    }

    companion object {
        @JvmStatic
        fun create(member: Member?, chatRoom: Chatroom?): ChatMember {
            return builder()
                .member(member)
                .chatRoom(chatRoom)
                .build()
        }

        fun builder(): ChatMemberBuilder {
            return ChatMemberBuilder()
        }
    }
}
