package _712.final_project_712.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.time.Duration;

@EnableCaching
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisConfig {
    
    private String host;
    private int port;
    private String password;
    private int database;
    private long timeout;
    private Lettuce lettuce = new Lettuce();

    public static class Lettuce {
        private Pool pool = new Pool();
        public Pool getPool() {
            return pool;
        }
        public void setPool(Pool pool) {
            this.pool = pool;
        }
    }

    public static class Pool {
        private int maxActive;
        private int maxIdle;
        private int minIdle;
        private Duration maxWait;

        public int getMaxActive() {
            return maxActive;
        }
        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }
        public int getMaxIdle() {
            return maxIdle;
        }
        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }
        public int getMinIdle() {
            return minIdle;
        }
        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }
        public Duration getMaxWait() {
            return maxWait;
        }
        public void setMaxWait(Duration maxWait) {
            this.maxWait = maxWait;
        }
    }

    public void setHost(String host) {
        this.host = host;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setDatabase(int database) {
        this.database = database;
    }
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    public void setLettuce(Lettuce lettuce) {
        this.lettuce = lettuce;
    }
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(password);
        config.setDatabase(database);

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(lettuce.getPool().getMaxActive());
        poolConfig.setMaxIdle(lettuce.getPool().getMaxIdle());
        poolConfig.setMinIdle(lettuce.getPool().getMinIdle());
        poolConfig.setMaxWait(lettuce.getPool().getMaxWait());

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .poolConfig(poolConfig)
                .build();

        return new LettuceConnectionFactory(config, clientConfig);
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, 
                                        ObjectMapper.DefaultTyping.NON_FINAL);
        
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = 
            new GenericJackson2JsonRedisSerializer(objectMapper);
        
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }
} 