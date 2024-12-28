package _712.final_project_712.service;

import _712.final_project_712.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RegisterServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testRegister() {
        // 创建测试用户
        User user = new User();
        user.setId(1001L);
        user.setName("testUser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        
        // 测试注册
        boolean result = userService.register(user);
        
        // 验证结果
        assertTrue(result, "用户注册应该成功");
    }
} 