package _712.final_project_712.service;

import _712.final_project_712.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getAllUsers() {
        List<User> users = userService.getAllUsers();
        // 确保返回的列表不为null
        assertNotNull(users);
        // 打印用户列表
        users.forEach(user -> {
            System.out.println("用户ID: " + user.getId());
            System.out.println("用户名: " + user.getName());
            System.out.println("----------------------");
        });
    }
} 