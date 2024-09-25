package team4.footwithme.chat.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.member.domain.Member;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chat SET is_deleted = true WHERE chat_id = ?")
public class Chat extends BaseEntity {

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

    @Builder
    private Chat(Chatroom chatRoom, Member member, String text) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.text = text;
    }

    public static Chat create(Chatroom chatRoom, Member member, String text) {
        return Chat.builder()
            .chatRoom(chatRoom)
            .member(member)
            .text(text)
            .build();
    }

}
