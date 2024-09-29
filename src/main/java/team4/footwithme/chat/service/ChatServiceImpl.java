package team4.footwithme.chat.service;

import lombok.RequiredArgsConstructor;
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
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;
    private final MemberRepository memberRepository;
    private final ChatMemberRepository chatMemberRepository;

    private final RedisChatroomRepository redisChatroomRepository;
    private final RedisPublisher redisPublisher;


    /**
     * 메세지 보내기
     * @param request
     * @param email
     */
    @Override
    @Transactional
    public void sendMessage(ChatServiceRequest request, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("Member not found"));
        Chatroom chatroom = chatroomRepository.findByChatroomId(request.ChatroomId()).orElseThrow(()-> new IllegalArgumentException("Chatroom not found"));

        // 채팅방에 참여한 멤버인지 검증
        if(!checkMember(member, chatroom)) {
            throw new IllegalArgumentException("Invalid member");
        }

        Chat chat = Chat.create(chatroom, member, request.message());

        // 메시지를 데이터베이스에 저장
        chatRepository.save(chat);

        //ChatResponse chatResponse = new ChatResponse(chat);

        // Redis에 메시지 발행
        ChannelTopic topic = redisChatroomRepository.getTopic(chatroom.getChatroomId().toString());
        redisPublisher.publish(topic, chat);
    }

    /**
     * 특정 채팅방에 과거 메세지 조회
     * 페이지네이션
     * 채팅방 메세지를 보려면 채팅방에 소속된 멤버여야 함
     */

    /**
     * 채팅방에 소속된 멤버인지 검증하는 메소드
     */
    public boolean checkMember(Member member, Chatroom chatroom) {
        return chatMemberRepository.existsByMemberAndChatRoom(member, chatroom);
    }
}
