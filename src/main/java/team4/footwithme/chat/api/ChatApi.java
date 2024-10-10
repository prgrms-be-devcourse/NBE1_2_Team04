package team4.footwithme.chat.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.chat.api.request.ChatRequest;
import team4.footwithme.chat.api.request.ChatUpdateRequest;
import team4.footwithme.chat.service.ChatService;
import team4.footwithme.chat.service.response.ChatResponse;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.member.jwt.PrincipalDetails;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat/message")
public class ChatApi {
    private final ChatService chatService;

    /**
     * 채팅 보내기
     */
    // TODO : test를 위해 email을 변수로 설정함. 나중에 매개변수에 @AuthenticationPrincipal에서 이메일 빼오기
    @MessageMapping("/api/v1/chat/message")
    public void sendMessage(@Valid ChatRequest request, @Header("Authorization") String token) {
        chatService.sendMessage(request.toServiceRequest(), token);
    }

    /**
     * 채팅 조회
     *
     * @param chatroomId 채팅방 ID
     * @param page       현재 페이지
     * @param size       한페이지에 나타날 갯수
     * @return
     */
    @GetMapping("/{chatroomId}")
    public ApiResponse<Slice<ChatResponse>> getChatting(@PathVariable Long chatroomId, @RequestParam int page, @RequestParam int size, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ApiResponse.ok(chatService.getChatList(chatroomId, pageRequest, principalDetails.getMember()));
    }

    /**
     * 채팅 수정
     */
    @PutMapping("/{chatId}")
    public ApiResponse<ChatResponse> updateChatting(@PathVariable Long chatId, @RequestBody @Valid ChatUpdateRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.ok(chatService.updateChat(request.toServiceRequest(), principalDetails.getMember(), chatId));
    }

    /**
     * 채팅 삭제
     */
    @DeleteMapping("/{chatId}")
    public ApiResponse<Long> deleteChatting(@PathVariable Long chatId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.ok(chatService.deleteChat(principalDetails.getMember(), chatId));
    }
}