package team4.footwithme.chat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;
import team4.footwithme.member.domain.Member;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private ChatMember(Member member, Chatroom chatRoom) {
        this.member = member;
        this.chatRoom = chatRoom;
    }

    public static ChatMember create(Member member, Chatroom chatRoom) {
        return ChatMember.builder()
            .member(member)
            .chatRoom(chatRoom)
            .build();
    }
}
