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

    String joinChatTeam(List<TeamMember> teamMembers, Long teamId);

    String joinChatGame(List<Participant> gameMembers, Long reservationId);

    String joinChatMembers(List<Member> members, Chatroom chatroom);

    ChatMemberResponse leaveChatMember(ChatMemberServiceRequest request);

    String leaveChatRoom(Long chatroomId);

    ChatMemberResponse joinTeamChatMember(Member member, Long teamId);

    ChatMemberResponse joinReservationChatMember(Member member, Long reservationId);

    String leaveTeamChatRoom(Long teamId);

    String leaveReservationChatRoom(Long reservationId);

    ChatMemberResponse leaveTeamChatMember(Member member, Long teamId);

    ChatMemberResponse leaveReservationChatMember(Member member, Long reservationId);
}
