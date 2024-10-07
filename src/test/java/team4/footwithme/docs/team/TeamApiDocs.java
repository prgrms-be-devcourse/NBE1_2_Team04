package team4.footwithme.docs.team;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.team.api.TeamApi;
import team4.footwithme.team.api.request.TeamCreateRequest;
import team4.footwithme.team.api.request.TeamUpdateRequest;
import team4.footwithme.team.service.TeamService;
import team4.footwithme.team.service.request.TeamDefaultServiceRequest;
import team4.footwithme.team.service.response.TeamDefaultResponse;
import team4.footwithme.team.service.response.TeamInfoResponse;

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


public class TeamApiDocs extends RestDocsSupport {

    private final TeamService teamService = mock(TeamService.class);

    @Override
    protected Object initController() {
        return new TeamApi(teamService);
    }

    @DisplayName("팀 생성 API")
    @Test
    void createTeam() throws Exception {
        //given
        TeamCreateRequest request = new TeamCreateRequest("팀 명", "팀 설명", "선호 지역");

        //response
        TeamDefaultResponse response = new TeamDefaultResponse(
            1L,
            null,
            "팀 명",
            "팀 설명",
            0,
            0,
            0,
            "선호 지역"
        );

        given(teamService.createTeam(any(TeamDefaultServiceRequest.class),any()))
                .willReturn(response);

        mockMvc.perform(post("/api/v1/team/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("team-create",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                            fieldWithPath("name").description("팀의 이름"),
                            fieldWithPath("description").description("팀의 설명"),
                            fieldWithPath("location").description("선호 지역")
                    ),
                    responseFields(
                            fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                            fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                            fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                            fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                            fieldWithPath("data.teamId").type(JsonFieldType.NUMBER).description("팀 ID"),
                            fieldWithPath("data.stadiumId").type(JsonFieldType.NUMBER).optional().description("경기장 ID"),
                            fieldWithPath("data.name").type(JsonFieldType.STRING).description("팀 이름"),
                            fieldWithPath("data.description").type(JsonFieldType.STRING).description("팀 설명"),
                            fieldWithPath("data.winCount").type(JsonFieldType.NUMBER).description("승리 횟수"),
                            fieldWithPath("data.drawCount").type(JsonFieldType.NUMBER).description("무승부 횟수"),
                            fieldWithPath("data.loseCount").type(JsonFieldType.NUMBER).description("패배 횟수"),
                            fieldWithPath("data.location").type(JsonFieldType.STRING).description("팀 지역")
                    )
                ));
    }

    @DisplayName("팀 정보 조회 API")
    @Test
    void getTeamInfo() throws Exception {
        long teamId = 1L;

        TeamInfoResponse response = new TeamInfoResponse(
            "팀 명",
            "팀 설명",
            "선호 지역",
            0,
            0,
            0,
            List.of("좋은 경기", "훌륭한 경기"),
            2L,
            1L
        );

        given(teamService.getTeamInfo(teamId)).willReturn(response);

        mockMvc.perform(get("/api/v1/team/{teamId}/info", 1L)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
                .andDo(document("team-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("teamId").description("팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("팀 이름"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("팀 설명"),
                                fieldWithPath("data.location").type(JsonFieldType.STRING).description("선호 지역"),
                                fieldWithPath("data.winCount").type(JsonFieldType.NUMBER).description("승리 횟수"),
                                fieldWithPath("data.loseCount").type(JsonFieldType.NUMBER).description("패배 횟수"),
                                fieldWithPath("data.drawCount").type(JsonFieldType.NUMBER).description("무승부 횟수"),
                                fieldWithPath("data.evaluation").type(JsonFieldType.ARRAY).description("팀 평가 리스트 (String 타입의 평가 내용)"),
                                fieldWithPath("data.maleCount").type(JsonFieldType.NUMBER).description("남자 팀원 수"),
                                fieldWithPath("data.femaleCount").type(JsonFieldType.NUMBER).description("여자 팀원 수")
                        )
                ));
    }

    @DisplayName("팀 정보 수정")
    @Test
    void updateTeamInfo() throws Exception{
        long teamId = 1L;
        TeamUpdateRequest request = new TeamUpdateRequest("수정할 팀명", "수정할 설명", "수정할 지역");
        //response
        TeamDefaultResponse response = new TeamDefaultResponse(
                teamId,
                null,
                "수정한 팀 명",
                "수정한 팀 설명",
                0,
                0,
                0,
                "수정한 선호 지역"
        );

        given(teamService.updateTeamInfo(eq(teamId),any(TeamDefaultServiceRequest.class),any()))
                .willReturn(response);

        mockMvc.perform(put("/api/v1/team/{teamId}/info", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("team-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정한 팀의 이름"),
                                fieldWithPath("description").description("수정한 팀의 설명"),
                                fieldWithPath("location").description("수정한 선호 지역")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.teamId").type(JsonFieldType.NUMBER).description("팀 ID"),
                                fieldWithPath("data.stadiumId").type(JsonFieldType.NUMBER).optional().description("경기장 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("팀 이름"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("팀 설명"),
                                fieldWithPath("data.winCount").type(JsonFieldType.NUMBER).description("승리 횟수"),
                                fieldWithPath("data.drawCount").type(JsonFieldType.NUMBER).description("무승부 횟수"),
                                fieldWithPath("data.loseCount").type(JsonFieldType.NUMBER).description("패배 횟수"),
                                fieldWithPath("data.location").type(JsonFieldType.STRING).description("팀 지역")
                        )
                ));
    }

    @DisplayName("팀 삭제 API")
    @Test
    void deleteTeam() throws Exception {
        long teamId = 1L;

        given(teamService.deleteTeam(eq(teamId), any()))
                .willReturn(teamId);

        mockMvc.perform(delete("/api/v1/team/{teamId}", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("team-delete",
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
                                        .description("응답 데이터 삭제한 팀 ID")
                        )
                ));
    }
}
