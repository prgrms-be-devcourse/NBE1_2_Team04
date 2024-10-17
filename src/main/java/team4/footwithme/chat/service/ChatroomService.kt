package team4.footwithme.chat.service;

import team4.footwithme.chat.service.request.ChatroomServiceRequest;
import team4.footwithme.chat.service.response.ChatroomResponse;

public interface ChatroomService {
    ChatroomResponse createChatroom(ChatroomServiceRequest request);

    Long deleteChatroomByChatroomId(Long chatroomId);

    ChatroomResponse updateChatroom(Long chatroomId, ChatroomServiceRequest request);

    ChatroomResponse createReservationChatroom(ChatroomServiceRequest request, Long reservationId);

    ChatroomResponse createTeamChatroom(ChatroomServiceRequest request, Long teamId);

    Long deleteTeamChatroom(Long teamId);

    Long deleteReservationChatroom(Long reservationId);
}
