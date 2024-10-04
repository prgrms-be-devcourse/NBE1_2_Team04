package team4.footwithme.chat.service;

import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.service.request.ChatMemberServiceRequest;
import team4.footwithme.chat.service.response.ChatMemberResponse;
import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.team.domain.TeamMember;

import java.util.List;

public interface ChatMemberService {

    ChatMemberResponse joinChatMember(ChatMemberServiceRequest request);

    void joinChatTeam(List<TeamMember> teamMembers, Long teamId);

    void joinChatGame(List<Participant> gameMembers, Long reservationId);

    void joinChatMembers(List<Member> members, Chatroom chatroom);

    ChatMemberResponse leaveChatMember(ChatMemberServiceRequest request);

    void leaveChatRoom(Long chatroomId);

    void joinTeamChatMember(Member member, Long teamId);

    void joinReservationChatMember(Member member, Long reservationId);

    void leaveTeamChatRoom(Long teamId);

    void leaveReservationChatRoom(Long reservationId);

    void leaveTeamChatMember(Member member, Long teamId);

    void leaveReservationChatMember(Member member, Long reservationId);
}
