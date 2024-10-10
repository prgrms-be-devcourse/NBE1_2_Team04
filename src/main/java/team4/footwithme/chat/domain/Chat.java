package team4.footwithme.chat.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.member.domain.Member;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private Chat(Chatroom chatRoom, Member member, ChatType chatType, String text) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.chatType = chatType;
        this.text = text;
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

    public void updateMessage(String message) {
        this.text = message;
    }

}
