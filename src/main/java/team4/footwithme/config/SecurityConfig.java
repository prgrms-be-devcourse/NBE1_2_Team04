package team4.footwithme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import team4.footwithme.global.api.ApiResponse;
import team4.footwithme.global.exception.ExceptionHandlerFilter;
import team4.footwithme.member.domain.MemberRole;
import team4.footwithme.member.jwt.JwtTokenFilter;
import team4.footwithme.member.oauth2.CustomOAuth2LoginSuccessHandler;
import team4.footwithme.member.oauth2.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(FormLoginConfigurer::disable)
            .sessionManagement((sessionManagement) -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers("/api/v1/members/join", "/api/v1/members/login").permitAll()
                .requestMatchers("/api/v1/court/**", "/api/v1/stadium/**").permitAll()
                .requestMatchers("/api/v1/team/{teamId}/info").permitAll()
                .requestMatchers("/api/v1/merchant/**").hasAuthority(MemberRole.MERCHANT.getText())
                .anyRequest().authenticated()
            )
            .oauth2Login(customConfigurer -> customConfigurer
                .userInfoEndpoint(endpointConfig -> endpointConfig.userService(customOAuth2UserService))
                .successHandler(customOAuth2LoginSuccessHandler))
            .headers((headerConfig) ->
                headerConfig.frameOptions((HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
            )
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlerFilter, JwtTokenFilter.class)
            .exceptionHandling((exceptionHandling) -> exceptionHandling
                .accessDeniedHandler(accessDeniedHandler()));

        return httpSecurity.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            ApiResponse apiResponse = ApiResponse.of(
                HttpStatus.FORBIDDEN,
                "권한이 없습니다.",
                null
            );

            String jsonResponse = objectMapper.writeValueAsString(apiResponse);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse);
        };
    }
}
