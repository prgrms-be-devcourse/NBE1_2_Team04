package team4.footwithme.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.chat.domain.ChatMember;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.repository.ChatMemberRepository;
import team4.footwithme.chat.repository.ChatroomRepository;
import team4.footwithme.chat.service.request.ChatMemberServiceRequest;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;
import team4.footwithme.resevation.domain.Participant;
import team4.footwithme.team.domain.TeamMember;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatMemberServiceImpl implements ChatMemberService {
    private final ChatMemberRepository chatMemberRepository;
    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;

    /**
     * 개인 채팅방 초대
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public String joinChatMember(ChatMemberServiceRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId())
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Chatroom chatroom = chatroomRepository.findByChatroomId(request.chatroomId())
            .orElseThrow(() -> new IllegalArgumentException("Chatroom not found"));

        chatMemberRepository.save(ChatMember.create(member, chatroom));

        return "Successfully joined chat";
    }

    /**
     * 팀원 채팅방 초대
     *
     * @param teamMembers
     * @param chatroomId
     * @return
     */
    @Override
    @Transactional
    public String joinChatTeam(List<TeamMember> teamMembers, Long chatroomId) {
        List<Member> members = teamMembers.stream().map(TeamMember::getMember).collect(Collectors.toList());

        Chatroom chatroom = chatroomRepository.findByChatroomId(chatroomId)
            .orElseThrow(() -> new IllegalArgumentException("Chatroom not found"));

        List<ChatMember> chatMembers = new ArrayList<>();

        for (Member member : members) {
            chatMembers.add(ChatMember.create(member, chatroom));
        }

        chatMemberRepository.saveAll(chatMembers);

        return "Successfully joined chat";
    }

    /**
     * 게임 참여 인원 채팅방 초대
     *
     * @param gameMembers
     * @param chatroomId
     * @return
     */
    @Override
    @Transactional
    public String joinChatGame(List<Participant> gameMembers, Long chatroomId) {
        List<Member> members = gameMembers.stream().map(Participant::getMember).collect(Collectors.toList());

        Chatroom chatroom = chatroomRepository.findByChatroomId(chatroomId)
            .orElseThrow(() -> new IllegalArgumentException("Chatroom not found"));

        List<ChatMember> chatMembers = new ArrayList<>();

        for (Member member : members) {
            chatMembers.add(ChatMember.create(member, chatroom));
        }

        chatMemberRepository.saveAll(chatMembers);

        return "Successfully joined chat";
    }

    /**
     * 개인 인원 채팅방 나가기
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public String leaveChatMember(ChatMemberServiceRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId())
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Chatroom chatroom = chatroomRepository.findByChatroomId(request.chatroomId())
            .orElseThrow(() -> new IllegalArgumentException("Chatroom not found"));

        chatMemberRepository.deleteByMemberAndChatRoom(member, chatroom);
        return "Successfully left chat";
    }

    /**
     * 채팅방이 삭제됐을 때 채팅방에 참가한 사람들 연관관계 삭제
     *
     * @param chatroomId 채팅방 ID
     * @return
     */
    @Override
    @Transactional
    public String leaveChatRoom(Long chatroomId) {
        Chatroom chatroom = chatroomRepository.findByChatroomId(chatroomId)
            .orElseThrow(() -> new IllegalArgumentException("Chatroom not found"));

        chatMemberRepository.deleteByChatRoom(chatroom);
        return "Successfully leaving the chatroom";
    }
}
