package team4.footwithme.config.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Objects;


/**
 * 로컬 환경일경우 내장 레디스가 실행됩니다.
 */
@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.data.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() {
        if(isArmMac()) {
            redisServer = new RedisServer(getRedisServerExecutable(), redisPort);
        } else {
            redisServer = new RedisServer(redisPort);
            redisServer.start();
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    private File getRedisServerExecutable(){
        try {
            return new File("src/main/resources/binary/redis/redis-server-6.2.5-mac-arm64");
        } catch (Exception e) {
            throw new NoSuchElementException("Redis Server Executable File Not Found");
        }
    }
    private boolean isArmMac() {
        return Objects.equals(System.getProperty("os.arch"), "aarch64")
            && Objects.equals(System.getProperty("os.name"), "Mac OS X");
    }
}
