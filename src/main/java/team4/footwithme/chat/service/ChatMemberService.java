package team4.footwithme.chat.service;

import team4.footwithme.chat.service.request.ChatMemberServiceRequest;
import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.team.domain.TeamMember;

import java.util.List;

public interface ChatMemberService {

    String joinChatMember(ChatMemberServiceRequest request);

    String joinChatTeam(List<TeamMember> teamMembers, Long chatroomId);

    String joinChatGame(List<Participant> gameMembers, Long chatroomId);

    String leaveChatMember(ChatMemberServiceRequest request);

    String leaveChatRoom(Long chatroomId);
}
