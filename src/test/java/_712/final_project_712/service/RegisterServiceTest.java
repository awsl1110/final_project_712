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
        user.setName("validUser123");  // 有效的用户名
        user.setPassword("Password123"); // 有效的密码
        user.setEmail("test@example.com"); // 有效的邮箱
        
        // 测试注册
        boolean result = userService.register(user);
        
        // 验证结果
        assertTrue(result, "有效用户注册应该成功");
        assertNotNull(user.getId(), "用户ID应该被自动生成");
        assertTrue(user.getId() > 0, "用户ID应该是正数");
    }

    @Test
    void testInvalidUsername() {
        // 创建用户名无效的测试用户
        User user = new User();
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
        user.setName("validUser123");
        user.setPassword("Password123");
        user.setEmail("invalid-email"); // 无效的邮箱
        
        // 测试注册
        assertThrows(RuntimeException.class, () -> userService.register(user),
                "无效邮箱应该抛出异常");
    }

    @Test
    void testDuplicateUsername() {
        // 创建第一个用户
        User user1 = new User();
        user1.setName("testUser123");
        user1.setPassword("Password123");
        user1.setEmail("test1@example.com");
        
        // 注册第一个用户
        boolean result1 = userService.register(user1);
        assertTrue(result1, "第一个用户注册应该成功");
        
        // 创建第二个用户，使用相同的用户名
        User user2 = new User();
        user2.setName("testUser123"); // 相同的用户名
        user2.setPassword("Password456");
        user2.setEmail("test2@example.com");
        
        // 尝试注册第二个用户，应该抛出异常
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.register(user2);
        });
        
        assertEquals("用户名已存在", exception.getMessage());
    }
} 