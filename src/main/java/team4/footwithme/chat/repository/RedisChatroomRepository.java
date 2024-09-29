package team4.footwithme.chat.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;
import team4.footwithme.chat.domain.Chatroom;
import team4.footwithme.chat.service.RedisSubscriber;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisChatroomRepository {
    private final ChatroomRepository chatroomRepository;

    private final RedisMessageListenerContainer container;
    private final RedisSubscriber redisSubscriber;

    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, Long, Chatroom> opsHashChatRoom;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;

    private static final String CHAT_ROOMS = "CHAT_ROOM";

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();

        // 서버 재시작 시 embeddedRedis 초기화되기 때문
        chatroomRepository.findAll().forEach(chatroom -> {
            if(!topics.containsKey(chatroom.getChatroomId().toString())){
                createChatRoom(chatroom);
                enterChatRoom(chatroom.getChatroomId().toString());
            }
        });
    }

    public Chatroom findChatroomFromRedis(String chatroomId) {
        return opsHashChatRoom.get(CHAT_ROOMS, chatroomId);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    public void createChatRoom(Chatroom chatroom) {
        opsHashChatRoom.put(CHAT_ROOMS, chatroom.getChatroomId(), chatroom);
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String chatroomId) {
        ChannelTopic topic = topics.get(chatroomId);
        if (topic == null)
            topic = new ChannelTopic(chatroomId);
        container.addMessageListener(redisSubscriber, topic);
        topics.put(chatroomId, topic);
    }

    public void leaveChatRoom(String chatroomId) {
        ChannelTopic topic = topics.get(chatroomId);

        container.removeMessageListener(redisSubscriber, topic);
        topics.remove(chatroomId);
    }

    public ChannelTopic getTopic(String chatroomId) {
        return topics.get(chatroomId);
    }
}
