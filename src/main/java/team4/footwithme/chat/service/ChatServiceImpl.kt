package team4.footwithme.chat.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team4.footwithme.chat.domain.Chat
import team4.footwithme.chat.domain.Chatroom
import team4.footwithme.chat.repository.ChatMemberRepository
import team4.footwithme.chat.repository.ChatRepository
import team4.footwithme.chat.repository.ChatroomRepository
import team4.footwithme.chat.service.request.ChatServiceRequest
import team4.footwithme.chat.service.request.ChatUpdateServiceRequest
import team4.footwithme.chat.service.response.ChatResponse
import team4.footwithme.global.exception.ExceptionMessage
import team4.footwithme.member.domain.Member
import team4.footwithme.member.jwt.JwtTokenUtil
import team4.footwithme.member.repository.MemberRepository

@Service
class ChatServiceImpl(
    private val chatRepository: ChatRepository,
    private val chatroomRepository: ChatroomRepository,
    private val memberRepository: MemberRepository,
    private val chatMemberRepository: ChatMemberRepository,
    private val redisPublisher: RedisPublisher,
    private val jwtTokenUtil: JwtTokenUtil
) : ChatService {
    /**
     * 메세지 보내기
     *
     * @param request
     */
    @Transactional
    override fun sendMessage(request: ChatServiceRequest?, token: String) {
        // 채팅방에 참여한 멤버인지 검증
        val accessToken = token.substring(7).trim { it <= ' ' }
        val email = jwtTokenUtil.getEmailFromToken(accessToken)

        val member = getMember(email)
        val chatroom = getChatroom(request!!.ChatroomId)
        checkMemberInChatroom(member, chatroom)

        // 메시지를 데이터베이스에 저장
        val chat: Chat = Chat.Companion.createTalkChat(chatroom, member, request.message)
        chatRepository.save(chat)

        // Redis에 메시지 발행
        redisPublisher.publish(chat)
    }

    /**
     * 특정 채팅방에 과거 메세지 조회
     * 페이지네이션
     * 채팅방 메세지를 보려면 채팅방에 소속된 멤버여야 함
     */
    @Transactional(readOnly = true)
    override fun getChatList(chatroomId: Long?, pageRequest: PageRequest?, member: Member?): Slice<ChatResponse> {
        val chatroom = getChatroom(chatroomId)
        checkMemberInChatroom(member, chatroom)

        return chatRepository.findChatByChatroom(chatroom!!, pageRequest!!)
    }

    /**
     * 채팅 수정
     */
    @Transactional
    override fun updateChat(request: ChatUpdateServiceRequest?, member: Member?, chatId: Long?): ChatResponse {
        val chat = getChat(chatId)

        checkChatByMember(member, chat)

        chat.updateMessage(request!!.message)

        return ChatResponse(chat)
    }

    /**
     * 채팅 삭제
     */
    @Transactional
    override fun deleteChat(member: Member?, chatId: Long?): Long? {
        val chat = getChat(chatId)

        checkChatByMember(member, chat)

        chatRepository.delete(chat)

        return chatId
    }

    /**
     * 채팅방에 소속된 멤버인지 검증하는 메소드
     */
    private fun checkMemberInChatroom(member: Member?, chatroom: Chatroom?) {
        require(
            chatMemberRepository.existByMemberAndChatroom(
                member!!,
                chatroom!!
            )
        ) { ExceptionMessage.MEMBER_NOT_IN_CHATROOM.text }
    }

    /**
     * 채팅을 작성한 멤버인지 검증하는 메소드
     *
     * @param member
     * @param chat
     */
    private fun checkChatByMember(member: Member?, chat: Chat) {
        require(chat.member == member) { ExceptionMessage.UNAUTHORIZED_MESSAGE_EDIT.text }
    }

    private fun getMember(email: String): Member {
        return memberRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException(ExceptionMessage.MEMBER_NOT_FOUND.text) }!!
    }

    private fun getChatroom(chatroomId: Long?): Chatroom? {
        return chatroomRepository.findByChatroomId(chatroomId)
            .orElseThrow { IllegalArgumentException(ExceptionMessage.CHATROOM_NOT_FOUND.text) }
    }

    private fun getChat(chatId: Long?): Chat {
        return chatRepository.findByChatId(chatId)
            .orElseThrow { IllegalArgumentException(ExceptionMessage.CHAT_NOT_FOUND.text) }
    }
}
