package team4.footwithme.docs.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import team4.footwithme.config.SecurityConfig;
import team4.footwithme.docs.RestDocsSupport;
import team4.footwithme.member.api.MemberApi;
import team4.footwithme.member.api.request.JoinRequest;
import team4.footwithme.member.api.request.LoginRequest;
import team4.footwithme.member.api.request.UpdatePasswordRequest;
import team4.footwithme.member.api.request.UpdateRequest;
import team4.footwithme.member.domain.Gender;
import team4.footwithme.member.domain.LoginProvider;
import team4.footwithme.member.domain.MemberRole;
import team4.footwithme.member.domain.TermsAgreed;
import team4.footwithme.member.jwt.PrincipalDetails;
import team4.footwithme.member.jwt.response.TokenResponse;
import team4.footwithme.member.service.CookieService;
import team4.footwithme.member.service.MemberService;
import team4.footwithme.member.service.request.JoinServiceRequest;
import team4.footwithme.member.service.request.LoginServiceRequest;
import team4.footwithme.member.service.request.UpdatePasswordServiceRequest;
import team4.footwithme.member.service.request.UpdateServiceRequest;
import team4.footwithme.member.service.response.LoginResponse;
import team4.footwithme.member.service.response.MemberResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MemberApiDocs extends RestDocsSupport {
    
    private final MemberService memberService = mock(MemberService.class);
    private final CookieService cookieService = mock(CookieService.class);

    @Override
    protected Object initController() {
        return new MemberApi(memberService, cookieService);
    }
    
    @DisplayName("회원 가입을 진행하는 API")
    @Test
    void joinMember() throws Exception{
        //given
        JoinRequest request = new JoinRequest("test@naver.com", "!test1234","!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "",  Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        MemberResponse response = new MemberResponse(1L, "test@naver.com", "test", "010-1234-1234", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        //when
        given(memberService.join(any(JoinServiceRequest.class)))
                .willReturn(response);

        //then
        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("member-join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("passwordConfirm").description("비밀번호 확인"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("phoneNumber").description("전화 번호"),
                                fieldWithPath("loginProvider").description("SNS 로그인 공급처"),
                                fieldWithPath("snsId").description("SNS 로그인 아이디"),
                                fieldWithPath("gender").description("성별"),
                                fieldWithPath("memberRole").description("권한"),
                                fieldWithPath("termsAgree").description("동의 여부")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("멤버 ID"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("전화 번호"),
                                fieldWithPath("data.gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("data.memberRole").type(JsonFieldType.STRING).description("역할"),
                                fieldWithPath("data.termsAgreed").type(JsonFieldType.STRING).description("AGREE")
                        )
                ));
    }

    @DisplayName("로그인을 진행하는 API")
    @Test
    void loginMember() throws Exception{
        //given
        LoginRequest request = new LoginRequest("test@naver.com", "!test1234");
        LoginResponse response = new LoginResponse("ACCESS_TOKEN", "REFRESH_TOKEN");

        //when
        given(memberService.login(any(LoginServiceRequest.class)))
                .willReturn(response);

        //then
        mockMvc.perform(post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("member-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레쉬 토큰")
                        )
                ));
    }

    @DisplayName("로그아웃을 진행하는 API")
    @Test
    void logoutMember() throws Exception{
        //given
        String response = "Success Logout";

        //when
        given(memberService.logout(any(HttpServletRequest.class)))
                .willReturn(response);

        //then
        mockMvc.perform(delete("/api/v1/members/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-logout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("성공 메세지")
                        )
                ));

    }

    @DisplayName("토큰 갱신을 진행하는 API")
    @Test
    void reissueToken() throws Exception{
        //given
        TokenResponse response = new TokenResponse("ACCESS_TOKEN", "REFERSH_TOKEN", 66666L);

        //when
        given(memberService.reissue(any(HttpServletRequest.class), any(String.class)))
                .willReturn(response);

        //then
        mockMvc.perform(post("/api/v1/members/reissue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-reissue",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레쉬 토큰"),
                                fieldWithPath("data.refreshTokenExpirationTime").type(JsonFieldType.NUMBER).description("리프레쉬 토큰 만료 시간")
                        )
                ));

    }


    @DisplayName("이름, 전화번호, 성별 수정을 진행하는 API")
    @Test
    void updateMember() throws Exception{
        //given
        UpdateRequest request = new UpdateRequest("mjk", "010-2222-2222", Gender.FEMALE);
        MemberResponse response = new MemberResponse(1L, "test@naver.com", "test", "010-1234-1234", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        //when
        given(memberService.update(any(PrincipalDetails.class), any(UpdateServiceRequest.class)))
                .willReturn(response);

        //then
        mockMvc.perform(put("/api/v1/members/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("member-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("멤버 ID"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                fieldWithPath("data.gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("data.memberRole").type(JsonFieldType.STRING).description("권한"),
                                fieldWithPath("data.termsAgreed").type(JsonFieldType.STRING).description("정보 제공 동의")
                        )
                ));

    }

    @DisplayName("비밀번호 수정을 진행하는 API")
    @Test
    void updatePassword() throws Exception{
        //given
        UpdatePasswordRequest request = new UpdatePasswordRequest("!test123", "Test1234!", "Test1234!");
        String response = "Success Change Password";

        //when
        given(memberService.updatePassword(any(PrincipalDetails.class), any(UpdatePasswordServiceRequest.class)))
                .willReturn(response);

        //then
        mockMvc.perform(put("/api/v1/members/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("member-update-password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("성공 메시지")
                        )
                ));

    }
}
