package team4.footwithme.chat.service.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import team4.footwithme.chat.service.ChatMemberService;
import team4.footwithme.chat.service.ChatroomService;
import team4.footwithme.chat.service.request.ChatroomServiceRequest;

@Component
public class ChatEventListener {

    private final ChatroomService chatroomService;
    private final ChatMemberService chatMemberService;

    public ChatEventListener(ChatroomService chatroomService, ChatMemberService chatMemberService) {
        this.chatroomService = chatroomService;
        this.chatMemberService = chatMemberService;
    }

    @EventListener
    public void onTeamPublishedEvent(TeamPublishedEvent event) {
        chatroomService.createTeamChatroom(new ChatroomServiceRequest(event.teamId() + " 팀 채팅방"), event.teamId());
    }

    @EventListener
    public void onTeamMemberJoinEvent(TeamMemberJoinEvent event) {
        chatMemberService.joinTeamChatMember(event.member(), event.teamId());
    }

    @EventListener
    public void onTeamMemberLeaveEvent(TeamMemberLeaveEvent event) {
        chatMemberService.leaveTeamChatMember(event.member(), event.teamId());
    }

    @EventListener
    public void onTeamMembersJoinEvent(TeamMembersJoinEvent event) {
        chatMemberService.joinChatTeam(event.members(), event.teamId());
    }

    @EventListener
    public void onTeamDeletedEvent(TeamDeletedEvent event) {
        chatMemberService.leaveTeamChatRoom(event.teamId());
        chatroomService.deleteTeamChatroom(event.teamId());
    }

    @EventListener
    public void onReservationPublishedEvent(ReservationPublishedEvent event) {
        chatroomService.createReservationChatroom(new ChatroomServiceRequest(event.reservationId() + " 예약 채팅방"), event.reservationId());
    }

    @EventListener
    public void onReservationMemberJoinEvent(ReservationMemberJoinEvent event) {
        chatMemberService.joinReservationChatMember(event.member(), event.reservationId());
    }

    @EventListener
    public void onReservationMemberLeaveEvent(ReservationMemberLeaveEvent event) {
        chatMemberService.leaveReservationChatMember(event.member(), event.reservationId());
    }

    @EventListener
    public void onReservationMembersJoinEvent(ReservationMembersJoinEvent event) {
        chatMemberService.joinChatGame(event.members(), event.reservationId());
    }

    @EventListener
    public void onReservationDeletedEvent(ReservationDeletedEvent event) {
        chatMemberService.leaveReservationChatRoom(event.reservationId());
        chatroomService.deleteReservationChatroom(event.reservationId());
    }


}
