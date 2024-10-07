package team4.footwithme.docs.team;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.team.api.TeamMemberApi;
import team4.footwithme.team.api.request.TeamMemberRequest;
import team4.footwithme.team.domain.TeamMemberRole;
import team4.footwithme.team.service.TeamMemberService;
import team4.footwithme.team.service.request.TeamMemberServiceRequest;
import team4.footwithme.team.service.response.TeamResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

public class TeamMemberApiDocs extends RestDocsSupport {

    private final TeamMemberService teamMemberService = mock(TeamMemberService.class);

    @Override
    protected Object initController() {
        return new TeamMemberApi(teamMemberService);
    }

    @DisplayName("팀 멤버 추가 API")
    @Test
    void addTeamMember() throws Exception {
        long teamId = 1L;

        TeamMemberRequest request = new TeamMemberRequest(
                List.of("test1@example.com", "test2@example.com")
        );

        //response
        List<TeamResponse> responses = List.of(
            new TeamResponse(1L, teamId, "팀 멤버 1", TeamMemberRole.MEMBER),
            new TeamResponse(2L, teamId, "팀 멤버 2", TeamMemberRole.MEMBER)
        );

        given(teamMemberService.addTeamMembers(eq(teamId), any(TeamMemberServiceRequest.class)))
                .willReturn(responses);

        mockMvc.perform(post("/api/v1/team-member/{teamId}/members", teamId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("team-member-add",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("teamId").description("팀 ID")
                    ),
                    requestFields(
                        fieldWithPath("emails").type(JsonFieldType.ARRAY).description("추가할 팀 멤버들의 이메일 리스트")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("팀 멤버 리스트"),
                        fieldWithPath("data[].teamMemberId").type(JsonFieldType.NUMBER).description("팀 멤버 ID"),
                        fieldWithPath("data[].teamId").type(JsonFieldType.NUMBER).description("팀 ID"),
                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("팀 멤버 이름"),
                        fieldWithPath("data[].role").type(JsonFieldType.STRING).description("팀 멤버의 역할")
                    )
                ));
    }

    @DisplayName("팀 멤버 전체 조회 API")
    @Test
    void getTeamMember() throws Exception {
        long teamId = 1L;

        List<TeamResponse> responses = List.of(
                new TeamResponse(1L, teamId, "팀 멤버 1", TeamMemberRole.MEMBER),
                new TeamResponse(2L, teamId, "팀 멤버 2", TeamMemberRole.MEMBER)
        );

        given(teamMemberService.getTeamMembers(eq(teamId)))
                .willReturn(responses);

        mockMvc.perform(get("/api/v1/team-member/{teamId}/members", teamId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("team-member-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("팀 멤버 리스트"),
                                fieldWithPath("data[].teamMemberId").type(JsonFieldType.NUMBER).description("팀 멤버 ID"),
                                fieldWithPath("data[].teamId").type(JsonFieldType.NUMBER).description("팀 ID"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("팀 멤버 이름"),
                                fieldWithPath("data[].role").type(JsonFieldType.STRING).description("팀 멤버의 역할")
                        )
                ));
    }

    @DisplayName("팀 탈퇴_팀장 API")
    @Test
    void deleteTeamMemberByCreator() throws Exception {
        long teamId = 1L;
        long teamMemberId = 1L;

        given(teamMemberService.deleteTeamMemberByCreator(eq(teamId),eq(teamMemberId),any()))
                .willReturn(teamMemberId);

        mockMvc.perform(delete("/api/v1/team-member/{teamId}/members/{teamMemberId}", teamId, teamMemberId)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("team-member-deleteByCreator",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("teamMemberId").description("삭제할 팀멤버 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("응답 데이터 삭제한 팀 멤버 ID")
                        )
                ));
    }

    @DisplayName("팀 탈퇴_멤버 API")
    @Test
    void deleteTeamMember() throws Exception {
        long teamId = 1L;

        given(teamMemberService.deleteTeamMember(eq(teamId), any()))
                .willReturn(teamId);

        mockMvc.perform(delete("/api/v1/team-member/{teamId}/members", teamId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("team-member-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("응답 데이터 삭제한 팀 멤버 ID")
                        )
                ));
    }
}
