package _712.final_project_712.service;

import _712.final_project_712.model.User;
import _712.final_project_712.mapper.UserMapper;
import com.mybatisflex.core.query.QueryChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PasswordUpdateTest {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        // 准备测试数据
        testUser = new User();
        testUser.setId(100L);  // 手动设置ID
        testUser.setName("张三");
        testUser.setPassword("123456");
        testUser.setEmail("zhangsan@test.com");
        
        userMapper.insert(testUser);
    }
    
    @Test
    void testUpdatePasswordSuccess() {
        // 测试成功修改密码
        boolean result = userService.updatePassword(
            100L,
            "123456",    // 原密码
            "888888"     // 新密码
        );
        
        assertTrue(result);
        
        // 验证密码是否修改成功
        User updatedUser = QueryChain.of(User.class)
                .where(User::getId).eq(100L)
                .one();
        assertEquals("888888", updatedUser.getPassword());
    }
    
    @Test
    void testUpdatePasswordWithWrongOldPassword() {
        // 测试原密码错误的情况
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(
                100L,
                "wrong_password",  // 错误的原密码
                "888888"
            );
        });
        
        assertEquals("原密码错误", exception.getMessage());
    }
    
    @Test
    void testUpdatePasswordWithNonExistentUser() {
        // 测试用户不存在的情况
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(
                999L,      // 不存在的用户ID
                "123456",
                "888888"
            );
        });
        
        assertEquals("用户不存在", exception.getMessage());
    }
} 