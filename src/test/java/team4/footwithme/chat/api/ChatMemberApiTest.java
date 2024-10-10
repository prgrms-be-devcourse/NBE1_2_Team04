package team4.footwithme.chat.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.chat.api.request.ChatMemberRequest;
import team4.footwithme.chat.service.request.ChatMemberServiceRequest;
import team4.footwithme.chat.service.response.ChatMemberResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatMemberApiTest extends ApiTestSupport {

    // 채팅방에 멤버를 초대할 수 있다.
    @DisplayName("채팅방에 멤버를 초대할 수 있다.")
    @Test
    public void addChatMember() throws Exception {
        ChatMemberRequest request = new ChatMemberRequest(
            1L,
            1L
        );

        ChatMemberResponse response = new ChatMemberResponse(
            1L,
            1L
        );

        given(chatMemberService.joinChatMember(any(ChatMemberServiceRequest.class)))
            .willReturn(response);

        mockMvc.perform(post("/api/v1/chat/member").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.chatroomId").value(1L))
            .andExpect(jsonPath("$.data.memberId").value(1L));
    }

    // 채팅방에서 멤버가 퇴장할 수 있다.
    @DisplayName("채팅방에서 멤버가 퇴장할 수 있다.")
    @Test
    public void removeChatMember() throws Exception {
        ChatMemberRequest request = new ChatMemberRequest(
            1L,
            1L
        );

        ChatMemberResponse response = new ChatMemberResponse(
            1L,
            1L
        );

        given(chatMemberService.leaveChatMember(any(ChatMemberServiceRequest.class)))
            .willReturn(response);

        mockMvc.perform(delete("/api/v1/chat/member").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.chatroomId").value(1L))
            .andExpect(jsonPath("$.data.memberId").value(1L));
    }
}
