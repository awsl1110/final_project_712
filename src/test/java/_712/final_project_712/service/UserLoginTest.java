package _712.final_project_712.service;

import _712.final_project_712.model.LoginResult;
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
        // 准备测试数据
        testUser = new User();
        testUser.setId(1000L);
        testUser.setName("testUser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        
        // 确保测试用户存在于数据库中
        userService.register(testUser);
        System.out.println("测试用户创建成功: " + testUser);
    }

    @Test
    void testSuccessfulLogin() {
        // 测试正确的用户名和密码
        LoginResult result = userService.login("testUser", "password123");
        System.out.println("成功登录测试结果: " + result);
        
        assertNotNull(result, "登录结果不应为空");
        assertTrue(result.isSuccess(), "登录应该成功");
        assertNotNull(result.getToken(), "登录成功应返回token");
        assertEquals("登录成功", result.getMessage());
    }

    @Test
    void testLoginWithWrongPassword() {
        // 测试错误的密码
        LoginResult result = userService.login("testUser", "wrongPassword");
        System.out.println("错误密码测试结果: " + result);
        
        assertNotNull(result, "登录结果不应为空");
        assertFalse(result.isSuccess());
        assertNull(result.getToken());
        assertEquals("密码错误", result.getMessage());
    }

    @Test
    void testLoginWithNonexistentUser() {
        // 测试不存在的用户
        LoginResult result = userService.login("nonexistentUser", "password123");
        System.out.println("不存在用户测试结果: " + result);
        
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getToken());
        assertEquals("用户不存在", result.getMessage());
    }

    @Test
    void testLoginWithInvalidInput() {
        // 测试空用户名
        LoginResult result1 = userService.login("", "password123");
        System.out.println("空用户名测试结果: " + result1);
        assertEquals("用户名不能为空", result1.getMessage());

        // 测试空密码
        LoginResult result2 = userService.login("testUser", "");
        System.out.println("空密码测试结果: " + result2);
        assertEquals("密码不能为空", result2.getMessage());

        // 测试null值
        LoginResult result3 = userService.login(null, null);
        System.out.println("null值测试结果: " + result3);
        assertEquals("用户名不能为空", result3.getMessage());
    }
} 