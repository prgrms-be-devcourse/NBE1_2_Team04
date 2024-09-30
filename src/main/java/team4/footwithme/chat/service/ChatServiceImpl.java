package team4.footwithme.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.chat.domain.Chat;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.repository.ChatMemberRepository;
import team4.footwithme.chat.repository.ChatRepository;
import team4.footwithme.chat.repository.ChatroomRepository;
import team4.footwithme.chat.repository.RedisChatroomRepository;
import team4.footwithme.chat.service.request.ChatServiceRequest;
import team4.footwithme.chat.service.response.ChatResponse;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;
    private final MemberRepository memberRepository;
    private final ChatMemberRepository chatMemberRepository;

    private final RedisChatroomRepository redisChatroomRepository;
    private final RedisPublisher redisPublisher;

    /**
     * 메세지 보내기
     *
     * @param request
     * @param email
     */
    @Override
    @Transactional
    public void sendMessage(ChatServiceRequest request, String email) {
        // 채팅방에 참여한 멤버인지 검증
        Member member = checkMember(email);
        Chatroom chatroom = checkChatroom(request.ChatroomId());
        checkMemberInChatroom(member, chatroom);

        // 메시지를 데이터베이스에 저장
        Chat chat = Chat.create(chatroom, member, request.message());
        chatRepository.save(chat);

        // Redis에 메시지 발행
        ChannelTopic topic = redisChatroomRepository.getTopic(chatroom.getChatroomId().toString());
        redisPublisher.publish(topic, chat);
    }

    /**
     * 특정 채팅방에 과거 메세지 조회
     * 페이지네이션
     * 채팅방 메세지를 보려면 채팅방에 소속된 멤버여야 함
     */
    @Transactional(readOnly = true)
    public Slice<ChatResponse> getChatList(Long chatroomId, PageRequest pageRequest, String email) {
        // 채팅방에 참여한 멤버인지 검증
        Member member = checkMember(email);
        Chatroom chatroom = checkChatroom(chatroomId);
        checkMemberInChatroom(member, chatroom);

        return chatRepository.findChatByChatroom(chatroom, pageRequest);
    }

    /**
     * 채팅방에 소속된 멤버인지 검증하는 메소드
     */
    public void checkMemberInChatroom(Member member, Chatroom chatroom) {
        if (!chatMemberRepository.existsByMemberAndChatRoom(member, chatroom)) {
            throw new IllegalArgumentException("Invalid member");
        }
    }

    public Member checkMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    public Chatroom checkChatroom(Long chatroomId) {
        return chatroomRepository.findByChatroomId(chatroomId).orElseThrow(() -> new IllegalArgumentException("Chatroom not found"));
    }
}
