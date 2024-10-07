package team4.footwithme.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Profile("dev | test")
@Configuration
public class ExternalRedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHostName;
    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Profile("dev | test")
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHostName);
        redisStandaloneConfiguration.setPort(redisPort);

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}
