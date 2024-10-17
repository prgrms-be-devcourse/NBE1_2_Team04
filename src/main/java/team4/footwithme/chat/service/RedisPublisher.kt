package team4.footwithme.chat.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service
import team4.footwithme.chat.domain.Chat

@Service
class RedisPublisher(private val redisTemplate: RedisTemplate<String, Any>, private val channelTopic: ChannelTopic) {
    fun publish(chat: Chat?) {
        redisTemplate.convertAndSend(channelTopic.topic, chat)
    }
}
