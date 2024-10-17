package team4.footwithme.chat.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import team4.footwithme.chat.domain.Chat;

@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    public RedisPublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic channelTopic) {
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }

    public void publish(Chat chat) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), chat);
    }
}
