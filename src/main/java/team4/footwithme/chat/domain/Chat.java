package team4.footwithme.chat.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.member.domain.Member;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@SQLDelete(sql = "UPDATE chat SET is_deleted = 'true' WHERE chat_id = ?")
public class Chat extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2501100146410218751L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    private String text;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    private Chat(Chatroom chatRoom, Member member, ChatType chatType, String text) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.chatType = chatType;
        this.text = text;
    }

    protected Chat() {
    }

    public static Chat create(Chatroom chatRoom, Member member, ChatType chatType, String text) {
        return Chat.builder()
            .chatRoom(chatRoom)
            .member(member)
            .chatType(chatType)
            .text(text)
            .build();
    }

    public static Chat createTalkChat(Chatroom chatRoom, Member member, String text) {
        return Chat.builder()
            .chatRoom(chatRoom)
            .member(member)
            .chatType(ChatType.TALK)
            .text(text)
            .build();
    }

    public static Chat createEnterChat(Chatroom chatRoom, Member member) {
        return Chat.builder()
            .chatRoom(chatRoom)
            .member(member)
            .chatType(ChatType.ENTER)
            .text(member.getName() + "님이 입장했습니다.")
            .build();
    }

    public static Chat createGroupEnterChat(Chatroom chatRoom, List<ChatMember> chatMembers) {
        return Chat.builder()
            .chatRoom(chatRoom)
            .member(chatMembers.get(0).getMember())
            .chatType(ChatType.ENTER)
            .text(chatMembers.get(0).getMember().getName() + "님 등 " + chatMembers.size() + "명이 입장했습니다.")
            .build();
    }

    public static Chat createQuitChat(Chatroom chatRoom, Member member) {
        return Chat.builder()
            .chatRoom(chatRoom)
            .member(member)
            .chatType(ChatType.QUIT)
            .text(member.getName() + "님이 채팅방을 떠났습니다.")
            .build();
    }

    public static ChatBuilder builder() {
        return new ChatBuilder();
    }

    public void updateMessage(String message) {
        this.text = message;
    }

    public Long getChatId() {
        return this.chatId;
    }

    public Chatroom getChatRoom() {
        return this.chatRoom;
    }

    public Member getMember() {
        return this.member;
    }

    public @NotNull String getText() {
        return this.text;
    }

    public @NotNull ChatType getChatType() {
        return this.chatType;
    }

    public static class ChatBuilder {
        private Chatroom chatRoom;
        private Member member;
        private ChatType chatType;
        private String text;

        ChatBuilder() {
        }

        public ChatBuilder chatRoom(Chatroom chatRoom) {
            this.chatRoom = chatRoom;
            return this;
        }

        public ChatBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public ChatBuilder chatType(ChatType chatType) {
            this.chatType = chatType;
            return this;
        }

        public ChatBuilder text(String text) {
            this.text = text;
            return this;
        }

        public Chat build() {
            return new Chat(this.chatRoom, this.member, this.chatType, this.text);
        }

        public String toString() {
            return "Chat.ChatBuilder(chatRoom=" + this.chatRoom + ", member=" + this.member + ", chatType=" + this.chatType + ", text=" + this.text + ")";
        }
    }
}
