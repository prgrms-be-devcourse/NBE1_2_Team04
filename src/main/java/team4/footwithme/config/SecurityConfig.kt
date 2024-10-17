package team4.footwithme.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.UserInfoEndpointConfig
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.client.RestTemplate
import team4.footwithme.global.api.ApiResponse
import team4.footwithme.global.exception.ExceptionHandlerFilter
import team4.footwithme.member.domain.MemberRole
import team4.footwithme.member.jwt.JwtTokenFilter
import team4.footwithme.member.oauth2.CustomOAuth2LoginSuccessHandler
import team4.footwithme.member.oauth2.CustomOAuth2UserService

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtTokenFilter: JwtTokenFilter,
    private val exceptionHandlerFilter: ExceptionHandlerFilter,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val customOAuth2LoginSuccessHandler: CustomOAuth2LoginSuccessHandler
) {
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .formLogin { obj: FormLoginConfigurer<HttpSecurity> -> obj.disable() }
            .sessionManagement { sessionManagement: SessionManagementConfigurer<HttpSecurity?> ->
                sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests(
                Customizer<AuthorizationManagerRequestMatcherRegistry> { authorizeHttpRequests: AuthorizationManagerRequestMatcherRegistry ->
                    authorizeHttpRequests
                        .requestMatchers("/api/v1/members/join", "/api/v1/members/login").permitAll()
                        .requestMatchers("/api/v1/court/**", "/api/v1/stadium/**").permitAll()
                        .requestMatchers("/api/v1/team/{teamId}/info").permitAll()
                        .requestMatchers("/ws").permitAll() // 이거 추가
                        .requestMatchers("/api/v1/merchant/**").hasAuthority(MemberRole.MERCHANT.text)
                        .anyRequest().authenticated()
                }
            )
            .oauth2Login { customConfigurer: OAuth2LoginConfigurer<HttpSecurity?> ->
                customConfigurer
                    .userInfoEndpoint(Customizer { endpointConfig: UserInfoEndpointConfig ->
                        endpointConfig.userService(
                            customOAuth2UserService
                        )
                    })
                    .successHandler(customOAuth2LoginSuccessHandler)
            }
            .headers { headerConfig: HeadersConfigurer<HttpSecurity?> ->
                headerConfig.frameOptions(
                    (Customizer<FrameOptionsConfig> { FrameOptionsConfig.sameOrigin() })
                )
            }
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(exceptionHandlerFilter, JwtTokenFilter::class.java)
            .exceptionHandling { exceptionHandling: ExceptionHandlingConfigurer<HttpSecurity?> ->
                exceptionHandling
                    .accessDeniedHandler(accessDeniedHandler())
            }

        return httpSecurity.build()
    }

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler {
        return AccessDeniedHandler { request: HttpServletRequest?, response: HttpServletResponse, accessDeniedException: AccessDeniedException? ->
            val objectMapper = ObjectMapper()
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.contentType = "application/json"
            val apiResponse: ApiResponse<*> = ApiResponse.Companion.of<Any?>(
                HttpStatus.FORBIDDEN,
                "권한이 없습니다.",
                null
            )

            val jsonResponse = objectMapper.writeValueAsString(apiResponse)
            response.characterEncoding = "UTF-8"
            response.writer.write(jsonResponse)
        }
    }
}
