package team4.footwithme.chat.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import team4.footwithme.chat.domain.Chatroom;

@Repository
public class RedisChatroomRepository implements CommandLineRunner {
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final ChatroomRepository chatroomRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, Chatroom> opsHashChatRoom;

    public RedisChatroomRepository(ChatroomRepository chatroomRepository, RedisTemplate<String, Object> redisTemplate) {
        this.chatroomRepository = chatroomRepository;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void start() {
        opsHashChatRoom = redisTemplate.opsForHash();
    }

    @Override
    public void run(String... args) {
        init();
    }

    private void init() {
        // 서버 재시작 시 embeddedRedis 초기화되기 때문
        chatroomRepository.findAll().forEach(chatroom -> {
            if (findChatroomFromRedis(chatroom.getChatroomId()) == null) {
                createChatRoom(chatroom);
            }
        });
    }

    public Chatroom findChatroomFromRedis(Long chatroomId) {
        return opsHashChatRoom.get(CHAT_ROOMS, chatroomId);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    public void createChatRoom(Chatroom chatroom) {
        opsHashChatRoom.put(CHAT_ROOMS, chatroom.getChatroomId(), chatroom);
    }

    public void deleteChatroomFromRedis(Long chatroomId) {
        opsHashChatRoom.delete(CHAT_ROOMS, chatroomId);
    }
}
