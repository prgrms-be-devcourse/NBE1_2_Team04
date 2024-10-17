package team4.footwithme.chat.service

import team4.footwithme.chat.domain.Chatroom
import team4.footwithme.chat.service.request.ChatMemberServiceRequest
import team4.footwithme.chat.service.response.ChatMemberResponse
import team4.footwithme.member.domain.Member
import team4.footwithme.resevation.domain.Participant
import team4.footwithme.team.domain.TeamMember

interface ChatMemberService {
    fun joinChatMember(request: ChatMemberServiceRequest?): ChatMemberResponse

    fun joinChatTeam(teamMembers: List<TeamMember>, teamId: Long?)

    fun joinChatGame(gameMembers: List<Participant>, reservationId: Long?)

    fun joinChatMembers(members: List<Member?>, chatroom: Chatroom?)

    fun leaveChatMember(request: ChatMemberServiceRequest?): ChatMemberResponse

    fun leaveChatRoom(chatroomId: Long?)

    fun joinTeamChatMember(member: Member?, teamId: Long?)

    fun joinReservationChatMember(member: Member?, reservationId: Long?)

    fun leaveTeamChatRoom(teamId: Long?)

    fun leaveReservationChatRoom(reservationId: Long?)

    fun leaveTeamChatMember(member: Member?, teamId: Long?)

    fun leaveReservationChatMember(member: Member?, reservationId: Long?)
}
