package team4.footwithme.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.domain.ReservationChatroom;
import team4.footwithme.chat.domain.TeamChatroom;
import team4.footwithme.chat.repository.ChatroomRepository;
import team4.footwithme.chat.repository.RedisChatroomRepository;
import team4.footwithme.chat.service.request.ChatroomServiceRequest;
import team4.footwithme.chat.service.response.ChatroomResponse;
import team4.footwithme.global.exception.ExceptionMessage;

@Service
public class ChatroomServiceImpl implements ChatroomService {
    private final ChatroomRepository chatroomRepository;

    private final RedisChatroomRepository redisChatroomRepository;

    public ChatroomServiceImpl(ChatroomRepository chatroomRepository, RedisChatroomRepository redisChatroomRepository) {
        this.chatroomRepository = chatroomRepository;
        this.redisChatroomRepository = redisChatroomRepository;
    }

    /**
     * 채팅방 생성
     * 채팅방을 만든 사람은 자동으로 채팅방 초대
     *
     * @param request 채팅방 이름 ( 팀 or 예약 만들어질 때 이름 넣어서 보내주기 )
     * @return
     */
    @Transactional
    @Override
    public ChatroomResponse createChatroom(ChatroomServiceRequest request) {

        Chatroom chatroom = chatroomRepository.save(Chatroom.create(request.name()));

        // redis Hash에 저장
        redisChatroomRepository.createChatRoom(chatroom);

        return new ChatroomResponse(chatroom);
    }

    @Transactional
    @Override
    public ChatroomResponse createTeamChatroom(ChatroomServiceRequest request, Long teamId) {

        Chatroom chatroom = chatroomRepository.save(TeamChatroom.create(request.name(), teamId));

        // redis Hash에 저장
        redisChatroomRepository.createChatRoom(chatroom);

        return new ChatroomResponse(chatroom);
    }

    @Transactional
    @Override
    public ChatroomResponse createReservationChatroom(ChatroomServiceRequest request, Long reservationId) {

        Chatroom chatroom = chatroomRepository.save(ReservationChatroom.create(request.name(), reservationId));

        // redis Hash에 저장
        redisChatroomRepository.createChatRoom(chatroom);

        return new ChatroomResponse(chatroom);
    }

    /**
     * 채팅방 삭제 ( 채팅방 인원 삭제도 같이 진행해야 함 )
     *
     * @param chatroomId
     * @return
     */
    @Transactional
    @Override
    public Long deleteChatroomByChatroomId(Long chatroomId) {
        Chatroom chatroom = getChatroomByChatroomId(chatroomId);

        return deleteChatroom(chatroom);
    }

    @Transactional
    @Override
    public Long deleteTeamChatroom(Long teamId) {
        Chatroom chatroom = getChatroomByTeamId(teamId);

        return deleteChatroom(chatroom);
    }

    @Transactional
    @Override
    public Long deleteReservationChatroom(Long reservationId) {
        Chatroom chatroom = getChatroomByReservationId(reservationId);

        return deleteChatroom(chatroom);
    }

    /**
     * 채팅방 수정
     */
    @Transactional
    @Override
    public ChatroomResponse updateChatroom(Long chatroomId, ChatroomServiceRequest request) {
        Chatroom chatroom = getChatroomByChatroomId(chatroomId);

        chatroom.updateName(request.name());

        return new ChatroomResponse(chatroom);
    }

    private Long deleteChatroom(Chatroom chatroom) {
        redisChatroomRepository.deleteChatroomFromRedis(chatroom.getChatroomId());

        chatroomRepository.deleteById(chatroom.getChatroomId());
        return chatroom.getChatroomId();
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
