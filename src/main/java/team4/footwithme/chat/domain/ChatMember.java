package team4.footwithme.chat.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.member.domain.Member;

@Entity
@SQLDelete(sql = "UPDATE chat_member SET is_deleted = 'true' WHERE chat_member_id = ?")
public class ChatMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ChatMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatRoom;

    private ChatMember(Member member, Chatroom chatRoom) {
        this.member = member;
        this.chatRoom = chatRoom;
    }

    protected ChatMember() {
    }

    public static ChatMember create(Member member, Chatroom chatRoom) {
        return ChatMember.builder()
            .member(member)
            .chatRoom(chatRoom)
            .build();
    }

    public static ChatMemberBuilder builder() {
        return new ChatMemberBuilder();
    }

    public Long getChatMemberId() {
        return this.ChatMemberId;
    }

    public Member getMember() {
        return this.member;
    }

    public Chatroom getChatRoom() {
        return this.chatRoom;
    }

    public static class ChatMemberBuilder {
        private Member member;
        private Chatroom chatRoom;

        ChatMemberBuilder() {
        }

        public ChatMemberBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public ChatMemberBuilder chatRoom(Chatroom chatRoom) {
            this.chatRoom = chatRoom;
            return this;
        }

        public ChatMember build() {
            return new ChatMember(this.member, this.chatRoom);
        }

        public String toString() {
            return "ChatMember.ChatMemberBuilder(member=" + this.member + ", chatRoom=" + this.chatRoom + ")";
        }
    }
}
