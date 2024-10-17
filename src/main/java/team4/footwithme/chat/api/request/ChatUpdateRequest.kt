package team4.footwithme.chat.api.request;

import jakarta.validation.constraints.NotBlank;
import team4.footwithme.chat.service.request.ChatUpdateServiceRequest;

public record ChatUpdateRequest(
    @NotBlank(message = "채팅 메세지는 필수입니다.")
    String message
) {
    // 코드 재사용을 원해서 serviceRequest에 null값을 넣어줬는데 괜찮은지 모르겠네요 ㅜㅜ
    public ChatUpdateServiceRequest toServiceRequest() {
        return new ChatUpdateServiceRequest(message);
    }
}
