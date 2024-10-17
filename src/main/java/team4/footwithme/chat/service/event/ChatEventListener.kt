package team4.footwithme.chat.service.event

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import team4.footwithme.chat.service.ChatMemberService
import team4.footwithme.chat.service.ChatroomService
import team4.footwithme.chat.service.request.ChatroomServiceRequest

@Component
class ChatEventListener(
    private val chatroomService: ChatroomService,
    private val chatMemberService: ChatMemberService
) {
    @EventListener
    fun onTeamPublishedEvent(event: TeamPublishedEvent) {
        chatroomService.createTeamChatroom(ChatroomServiceRequest(event.teamId.toString() + " 팀 채팅방"), event.teamId)
    }

    @EventListener
    fun onTeamMemberJoinEvent(event: TeamMemberJoinEvent) {
        chatMemberService.joinTeamChatMember(event.member, event.teamId)
    }

    @EventListener
    fun onTeamMemberLeaveEvent(event: TeamMemberLeaveEvent) {
        chatMemberService.leaveTeamChatMember(event.member, event.teamId)
    }

    @EventListener
    fun onTeamMembersJoinEvent(event: TeamMembersJoinEvent) {
        chatMemberService.joinChatTeam(event.members, event.teamId)
    }

    @EventListener
    fun onTeamDeletedEvent(event: TeamDeletedEvent) {
        chatMemberService.leaveTeamChatRoom(event.teamId)
        chatroomService.deleteTeamChatroom(event.teamId)
    }

    @EventListener
    fun onReservationPublishedEvent(event: ReservationPublishedEvent) {
        chatroomService.createReservationChatroom(
            ChatroomServiceRequest(event.reservationId.toString() + " 예약 채팅방"),
            event.reservationId
        )
    }

    @EventListener
    fun onReservationMemberJoinEvent(event: ReservationMemberJoinEvent) {
        chatMemberService.joinReservationChatMember(event.member, event.reservationId)
    }

    @EventListener
    fun onReservationMemberLeaveEvent(event: ReservationMemberLeaveEvent) {
        chatMemberService.leaveReservationChatMember(event.member, event.reservationId)
    }

    @EventListener
    fun onReservationMembersJoinEvent(event: ReservationMembersJoinEvent) {
        chatMemberService.joinChatGame(event.members, event.reservationId)
    }

    @EventListener
    fun onReservationDeletedEvent(event: ReservationDeletedEvent) {
        chatMemberService.leaveReservationChatRoom(event.reservationId)
        chatroomService.deleteReservationChatroom(event.reservationId)
    }
}
