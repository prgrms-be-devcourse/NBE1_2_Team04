package team4.footwithme.docs.chat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.chat.api.ChatMemberApi;
import team4.footwithme.chat.api.request.ChatMemberRequest;
import team4.footwithme.chat.service.ChatMemberService;
import team4.footwithme.chat.service.request.ChatMemberServiceRequest;
import team4.footwithme.chat.service.response.ChatMemberResponse;
import team4.footwithme.docs.RestDocsSupport;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatMemberApiDocs extends RestDocsSupport {
    private final ChatMemberService chatMemberService = mock(ChatMemberService.class);

    @Override
    protected Object initController() {
        return new ChatMemberApi(chatMemberService);
    }

    // 채팅방에 멤버를 초대하는 API
    @DisplayName("채팅방에 멤버를 초대하는 API")
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
                .andExpect(status().isOk())
                .andDo(document("chat-member-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("chatroomId").description("채팅방 ID"),
                                fieldWithPath("memberId").description("회원 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.chatroomId").type(JsonFieldType.NUMBER)
                                        .description("채팅방 ID"),
                                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                                        .description("회원 ID")
                        )
                ));
    }

    // 채팅방을 퇴장하는 API
    @DisplayName("채팅방을 퇴장하는 API")
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
                .andExpect(status().isOk())
                .andDo(document("chat-member-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("chatroomId").description("채팅방 ID"),
                                fieldWithPath("memberId").description("회원 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.chatroomId").type(JsonFieldType.NUMBER)
                                        .description("채팅방 ID"),
                                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                                        .description("회원 ID")
                        )
                ));
    }
}
