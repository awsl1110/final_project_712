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
    void testValidRegister() {
        // 创建有效的测试用户
        User user = new User();
        user.setId(1001L);
        user.setName("validUser123");  // 有效的用户名
        user.setPassword("Password123"); // 有效的密码
        user.setEmail("test@example.com"); // 有效的邮箱
        
        // 测试注册
        boolean result = userService.register(user);
        
        // 验证结果
        assertTrue(result, "有效用户注册应该成功");
    }

    @Test
    void testInvalidUsername() {
        // 创建用户名无效的测试用户
        User user = new User();
        user.setId(1002L);
        user.setName("u"); // 无效的用户名（太短）
        user.setPassword("Password123");
        user.setEmail("test@example.com");
        
        // 测试注册
        assertThrows(RuntimeException.class, () -> userService.register(user),
                "无效用户名应该抛出异常");
    }

    @Test
    void testInvalidPassword() {
        // 创建密码无效的测试用户
        User user = new User();
        user.setId(1003L);
        user.setName("validUser123");
        user.setPassword("123"); // 无效的密码（太短且没有字母）
        user.setEmail("test@example.com");
        
        // 测试注册
        assertThrows(RuntimeException.class, () -> userService.register(user),
                "无效密码应该抛出异常");
    }

    @Test
    void testInvalidEmail() {
        // 创建邮箱无效的测试用户
        User user = new User();
        user.setId(1004L);
        user.setName("validUser123");
        user.setPassword("Password123");
        user.setEmail("invalid-email"); // 无效的邮箱
        
        // 测试注册
        assertThrows(RuntimeException.class, () -> userService.register(user),
                "无效邮箱应该抛出异常");
    }
} 