package team4.footwithme.chat.api

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import team4.footwithme.chat.api.request.ChatroomRequest
import team4.footwithme.chat.service.ChatroomService
import team4.footwithme.chat.service.response.ChatroomResponse
import team4.footwithme.global.api.ApiResponse

@RestController
@RequestMapping("/api/v1/chat/room")
class ChatroomApi(private val chatroomService: ChatroomService) {
    /**
     * 채팅방 생성
     * 팀, 예약 생성시 같이 실행해주기
     */
    @PostMapping
    fun createChatroom(@RequestBody chatroomRequest: @Valid ChatroomRequest?): ApiResponse<ChatroomResponse?> {
        return ApiResponse.Companion.created<ChatroomResponse?>(chatroomService.createChatroom(chatroomRequest!!.toServiceRequest()))
    }

    /**
     * 채팅방 삭제
     */
    @DeleteMapping("/{chatroomId}")
    fun deleteChatroom(@PathVariable("chatroomId") chatroomId: Long?): ApiResponse<Long?> {
        return ApiResponse.Companion.ok<Long?>(chatroomService.deleteChatroomByChatroomId(chatroomId))
    }

    /**
     * 채팅방 수정
     */
    @PutMapping("/{chatroomId}")
    fun updateChatroom(
        @PathVariable("chatroomId") chatroomId: Long?,
        @RequestBody chatroomRequest: @Valid ChatroomRequest?
    ): ApiResponse<ChatroomResponse?> {
        return ApiResponse.Companion.ok<ChatroomResponse?>(
            chatroomService.updateChatroom(
                chatroomId,
                chatroomRequest!!.toServiceRequest()
            )
        )
    }
}
