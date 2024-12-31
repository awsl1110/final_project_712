package _712.final_project_712.service;

import _712.final_project_712.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // 确保测试后数据回滚，不影响数据库
public class UserLoginTest {

    @Autowired
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 创建测试用户
        testUser = new User();
        testUser.setName("testUser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        
        // 注册测试用户
        userService.register(testUser);
    }

    @Test
    void testSuccessfulLogin() {
        // 测试正确的用户名和密码
        String token = userService.login("testUser", "password123");
        
        // 验证返回的token不为空
        assertNotNull(token);
        assertTrue(token.length() > 0);
        System.out.println("登录成功，获取到token: " + token);
    }

    @Test
    void testLoginWithWrongPassword() {
        // 测试错误的密码
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.login("testUser", "wrongpassword");
        });
        assertEquals("密码错误", exception.getMessage());
        System.out.println("密码错误测试通过");
    }

    @Test
    void testLoginWithNonexistentUser() {
        // 测试不存在的用户
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.login("nonexistentUser", "password123");
        });
        assertEquals("用户不存在", exception.getMessage());
        System.out.println("用户不存在测试通过");
    }

    @Test
    void testLoginWithEmptyCredentials() {
        // 测试空用户名
        Exception exception1 = assertThrows(RuntimeException.class, () -> {
            userService.login("", "password123");
        });
        assertEquals("用户名不能为空", exception1.getMessage());
        System.out.println("空用户名测试通过");

        // 测试空密码
        Exception exception2 = assertThrows(RuntimeException.class, () -> {
            userService.login("testUser", "");
        });
        assertEquals("密码不能为空", exception2.getMessage());
        System.out.println("空密码测试通过");

        // 测试null值
        Exception exception3 = assertThrows(RuntimeException.class, () -> {
            userService.login(null, null);
        });
        assertEquals("用户名不能为空", exception3.getMessage());
        System.out.println("null值测试通过");
    }
} 