package team4.footwithme.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.chat.domain.Chat;
import team4.footwithme.chat.domain.ChatMember;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.repository.ChatMemberRepository;
import team4.footwithme.chat.repository.ChatRepository;
import team4.footwithme.chat.repository.ChatroomRepository;
import team4.footwithme.chat.service.request.ChatMemberServiceRequest;
import team4.footwithme.chat.service.response.ChatMemberResponse;
import team4.footwithme.global.exception.ExceptionMessage;
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
    private final ChatRepository chatRepository;

    private final RedisPublisher redisPublisher;

    /**
     * 개인 채팅방 초대
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public ChatMemberResponse joinChatMember(ChatMemberServiceRequest request) {
        Member member = getMember(request.memberId());
        Chatroom chatroom = getChatroom(request.chatroomId());

        checkMemberNotInChatroom(member, chatroom);

        ChatMember chatMember = chatMemberRepository.save(ChatMember.create(member, chatroom));

        Chat chat = Chat.createEnterChat(chatroom, member, member.getName() + "님이 입장했습니다.");
        chatRepository.save(chat);

        redisPublisher.publish(chat);

        return new ChatMemberResponse(chatMember);
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

        return joinChatMembers(members, chatroomId);
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

        return joinChatMembers(members, chatroomId);
    }

    /**
     * 단체로 채팅방 초대
     *
     * @param members
     * @param chatroomId
     * @return
     */
    @Override
    @Transactional
    public String joinChatMembers(List<Member> members, Long chatroomId){
        Chatroom chatroom = getChatroom(chatroomId);

        List<Long> oldMembersId = chatMemberRepository.findByChatroom(chatroom)
                .stream().map(chatMember -> chatMember.getMember().getMemberId()).collect(Collectors.toList());

        List<ChatMember> chatMembers = new ArrayList<>();

        for (Member member : members) {
            if(oldMembersId.contains(member.getMemberId())) {
                continue;
            }
            chatMembers.add(ChatMember.create(member, chatroom));
        }

        chatMemberRepository.saveAll(chatMembers);

        Chat chat = Chat.createGroupEnterChat(chatroom, chatMembers);
        chatRepository.save(chat);

        redisPublisher.publish(chat);

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
    public ChatMemberResponse leaveChatMember(ChatMemberServiceRequest request) {
        Member member = getMember(request.memberId());
        Chatroom chatroom = getChatroom(request.chatroomId());

        checkMemberInChatroom(member, chatroom);

        chatMemberRepository.deleteByMemberAndChatRoom(member, chatroom);

        Chat chat = Chat.createQuitChat(chatroom, member, member.getName());
        chatRepository.save(chat);

        redisPublisher.publish(chat);

        return new ChatMemberResponse(chatroom.getChatroomId(), member.getMemberId());
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
        Chatroom chatroom = getChatroom(chatroomId);

        chatMemberRepository.deleteByChatRoom(chatroom);
        return "Successfully leaving the chatroom";
    }

    private void checkMemberInChatroom(Member member, Chatroom chatroom) {
        if(!chatMemberRepository.existByMemberAndChatroom(member, chatroom)) {
            throw new IllegalArgumentException(ExceptionMessage.MEMBER_NOT_IN_CHATROOM.getText());
        }
    }

    private void checkMemberNotInChatroom(Member member, Chatroom chatroom) {
        if(chatMemberRepository.existByMemberAndChatroom(member, chatroom)) {
            throw new IllegalArgumentException(ExceptionMessage.MEMBER_IN_CHATROOM.getText());
        }
    }

    private Member getMember(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.MEMBER_NOT_FOUND.getText()));
    }

    private Chatroom getChatroom(Long chatroomId) {
        return chatroomRepository.findByChatroomId(chatroomId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.CHATROOM_NOT_FOUND.getText()));
    }
}
