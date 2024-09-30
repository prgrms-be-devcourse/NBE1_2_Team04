package team4.footwithme.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.repository.ChatroomRepository;
import team4.footwithme.chat.repository.RedisChatroomRepository;
import team4.footwithme.chat.service.request.ChatroomServiceRequest;
import team4.footwithme.chat.service.response.ChatroomResponse;

@RequiredArgsConstructor
@Service
public class ChatroomServiceImpl implements ChatroomService {
    private final ChatroomRepository chatroomRepository;

    private final RedisChatroomRepository redisChatroomRepository;

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
        System.out.println(request.name());
        System.out.println(chatroom.getName());

        // redis Hash에 저장
        redisChatroomRepository.createChatRoom(chatroom);
        redisChatroomRepository.enterChatRoom(chatroom.getChatroomId().toString());

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
    public String deleteChatroom(Long chatroomId) {
        // 채팅방 인원 삭제는 ChatMemberRepository에 있음
        chatroomRepository.deleteById(chatroomId);
        redisChatroomRepository.leaveChatRoom(chatroomId.toString());
        return "Chatroom deleted";
    }
}
