package team4.footwithme.chat.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.chat.domain.Chat;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.repository.ChatMemberRepository;
import team4.footwithme.chat.repository.ChatRepository;
import team4.footwithme.chat.repository.ChatroomRepository;
import team4.footwithme.chat.service.request.ChatServiceRequest;
import team4.footwithme.chat.service.request.ChatUpdateServiceRequest;
import team4.footwithme.chat.service.response.ChatResponse;
import team4.footwithme.global.exception.ExceptionMessage;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.jwt.JwtTokenUtil;
import team4.footwithme.member.repository.MemberRepository;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;
    private final MemberRepository memberRepository;
    private final ChatMemberRepository chatMemberRepository;

    private final RedisPublisher redisPublisher;
    private final JwtTokenUtil jwtTokenUtil;

    public ChatServiceImpl(ChatRepository chatRepository, ChatroomRepository chatroomRepository, MemberRepository memberRepository, ChatMemberRepository chatMemberRepository, RedisPublisher redisPublisher, JwtTokenUtil jwtTokenUtil) {
        this.chatRepository = chatRepository;
        this.chatroomRepository = chatroomRepository;
        this.memberRepository = memberRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.redisPublisher = redisPublisher;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 메세지 보내기
     *
     * @param request
     */
    @Override
    @Transactional
    public void sendMessage(ChatServiceRequest request, String token) {
        // 채팅방에 참여한 멤버인지 검증
        String accessToken = token.substring(7).trim();
        String email = jwtTokenUtil.getEmailFromToken(accessToken);

        Member member = getMember(email);
        Chatroom chatroom = getChatroom(request.ChatroomId());
        checkMemberInChatroom(member, chatroom);

        // 메시지를 데이터베이스에 저장
        Chat chat = Chat.createTalkChat(chatroom, member, request.message());
        chatRepository.save(chat);

        // Redis에 메시지 발행
        redisPublisher.publish(chat);
    }

    /**
     * 특정 채팅방에 과거 메세지 조회
     * 페이지네이션
     * 채팅방 메세지를 보려면 채팅방에 소속된 멤버여야 함
     */
    @Override
    @Transactional(readOnly = true)
    public Slice<ChatResponse> getChatList(Long chatroomId, PageRequest pageRequest, Member member) {
        Chatroom chatroom = getChatroom(chatroomId);
        checkMemberInChatroom(member, chatroom);

        return chatRepository.findChatByChatroom(chatroom, pageRequest);
    }

    /**
     * 채팅 수정
     */
    @Transactional
    @Override
    public ChatResponse updateChat(ChatUpdateServiceRequest request, Member member, Long chatId) {
        Chat chat = getChat(chatId);

        checkChatByMember(member, chat);

        chat.updateMessage(request.message());

        return new ChatResponse(chat);
    }

    /**
     * 채팅 삭제
     */
    @Transactional
    @Override
    public Long deleteChat(Member member, Long chatId) {
        Chat chat = getChat(chatId);

        checkChatByMember(member, chat);

        chatRepository.delete(chat);

        return chatId;
    }

    /**
     * 채팅방에 소속된 멤버인지 검증하는 메소드
     */
    private void checkMemberInChatroom(Member member, Chatroom chatroom) {
        if (!chatMemberRepository.existByMemberAndChatroom(member, chatroom)) {
            throw new IllegalArgumentException(ExceptionMessage.MEMBER_NOT_IN_CHATROOM.getText());
        }
    }

    /**
     * 채팅을 작성한 멤버인지 검증하는 메소드
     *
     * @param member
     * @param chat
     */
    private void checkChatByMember(Member member, Chat chat) {
        if (!chat.getMember().equals(member)) {
            throw new IllegalArgumentException(ExceptionMessage.UNAUTHORIZED_MESSAGE_EDIT.getText());
        }
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.MEMBER_NOT_FOUND.getText()));
    }

    private Chatroom getChatroom(Long chatroomId) {
        return chatroomRepository.findByChatroomId(chatroomId).orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.CHATROOM_NOT_FOUND.getText()));
    }

    private Chat getChat(Long chatId) {
        return chatRepository.findByChatId(chatId).orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.CHAT_NOT_FOUND.getText()));
    }
}
