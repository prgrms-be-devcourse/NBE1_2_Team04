package team4.footwithme.chat.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity
import team4.footwithme.member.domain.Member
import java.io.Serial
import java.io.Serializable

@Entity
@SQLDelete(sql = "UPDATE chat SET is_deleted = 'true' WHERE chat_id = ?")
class Chat : BaseEntity, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val chatId: Long? = null

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    var chatRoom: Chatroom? = null
        

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member? = null
        

    var text: @NotNull String? = null
        

    @Enumerated(EnumType.STRING)
    var chatType: @NotNull ChatType? = null
        

    private constructor(chatRoom: Chatroom?, member: Member?, chatType: ChatType?, text: String?) {
        this.chatRoom = chatRoom
        this.member = member
        this.chatType = chatType
        this.text = text
    }

    protected constructor()

    fun updateMessage(message: String?) {
        this.text = message
    }

    class ChatBuilder internal constructor() {
        private var chatRoom: Chatroom? = null
        private var member: Member? = null
        private var chatType: ChatType? = null
        private var text: String? = null

        fun chatRoom(chatRoom: Chatroom?): ChatBuilder {
            this.chatRoom = chatRoom
            return this
        }

        fun member(member: Member?): ChatBuilder {
            this.member = member
            return this
        }

        fun chatType(chatType: ChatType?): ChatBuilder {
            this.chatType = chatType
            return this
        }

        fun text(text: String?): ChatBuilder {
            this.text = text
            return this
        }

        fun build(): Chat {
            return Chat(this.chatRoom, this.member, this.chatType, this.text)
        }

        override fun toString(): String {
            return "Chat.ChatBuilder(chatRoom=" + this.chatRoom + ", member=" + this.member + ", chatType=" + this.chatType + ", text=" + this.text + ")"
        }
    }

    companion object {
        @Serial
        private val serialVersionUID = -2501100146410218751L

        fun create(chatRoom: Chatroom?, member: Member?, chatType: ChatType?, text: String?): Chat {
            return builder()
                .chatRoom(chatRoom)
                .member(member)
                .chatType(chatType)
                .text(text)
                .build()
        }

        fun createTalkChat(chatRoom: Chatroom?, member: Member?, text: String?): Chat {
            return builder()
                .chatRoom(chatRoom)
                .member(member)
                .chatType(ChatType.TALK)
                .text(text)
                .build()
        }

        fun createEnterChat(chatRoom: Chatroom?, member: Member?): Chat {
            return builder()
                .chatRoom(chatRoom)
                .member(member)
                .chatType(ChatType.ENTER)
                .text(member!!.name + "님이 입장했습니다.")
                .build()
        }

        fun createGroupEnterChat(chatRoom: Chatroom?, chatMembers: List<ChatMember>): Chat {
            return builder()
                .chatRoom(chatRoom)
                .member(chatMembers[0].member)
                .chatType(ChatType.ENTER)
                .text(chatMembers[0].member?.name + "님 등 " + chatMembers.size + "명이 입장했습니다.")
                .build()
        }

        fun createQuitChat(chatRoom: Chatroom?, member: Member?): Chat {
            return builder()
                .chatRoom(chatRoom)
                .member(member)
                .chatType(ChatType.QUIT)
                .text(member!!.name + "님이 채팅방을 떠났습니다.")
                .build()
        }

        fun builder(): ChatBuilder {
            return ChatBuilder()
        }
    }
}
