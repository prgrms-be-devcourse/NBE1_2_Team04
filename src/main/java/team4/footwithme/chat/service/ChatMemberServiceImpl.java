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
        Chatroom chatroom = getChatroomByChatroomId(request.chatroomId());

        return addChatMember(member, chatroom);
    }

    @Override
    @Transactional
    public void joinTeamChatMember(Member member, Long teamId) {
        Chatroom chatroom = getChatroomByTeamId(teamId);

        addChatMember(member, chatroom);
    }

    @Override
    @Transactional
    public void joinReservationChatMember(Member member, Long reservationId) {
        Chatroom chatroom = getChatroomByReservationId(reservationId);

        addChatMember(member, chatroom);
    }

    private ChatMemberResponse addChatMember(Member member, Chatroom chatroom){
        checkMemberNotInChatroom(member, chatroom);

        ChatMember chatMember = chatMemberRepository.save(ChatMember.create(member, chatroom));

        Chat chat = Chat.createEnterChat(chatroom, member);
        chatRepository.save(chat);

        redisPublisher.publish(chat);

        return new ChatMemberResponse(chatMember);
    }

    /**
     * 팀원 채팅방 초대
     *
     * @param teamMembers
     */
    @Override
    @Transactional
    public void joinChatTeam(List<TeamMember> teamMembers, Long teamId) {
        Chatroom chatroom = getChatroomByTeamId(teamId);
        List<Member> members = teamMembers.stream().map(TeamMember::getMember).collect(Collectors.toList());

        joinChatMembers(members, chatroom);
    }

    /**
     * 게임 참여 인원 채팅방 초대
     *
     * @param gameMembers
     */
    @Override
    @Transactional
    public void joinChatGame(List<Participant> gameMembers, Long reservationId) {
        Chatroom chatroom = getChatroomByReservationId(reservationId);

        List<Member> members = gameMembers.stream().map(Participant::getMember).collect(Collectors.toList());

        joinChatMembers(members, chatroom);
    }

    /**
     * 단체로 채팅방 초대
     *
     * @param members
     */
    @Override
    @Transactional
    public void joinChatMembers(List<Member> members, Chatroom chatroom){

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
        Chatroom chatroom = getChatroomByChatroomId(request.chatroomId());

        return removeChatMember(member, chatroom);
    }

    @Override
    @Transactional
    public void leaveTeamChatMember(Member member, Long teamId) {
        Chatroom chatroom = getChatroomByTeamId(teamId);

        removeChatMember(member, chatroom);
    }

    @Override
    @Transactional
    public void leaveReservationChatMember(Member member, Long reservationId) {
        Chatroom chatroom = getChatroomByReservationId(reservationId);

        removeChatMember(member, chatroom);
    }

    private ChatMemberResponse removeChatMember(Member member, Chatroom chatroom) {
        checkMemberInChatroom(member, chatroom);

        chatMemberRepository.deleteByMemberAndChatRoom(member, chatroom);

        Chat chat = Chat.createQuitChat(chatroom, member);
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
    public void leaveChatRoom(Long chatroomId) {
        Chatroom chatroom = getChatroomByChatroomId(chatroomId);

        chatMemberRepository.updateIsDeletedForChatRoom(chatroom);
    }

    @Override
    @Transactional
    public void leaveTeamChatRoom(Long teamId) {
        Chatroom chatroom = getChatroomByTeamId(teamId);

        chatMemberRepository.updateIsDeletedForChatRoom(chatroom);
    }

    @Override
    @Transactional
    public void leaveReservationChatRoom(Long reservationId) {
        Chatroom chatroom = getChatroomByReservationId(reservationId);

        chatMemberRepository.updateIsDeletedForChatRoom(chatroom);
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

    private Chatroom getChatroomByChatroomId(Long chatroomId) {
        return chatroomRepository.findByChatroomId(chatroomId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.CHATROOM_NOT_FOUND.getText()));
    }

    private Chatroom getChatroomByTeamId(Long teamId) {
        return chatroomRepository.findByTeamId(teamId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.CHATROOM_NOT_FOUND.getText()));
    }

    private Chatroom getChatroomByReservationId(Long reservationId) {
        return chatroomRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.CHATROOM_NOT_FOUND.getText()));
    }
}
