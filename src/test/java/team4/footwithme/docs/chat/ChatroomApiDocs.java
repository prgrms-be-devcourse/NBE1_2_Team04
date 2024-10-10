package team4.footwithme.docs.chat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.chat.api.ChatroomApi;
import team4.footwithme.chat.api.request.ChatroomRequest;
import team4.footwithme.chat.service.ChatroomService;
import team4.footwithme.chat.service.request.ChatroomServiceRequest;
import team4.footwithme.chat.service.response.ChatroomResponse;
import team4.footwithme.docs.RestDocsSupport;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatroomApiDocs extends RestDocsSupport {

    private final ChatroomService chatroomService = mock(ChatroomService.class);

    @Override
    protected Object initController() {
        return new ChatroomApi(chatroomService);
    }

    // 새로운 채팅방을 생성한다.
    @DisplayName("채팅방을 생성하는 API")
    @Test
    void createChatroom() throws Exception {
        //given
        ChatroomRequest request = new ChatroomRequest("채팅방 1");

        //response
        ChatroomResponse response = new ChatroomResponse(
            1L,
            "채팅방 1"
        );

        given(chatroomService.createChatroom(any(ChatroomServiceRequest.class)))
            .willReturn(response);

        mockMvc.perform(post("/api/v1/chat/room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("chatroom-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("name").description("채팅방 이름")
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
                    fieldWithPath("data.name").type(JsonFieldType.STRING)
                        .description("채팅방 이름")
                )
            ));
    }

    // 기존 채팅방을 수정한다.
    @DisplayName("채팅방을 수정하는 API")
    @Test
    void updateChatroom() throws Exception {
        //given
        ChatroomRequest request = new ChatroomRequest("채팅방 2");

        //response
        ChatroomResponse response = new ChatroomResponse(
            1L,
            "채팅방 2"
        );

        given(chatroomService.updateChatroom(eq(1L), any(ChatroomServiceRequest.class)))
            .willReturn(new ChatroomResponse(
                1L,
                "채팅방 2"
            ));

        mockMvc.perform(put("/api/v1/chat/room/{chatroomId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andDo(document("chatroom-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("name").description("채팅방 이름")
                ),
                pathParameters(
                    parameterWithName("chatroomId").description("채팅방 ID")
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
                    fieldWithPath("data.name").type(JsonFieldType.STRING)
                        .description("채팅방 이름")
                )
            ));
    }

    // 기존 채팅방을 삭제한다.
    @DisplayName("채팅방을 삭제하는 API")
    @Test
    void deleteChatroom() throws Exception {
        long chatroomId = 1L;

        given(chatroomService.deleteChatroomByChatroomId(chatroomId))
            .willReturn(chatroomId);

        mockMvc.perform(delete("/api/v1/chat/room/{chatroomId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("chatroom-delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("chatroomId").description("채팅방 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.NUMBER)
                        .description("응답 데이터 삭제한 채팅방 ID")
                )
            ));
    }
}
