package team4.footwithme.chat.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import team4.footwithme.chat.api.request.ChatRequest;
import team4.footwithme.chat.service.ChatService;
import team4.footwithme.chat.service.response.ChatResponse;
import team4.footwithme.global.api.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ChatApi {
    private final ChatService chatService;

    /**
     * 채팅 보내기
     */
    // TODO : test를 위해 email을 변수로 설정함. 나중에 매개변수에 @AuthenticationPrincipal에서 이메일 빼오기
    @MessageMapping("/chat/message")
    public void sendMessage(@Valid ChatRequest request) {
        String email = "a@a.com";
        chatService.sendMessage(request.toServiceRequest(), email);
    }

    /**
     * 채팅 조회
     * @param chatroomId 채팅방 ID
     * @param page 현재 페이지
     * @param size 한페이지에 나타날 갯수
     * @return
     */
    @GetMapping("/chat/message/{chatroomId}")
    public ApiResponse<Slice<ChatResponse>> getChatting(@PathVariable Long chatroomId, @RequestParam int page, @RequestParam int size) {
        String email = "a@a.com";
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by("createdAt").descending());
        Slice<ChatResponse> chatList = chatService.getChatList(chatroomId, pageRequest, email);
        return ApiResponse.ok(chatList);
    }
}