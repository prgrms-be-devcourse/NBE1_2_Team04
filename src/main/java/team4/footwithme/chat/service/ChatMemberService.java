package team4.footwithme.chat.service;

import team4.footwithme.chat.service.request.ChatMemberServiceRequest;
import team4.footwithme.chat.service.response.ChatMemberResponse;
import team4.footwithme.member.domain.Member;
import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.team.domain.TeamMember;

import java.util.List;

public interface ChatMemberService {

    ChatMemberResponse joinChatMember(ChatMemberServiceRequest request);

    String joinChatTeam(List<TeamMember> teamMembers, Long chatroomId);

    String joinChatGame(List<Participant> gameMembers, Long chatroomId);

    String joinChatMembers(List<Member> members, Long chatroomId);

    ChatMemberResponse leaveChatMember(ChatMemberServiceRequest request);

    String leaveChatRoom(Long chatroomId);
}
