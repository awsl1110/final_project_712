package _712.final_project_712.config;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisConnection() {
        // 测试基本的字符串操作
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        
        // 测试存储
        String key = "test:key";
        String value = "Hello Redis!";
        ops.set(key, value);
        
        // 测试获取
        Object retrievedValue = ops.get(key);
        assertEquals(value, retrievedValue);
        
        // 测试删除
        redisTemplate.delete(key);
        assertNull(ops.get(key));
        
        // 测试存储对象
        TestUser user = new TestUser("张三", 25);
        String userKey = "test:user";
        ops.set(userKey, user);
        
        // 测试获取对象
        TestUser retrievedUser = (TestUser) ops.get(userKey);
        assertNotNull(retrievedUser);
        assertEquals("张三", retrievedUser.getName());
        assertEquals(25, retrievedUser.getAge());
        
        // 清理测试数据
        redisTemplate.delete(userKey);
    }
}

// 用于测试的简单用户类
@Setter
@Getter
class TestUser {
    // getter和setter方法
    private String name;
    private int age;

    // 必须有无参构造函数，因为JSON序列化需要
    public TestUser() {
    }

    public TestUser(String name, int age) {
        this.name = name;
        this.age = age;
    }

}