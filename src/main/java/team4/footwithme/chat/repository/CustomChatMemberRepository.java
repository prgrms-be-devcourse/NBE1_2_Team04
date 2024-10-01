package team4.footwithme.chat.repository;

import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.member.domain.Member;

public interface CustomChatMemberRepository {
    Boolean existByMemberAndChatroom(Member member, Chatroom chatroom);
}
