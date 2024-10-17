package team4.footwithme.config.websocket

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    private val stompHandler: StompHandler? = null

    // 웹소켓 연결되기 전 인터셉트
    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(stompHandler)
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/sub")
        registry.setApplicationDestinationPrefixes("/pub")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/ws") // 엔드포인트: /ws
            .setAllowedOriginPatterns("*")
            .setAllowedOrigins("*")
        //                .withSockJS();
    }
}
