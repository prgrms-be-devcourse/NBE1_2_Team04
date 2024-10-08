package team4.footwithme.chat.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.chat.api.request.ChatUpdateRequest;
import team4.footwithme.chat.domain.ChatType;
import team4.footwithme.chat.service.request.ChatUpdateServiceRequest;
import team4.footwithme.chat.service.response.ChatMemberInfo;
import team4.footwithme.chat.service.response.ChatResponse;
import team4.footwithme.chat.service.response.ChatroomResponse;
import team4.footwithme.member.domain.MemberRole;
import team4.footwithme.security.WithMockPrincipalDetail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatApiTest extends ApiTestSupport {

    // 채팅을 조회할 수 있다.
    @DisplayName("채팅을 조회할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void getChat() throws Exception {
        LocalDateTime createdAt = LocalDateTime.now();
        int page = 1;
        int size = 1;

        ChatResponse chatResponse = new ChatResponse(
                1L,
                createdAt,
                createdAt,
                new ChatroomResponse(
                        1L,
                        "채팅방 1"
                ),
                new ChatMemberInfo(
                        1L,
                        "a@a.com",
                        "이름 1",
                        MemberRole.USER
                ),
                ChatType.TALK,
                "채팅 1"
        );

        List<ChatResponse> chatResponseList = new ArrayList<>();

        chatResponseList.add(chatResponse);

        Slice<ChatResponse> chatroomResponses = new SliceImpl<>(
                chatResponseList, PageRequest.of(page - 1, size, Sort.by("createdAt").descending()), false
        );

        given(chatService.getChatList(any(Long.class), any(PageRequest.class), argThat(m -> m.getEmail().equals("a@a.com"))))
                .willReturn(chatroomResponses);

        mockMvc.perform(get("/api/v1/chat/message/{chatroomId}", 1L).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("size", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].chatId").value(1L))
                .andExpect(jsonPath("$.data.content[0].createdAt").value(containsString(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.data.content[0].updatedAt").value(containsString(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.data.content[0].chatroomResponse").isMap())
                .andExpect(jsonPath("$.data.content[0].chatroomResponse.chatroomId").value(1L))
                .andExpect(jsonPath("$.data.content[0].chatroomResponse.name").value("채팅방 1"))
                .andExpect(jsonPath("$.data.content[0].memberInfo").isMap())
                .andExpect(jsonPath("$.data.content[0].memberInfo.memberId").value(1L))
                .andExpect(jsonPath("$.data.content[0].memberInfo.email").value("a@a.com"))
                .andExpect(jsonPath("$.data.content[0].memberInfo.name").value("이름 1"))
                .andExpect(jsonPath("$.data.content[0].memberInfo.memberRole").value(MemberRole.USER.toString()))
                .andExpect(jsonPath("$.data.content[0].chatType").value("TALK"))
                .andExpect(jsonPath("$.data.content[0].text").value("채팅 1"))
                .andExpect(jsonPath("$.data.pageable").isMap())
                .andExpect(jsonPath("$.data.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageable.pageSize").value(1))
                .andExpect(jsonPath("$.data.pageable.sort").isMap())
                .andExpect(jsonPath("$.data.pageable.sort.empty").value(false))
                .andExpect(jsonPath("$.data.pageable.sort.unsorted").value(false))
                .andExpect(jsonPath("$.data.pageable.sort.sorted").value(true))
                .andExpect(jsonPath("$.data.pageable.offset").value(0))
                .andExpect(jsonPath("$.data.pageable.paged").value(true))
                .andExpect(jsonPath("$.data.pageable.unpaged").value(false))
                .andExpect(jsonPath("$.data.size").value(1))
                .andExpect(jsonPath("$.data.number").value(0))
                .andExpect(jsonPath("$.data.sort").isMap())
                .andExpect(jsonPath("$.data.sort.empty").value(false))
                .andExpect(jsonPath("$.data.sort.unsorted").value(false))
                .andExpect(jsonPath("$.data.sort.sorted").value(true))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true))
                .andExpect(jsonPath("$.data.numberOfElements").value(1))
                .andExpect(jsonPath("$.data.empty").value(false));
    }


    // 기존 채팅을 수정할 수 있다.
    @DisplayName("기존 채팅을 수정할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void updateChat() throws Exception {
        LocalDateTime createdAt = LocalDateTime.now();
        long chatId = 1L;

        ChatUpdateRequest request = new ChatUpdateRequest("채팅 2");

        ChatResponse chatResponse = new ChatResponse(
                1L,
                createdAt,
                createdAt,
                new ChatroomResponse(
                        1L,
                        "채팅방 1"
                ),
                new ChatMemberInfo(
                        1L,
                        "a@a.com",
                        "이름 1",
                        MemberRole.USER
                ),
                ChatType.TALK,
                "채팅 2"
        );

        given(chatService.updateChat(any(ChatUpdateServiceRequest.class), argThat(m -> m.getEmail().equals("a@a.com")), eq(chatId)))
                .willReturn(chatResponse);

        mockMvc.perform(put("/api/v1/chat/message/{chatId}", 1L).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.chatId").value(1L))
                .andExpect(jsonPath("$.data.createdAt").value(containsString(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.data.updatedAt").value(containsString(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.data.chatroomResponse").isMap())
                .andExpect(jsonPath("$.data.chatroomResponse.chatroomId").value(1L))
                .andExpect(jsonPath("$.data.chatroomResponse.name").value("채팅방 1"))
                .andExpect(jsonPath("$.data.memberInfo").isMap())
                .andExpect(jsonPath("$.data.memberInfo.memberId").value(1L))
                .andExpect(jsonPath("$.data.memberInfo.email").value("a@a.com"))
                .andExpect(jsonPath("$.data.memberInfo.name").value("이름 1"))
                .andExpect(jsonPath("$.data.memberInfo.memberRole").value(MemberRole.USER.toString()))
                .andExpect(jsonPath("$.data.chatType").value("TALK"))
                .andExpect(jsonPath("$.data.text").value("채팅 2"));
    }

    // 기존 채팅을 삭제할 수 있다.
    @DisplayName("기존 채팅을 삭제할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void deleteChat() throws Exception {
        long chatId = 1L;

        given(chatService.deleteChat(argThat(m -> m.getEmail().equals("a@a.com")), eq(chatId)))
                .willReturn(chatId);

        mockMvc.perform(delete("/api/v1/chat/message/{chatId}", 1L).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value(1L));
    }
}
