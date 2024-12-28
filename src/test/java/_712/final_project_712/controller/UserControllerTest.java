package _712.final_project_712.controller;

import _712.final_project_712.model.User;
import _712.final_project_712.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testRegisterSuccess() throws Exception {
        // 配置mock服务的行为
        when(userService.register(any(User.class))).thenReturn(true);

        // 执行注册请求并验证结果
        mockMvc.perform(post("/api/user/register")
                .param("id", "1")
                .param("name", "testUser")
                .param("password", "testPass123")
                .param("email", "test@example.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("注册成功"));
    }

    @Test
    public void testRegisterWithExistingId() throws Exception {
        // 模拟ID已存在的情况
        doThrow(new RuntimeException("用户ID已存在")).when(userService).register(any(User.class));

        mockMvc.perform(post("/api/user/register")
                .param("id", "1")
                .param("name", "testUser")
                .param("password", "testPass123")
                .param("email", "test@example.com"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("注册失败：用户ID已存在"));
    }

    @Test
    public void testRegisterWithInvalidEmail() throws Exception {
        // 模拟无效邮箱的情况
        doThrow(new RuntimeException("无效的邮箱格式")).when(userService).register(any(User.class));

        mockMvc.perform(post("/api/user/register")
                .param("id", "1")
                .param("name", "testUser")
                .param("password", "testPass123")
                .param("email", "invalid-email"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("注册失败：无效的邮箱格式"));
    }

    @Test
    public void testRegisterWithMissingParameters() throws Exception {
        // 测试缺少必要参数的情况
        mockMvc.perform(post("/api/user/register")
                .param("id", "1")
                .param("name", "testUser")
                // 故意不提供password和email参数
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterWithInvalidId() throws Exception {
        // 测试无效ID格式的情况
        mockMvc.perform(post("/api/user/register")
                .param("id", "invalid-id")
                .param("name", "testUser")
                .param("password", "testPass123")
                .param("email", "test@example.com"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
} 