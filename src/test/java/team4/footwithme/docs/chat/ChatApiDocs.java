package team4.footwithme.docs.chat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.chat.api.ChatApi;
import team4.footwithme.chat.api.request.ChatUpdateRequest;
import team4.footwithme.chat.domain.ChatType;
import team4.footwithme.chat.service.ChatService;
import team4.footwithme.chat.service.request.ChatUpdateServiceRequest;
import team4.footwithme.chat.service.response.ChatMemberInfo;
import team4.footwithme.chat.service.response.ChatResponse;
import team4.footwithme.chat.service.response.ChatroomResponse;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.domain.MemberRole;
import team4.footwithme.security.WithMockPrincipalDetail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatApiDocs extends RestDocsSupport {
    private final ChatService chatService = mock(ChatService.class);

    @Override
    protected Object initController() {
        return new ChatApi(chatService);
    }

    // 채팅을 조회하는 API
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

        given(chatService.getChatList(any(Long.class), any(PageRequest.class), any(Member.class)))
                .willReturn(chatroomResponses);

        mockMvc.perform(get("/api/v1/chat/message/{chatroomId}", 1L).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andDo(document("chat-get",
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
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.content").type(JsonFieldType.ARRAY)
                                        .description("컨텐츠 리스트"),
                                fieldWithPath("data.content[].chatId").type(JsonFieldType.NUMBER)
                                        .description("채팅 ID"),
                                fieldWithPath("data.content[].createdAt").type(JsonFieldType.ARRAY)
                                        .description("채팅 생성일"),
                                fieldWithPath("data.content[].updatedAt").type(JsonFieldType.ARRAY)
                                        .description("채팅 수정일"),
                                fieldWithPath("data.content[].chatroomResponse").type(JsonFieldType.OBJECT)
                                        .description("채팅방 정보"),
                                fieldWithPath("data.content[].chatroomResponse.chatroomId").type(JsonFieldType.NUMBER)
                                        .description("채팅방 ID"),
                                fieldWithPath("data.content[].chatroomResponse.name").type(JsonFieldType.STRING)
                                        .description("채팅방 이름"),
                                fieldWithPath("data.content[].memberInfo").type(JsonFieldType.OBJECT)
                                        .description("회원 정보"),
                                fieldWithPath("data.content[].memberInfo.memberId").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("data.content[].memberInfo.email").type(JsonFieldType.STRING)
                                        .description("회원 이메일"),
                                fieldWithPath("data.content[].memberInfo.name").type(JsonFieldType.STRING)
                                        .description("회원 이름"),
                                fieldWithPath("data.content[].memberRole").type(JsonFieldType.STRING)
                                        .description("회원 권한"),
                                fieldWithPath("data.content[].chatType").type(JsonFieldType.STRING)
                                        .description("채팅 타입"),
                                fieldWithPath("data.pageable").type(JsonFieldType.OBJECT)
                                        .description("Pageable 정보"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("Pageable 페이지 넘버"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("Pageable 페이지 사이즈"),
                                fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT)
                                        .description("Pageable 정렬 정보"),
                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("Pageable 정렬 존재 확인"),
                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("Pageable 비정렬 확인"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("Pageable 정렬 확인"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("Pageable 오프셋"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("Pageable 페이징 확인"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("Pageable 비페이징 확인"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                        .description("페이지 사이즈"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                                        .description("페이지 넘버"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 정렬 존재 확인"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 비정렬 확인"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 정렬 확인"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 첫번째 확인"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 마지막 확인"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("페이지 컨텐츠 갯수"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 존재 확인")

                        )
                ));
    }

    // 채팅을 수정하는 API
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
                .andExpect(status().isOk())
                .andDo(document("chat-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("message").description("수정 할 채팅 메세지")
                        ),
                        pathParameters(
                                parameterWithName("chatId").description("수정 할 채팅 ID")
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
                                fieldWithPath("data.chatId").type(JsonFieldType.NUMBER)
                                        .description("채팅 ID"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.ARRAY)
                                        .description("채팅 생성일"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.ARRAY)
                                        .description("채팅 수정일"),
                                fieldWithPath("data.chatroomResponse").type(JsonFieldType.OBJECT)
                                        .description("채팅방 정보"),
                                fieldWithPath("data.chatroomResponse.chatroomId").type(JsonFieldType.NUMBER)
                                        .description("채팅방 ID"),
                                fieldWithPath("data.chatroomResponse.name").type(JsonFieldType.STRING)
                                        .description("채팅방 이름"),
                                fieldWithPath("data.memberInfo").type(JsonFieldType.OBJECT)
                                        .description("회원 정보"),
                                fieldWithPath("data.memberInfo.memberId").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("data.memberInfo.email").type(JsonFieldType.STRING)
                                        .description("회원 이메일"),
                                fieldWithPath("data.memberInfo.name").type(JsonFieldType.STRING)
                                        .description("회원 이름"),
                                fieldWithPath("data.memberRole").type(JsonFieldType.STRING)
                                        .description("회원 권한"),
                                fieldWithPath("data.chatType").type(JsonFieldType.STRING)
                                        .description("채팅 타입")
                        )));
    }

    // 채팅을 삭제하는 API
    @DisplayName("기존 채팅을 삭제할 수 있다.")
    @Test
    @WithMockPrincipalDetail(email = "a@a.com")
    void deleteChat() throws Exception {
        long chatId = 1L;

        given(chatService.deleteChat(argThat(m -> m.getEmail().equals("a@a.com")), eq(chatId)))
                .willReturn(chatId);

        mockMvc.perform(delete("/api/v1/chat/message/{chatId}", 1L).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("chat-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("chatId").description("삭제 할 채팅 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("삭제 된 채팅 ID")
                        )));
    }
}
