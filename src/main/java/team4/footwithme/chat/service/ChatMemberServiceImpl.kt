package team4.footwithme.chat.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team4.footwithme.chat.domain.Chat
import team4.footwithme.chat.domain.ChatMember
import team4.footwithme.chat.domain.Chatroom
import team4.footwithme.chat.repository.ChatMemberRepository
import team4.footwithme.chat.repository.ChatRepository
import team4.footwithme.chat.repository.ChatroomRepository
import team4.footwithme.chat.service.request.ChatMemberServiceRequest
import team4.footwithme.chat.service.response.ChatMemberResponse
import team4.footwithme.global.exception.ExceptionMessage
import team4.footwithme.member.domain.Member
import team4.footwithme.member.repository.MemberRepository
import team4.footwithme.resevation.domain.*
import team4.footwithme.team.domain.TeamMember
import java.util.function.Supplier
import java.util.stream.Collectors

@Service
class ChatMemberServiceImpl(
    private val chatMemberRepository: ChatMemberRepository,
    private val memberRepository: MemberRepository,
    private val chatroomRepository: ChatroomRepository,
    private val chatRepository: ChatRepository,
    private val redisPublisher: RedisPublisher
) : ChatMemberService {
    /**
     * 개인 채팅방 초대
     *
     * @param request
     * @return
     */
    @Transactional
    override fun joinChatMember(request: ChatMemberServiceRequest?): ChatMemberResponse {
        val member = getMember(request!!.memberId)
        val chatroom = getChatroomByChatroomId(request.chatroomId)

        return addChatMember(member, chatroom)
    }

    @Transactional
    override fun joinTeamChatMember(member: Member?, teamId: Long?) {
        val chatroom = getChatroomByTeamId(teamId)

        addChatMember(member, chatroom)
    }

    @Transactional
    override fun joinReservationChatMember(member: Member?, reservationId: Long?) {
        val chatroom = getChatroomByReservationId(reservationId)

        addChatMember(member, chatroom)
    }

    private fun addChatMember(member: Member?, chatroom: Chatroom?): ChatMemberResponse {
        checkMemberNotInChatroom(member, chatroom)

        val chatMember = chatMemberRepository.save<ChatMember>(ChatMember.Companion.create(member, chatroom))

        val chat: Chat = Chat.Companion.createEnterChat(chatroom, member)
        chatRepository.save(chat)

        redisPublisher.publish(chat)

        return ChatMemberResponse(chatMember)
    }

    /**
     * 팀원 채팅방 초대
     *
     * @param teamMembers
     */
    @Transactional
    override fun joinChatTeam(teamMembers: List<TeamMember>, teamId: Long?) {
        val chatroom = getChatroomByTeamId(teamId)
        val members = teamMembers.stream().map { obj: TeamMember -> obj.member }
            .collect(Collectors.toList())

        joinChatMembers(members, chatroom)
    }

    /**
     * 게임 참여 인원 채팅방 초대
     *
     * @param gameMembers
     */
    @Transactional
    override fun joinChatGame(gameMembers: List<Participant>, reservationId: Long?) {
        val chatroom = getChatroomByReservationId(reservationId)

        val members = gameMembers.stream().map { obj: Participant -> obj.member }
            .collect(Collectors.toList())

        joinChatMembers(members, chatroom)
    }

    /**
     * 단체로 채팅방 초대
     *
     * @param members
     */
    @Transactional
    override fun joinChatMembers(members: List<Member?>, chatroom: Chatroom?) {
        val oldMembersId = chatMemberRepository.findByChatroom(chatroom!!)
            ?.stream()?.map { chatMember: ChatMember -> chatMember.member?.memberId }?.collect(Collectors.toList())

        val chatMembers: MutableList<ChatMember> = ArrayList()

        for (member in members) {
            if (oldMembersId != null) {
                if (oldMembersId.contains(member!!.memberId)) {
                    continue
                }
            }
            chatMembers.add(ChatMember.Companion.create(member, chatroom))
        }

        chatMemberRepository.saveAll(chatMembers)

        val chat: Chat = Chat.Companion.createGroupEnterChat(chatroom, chatMembers)
        chatRepository.save(chat)

        redisPublisher.publish(chat)
    }

    /**
     * 개인 인원 채팅방 나가기
     *
     * @param request
     * @return
     */
    @Transactional
    override fun leaveChatMember(request: ChatMemberServiceRequest?): ChatMemberResponse {
        val member = getMember(request!!.memberId)
        val chatroom = getChatroomByChatroomId(request.chatroomId)

        return removeChatMember(member, chatroom)
    }

    @Transactional
    override fun leaveTeamChatMember(member: Member?, teamId: Long?) {
        val chatroom = getChatroomByTeamId(teamId)

        removeChatMember(member, chatroom)
    }

    @Transactional
    override fun leaveReservationChatMember(member: Member?, reservationId: Long?) {
        val chatroom = getChatroomByReservationId(reservationId)

        removeChatMember(member, chatroom)
    }

    private fun removeChatMember(member: Member?, chatroom: Chatroom?): ChatMemberResponse {
        checkMemberInChatroom(member, chatroom)

        chatMemberRepository.deleteByMemberAndChatRoom(member, chatroom)

        val chat: Chat = Chat.Companion.createQuitChat(chatroom, member)
        chatRepository.save(chat)

        redisPublisher.publish(chat)

        return ChatMemberResponse(chatroom!!.chatroomId, member!!.memberId)
    }

    /**
     * 채팅방이 삭제됐을 때 채팅방에 참가한 사람들 연관관계 삭제
     *
     * @param chatroomId 채팅방 ID
     * @return
     */
    @Transactional
    override fun leaveChatRoom(chatroomId: Long?) {
        val chatroom = getChatroomByChatroomId(chatroomId)

        chatMemberRepository.updateIsDeletedForChatRoom(chatroom)
    }

    @Transactional
    override fun leaveTeamChatRoom(teamId: Long?) {
        val chatroom = getChatroomByTeamId(teamId)

        chatMemberRepository.updateIsDeletedForChatRoom(chatroom)
    }

    @Transactional
    override fun leaveReservationChatRoom(reservationId: Long?) {
        val chatroom = getChatroomByReservationId(reservationId)

        chatMemberRepository.updateIsDeletedForChatRoom(chatroom)
    }

    private fun checkMemberInChatroom(member: Member?, chatroom: Chatroom?) {
        require(
            chatMemberRepository.existByMemberAndChatroom(
                member!!,
                chatroom!!
            )
        ) { ExceptionMessage.MEMBER_NOT_IN_CHATROOM.text }
    }

    private fun checkMemberNotInChatroom(member: Member?, chatroom: Chatroom?) {
        require(
            !chatMemberRepository.existByMemberAndChatroom(
                member!!,
                chatroom!!
            )
        ) { ExceptionMessage.MEMBER_IN_CHATROOM.text }
    }

    private fun getMember(memberId: Long?): Member {
        return memberRepository.findByMemberId(memberId)
            ?.orElseThrow(Supplier { IllegalArgumentException(ExceptionMessage.MEMBER_NOT_FOUND.text) })!!
    }

    private fun getChatroomByChatroomId(chatroomId: Long?): Chatroom? {
        return chatroomRepository.findByChatroomId(chatroomId)
            .orElseThrow { IllegalArgumentException(ExceptionMessage.CHATROOM_NOT_FOUND.text) }
    }

    private fun getChatroomByTeamId(teamId: Long?): Chatroom? {
        return chatroomRepository.findByTeamId(teamId)
            .orElseThrow { IllegalArgumentException(ExceptionMessage.CHATROOM_NOT_FOUND.text) }
    }

    private fun getChatroomByReservationId(reservationId: Long?): Chatroom? {
        return chatroomRepository.findByReservationId(reservationId)
            .orElseThrow { IllegalArgumentException(ExceptionMessage.CHATROOM_NOT_FOUND.text) }
    }
}
