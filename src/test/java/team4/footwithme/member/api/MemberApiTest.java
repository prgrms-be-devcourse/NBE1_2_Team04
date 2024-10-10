package team4.footwithme.member.api;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import team4.footwithme.ApiTestSupport;
import team4.footwithme.member.api.request.JoinRequest;
import team4.footwithme.member.api.request.LoginRequest;
import team4.footwithme.member.api.request.UpdatePasswordRequest;
import team4.footwithme.member.api.request.UpdateRequest;
import team4.footwithme.member.domain.*;
import team4.footwithme.member.jwt.response.TokenResponse;
import team4.footwithme.member.service.request.JoinServiceRequest;
import team4.footwithme.member.service.request.LoginServiceRequest;
import team4.footwithme.member.service.request.UpdatePasswordServiceRequest;
import team4.footwithme.member.service.request.UpdateServiceRequest;
import team4.footwithme.member.service.response.LoginResponse;
import team4.footwithme.member.service.response.MemberResponse;
import team4.footwithme.security.WithMockPrincipalDetail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberApiTest extends ApiTestSupport {

    @DisplayName("회원 가입을 진행한다.")
    @Test
    void join() throws Exception {
        //given
        JoinRequest request = new JoinRequest("test@naver.com", "!test1234", "!test1234", "test", "010-1234-1234", LoginProvider.ORIGINAL, "", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        MemberResponse response = new MemberResponse(1L, "test@naver.com", "test", "010-1234-1234", Gender.MALE, MemberRole.USER, TermsAgreed.AGREE);

        //when
        given(memberService.join(any(JoinServiceRequest.class)))
            .willReturn(response);

        //then

        mockMvc.perform(post("/api/v1/members/join")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andExpect(jsonPath("$.message").value("CREATED"))
            .andExpect(jsonPath("$.data.memberId").value(1L))
            .andExpect(jsonPath("$.data.email").value("test@naver.com"))
            .andExpect(jsonPath("$.data.name").value("test"))
            .andExpect(jsonPath("$.data.phoneNumber").value("010-1234-1234"))
            .andExpect(jsonPath("$.data.gender").value("MALE"))
            .andExpect(jsonPath("$.data.memberRole").value("USER"))
            .andExpect(jsonPath("$.data.termsAgreed").value("AGREE"));
    }

    @DisplayName("로그인을 진행한다.")
    @Test
    void login() throws Exception {
        //given
        LoginRequest request = new LoginRequest("test@naver.com", "test1234!");

        LoginResponse response = new LoginResponse("accessToken", "refreshToken");

        //when
        given(memberService.login(any(LoginServiceRequest.class)))
            .willReturn(response);

        //then

        mockMvc.perform(post("/api/v1/members/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.accessToken").value("accessToken"))
            .andExpect(jsonPath("$.data.refreshToken").value("refreshToken"));
    }

    @DisplayName("이름, 전화번호, 성별을 수정한다.")
    @WithMockPrincipalDetail(email = "test@naver.com")
    @Test
    void update() throws Exception {
        //given
        UpdateRequest request = new UpdateRequest("문가박", "010-1221-1221", Gender.FEMALE);
        MemberResponse response = new MemberResponse(1L, "test@naver.com", "문가박", "010-1221-1221", Gender.FEMALE, MemberRole.USER, TermsAgreed.AGREE);


        //when
        given(memberService.update(any(Member.class), any(UpdateServiceRequest.class)))
            .willReturn(response);

        //then
        mockMvc.perform(put("/api/v1/members/update")
                .header("Authorization", "Bearer " + "AccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))  // CSRF 토큰 추가
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.memberId").value(1L))
            .andExpect(jsonPath("$.data.email").value("test@naver.com"))
            .andExpect(jsonPath("$.data.name").value("문가박"))
            .andExpect(jsonPath("$.data.phoneNumber").value("010-1221-1221"))
            .andExpect(jsonPath("$.data.gender").value("FEMALE"))
            .andExpect(jsonPath("$.data.memberRole").value("USER"))
            .andExpect(jsonPath("$.data.termsAgreed").value("AGREE"));
    }

    @DisplayName("비밀번호를 수정한다.")
    @WithMockPrincipalDetail(email = "test@naver.com")
    @Test
    void updatePassword() throws Exception {
        //given
        UpdatePasswordRequest request = new UpdatePasswordRequest("test1234!", "!test123", "!test123");
        String response = "Success Change Password";


        //when
        given(memberService.updatePassword(any(Member.class), any(UpdatePasswordServiceRequest.class)))
            .willReturn(response);

        //then

        mockMvc.perform(put("/api/v1/members/update-password")
                .header("Authorization", "Bearer " + "AccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))  // CSRF 토큰 추가
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").value(response));
    }

    @DisplayName("토큰 재발급을 진행한다.")
    @Test
    void reissue() throws Exception {
        //given
        TokenResponse response = new TokenResponse("accessToken", "refreshToken", 16655L);
        String refreshToken = "refreshToken";

        //when
        given(memberService.reissue(any(HttpServletRequest.class), eq(refreshToken)))
            .willReturn(response);

        //then

        mockMvc.perform(post("/api/v1/members/reissue")
                .header("Authorization", "Bearer " + "AccessToken")
                .header("refresh_token", refreshToken)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))  // CSRF 토큰 추가
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data.accessToken").value("accessToken"))
            .andExpect(jsonPath("$.data.refreshToken").value("refreshToken"))
            .andExpect(jsonPath("$.data.refreshTokenExpirationTime").value(16655L));
    }

}