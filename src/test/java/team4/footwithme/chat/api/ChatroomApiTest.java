package team4.footwithme.chat.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.chat.api.request.ChatroomRequest;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.service.request.ChatroomServiceRequest;
import team4.footwithme.chat.service.response.ChatroomResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatroomApiTest extends ApiTestSupport {


    //새로운 채팅방을 생성한다.
    @DisplayName("새로운 채팅방을 생성한다.")
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
            .willReturn(new ChatroomResponse(
                1L,
                "채팅방 1"
            ));

        mockMvc.perform(post("/api/v1/chat/room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.chatroomId").value(1L))
            .andExpect(jsonPath("$.data.name").value("채팅방 1"));
    }

    //기존 채팅방을 수정한다.
    @DisplayName("기존 채팅방을 수정한다.")
    @Test
    void updateChatroom() throws Exception {
        //given
        ChatroomRequest request = new ChatroomRequest("채팅방 2");

        Chatroom chatroom = Chatroom.create("채팅방 1");

        //response
        ChatroomResponse response = new ChatroomResponse(
            1L,
            "채팅방 2"
        );

        given(chatroomService.updateChatroom(eq(1L), any(ChatroomServiceRequest.class)))
            .willReturn(response);

        mockMvc.perform(put("/api/v1/chat/room/{chatroomId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isMap())
            .andExpect(jsonPath("$.data.chatroomId").value(1L))
            .andExpect(jsonPath("$.data.name").value("채팅방 2"));
    }

    //기존 채팅방을 삭제한다.
    @DisplayName("기존 채팅방을 삭제한다.")
    @Test
    void deleteChatroom() throws Exception {
        long chatroomId = 1L;

        Chatroom chatroom = Chatroom.create("채팅방 1");

        given(chatroomService.deleteChatroomByChatroomId(chatroomId))
            .willReturn(chatroomId);

        mockMvc.perform(delete("/api/v1/chat/room/{chatroomId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").value(1L));
    }
}
