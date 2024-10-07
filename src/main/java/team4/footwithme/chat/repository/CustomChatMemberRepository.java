package team4.footwithme.chat.repository;

import team4.footwithme.chat.domain.ChatMember;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.member.domain.Member;

import java.util.List;

public interface CustomChatMemberRepository {
    Boolean existByMemberAndChatroom(Member member, Chatroom chatroom);

    List<ChatMember> findByChatroom(Chatroom chatroom);
}
