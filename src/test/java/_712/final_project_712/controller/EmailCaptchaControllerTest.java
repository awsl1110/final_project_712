package _712.final_project_712.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailCaptchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void setUp() {
        // 配置JavaMailSender的Mock行为
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        
        // 清理所有与验证码相关的键
        Set<String> keys = redisTemplate.keys("email:captcha:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Test
    void testSendEmailCaptcha() throws Exception {
        // 测试发送验证码
        String testEmail = "test@example.com";
        
        MvcResult result = mockMvc.perform(post("/api/email/captcha/send")
                .param("email", testEmail))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // 验证响应内容
        String content = result.getResponse().getContentAsString();
        assertEquals("验证码已发送", content);

        // 验证Redis中是否存储了验证码
        String key = "email:captcha:" + testEmail;
        String captcha = redisTemplate.opsForValue().get(key);
        assertNotNull(captcha);
        assertEquals(6, captcha.length());
    }

    @Test
    void testVerifyEmailCaptcha() throws Exception {
        // 准备测试数据
        String testEmail = "test@example.com";
        String testCaptcha = "123456";
        String key = "email:captcha:" + testEmail;
        
        // 手动在Redis中设置验证码
        redisTemplate.opsForValue().set(key, testCaptcha);

        // 测试验证正确的验证码
        MvcResult correctResult = mockMvc.perform(post("/api/email/captcha/verify")
                .param("email", testEmail)
                .param("captcha", testCaptcha))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("验证成功", correctResult.getResponse().getContentAsString());

        // 验证码应该被删除
        assertNull(redisTemplate.opsForValue().get(key));

        // 测试验证错误的验证码
        redisTemplate.opsForValue().set(key, testCaptcha);
        MvcResult wrongResult = mockMvc.perform(post("/api/email/captcha/verify")
                .param("email", testEmail)
                .param("captcha", "wrong"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("验证码错误", wrongResult.getResponse().getContentAsString());
    }

    @Test
    void testVerifyExpiredCaptcha() throws Exception {
        String testEmail = "test@example.com";
        String key = "email:captcha:" + testEmail;
        
        // 确保Redis中没有验证码
        redisTemplate.delete(key);
        
        // 不设置验证码，模拟过期情况
        MvcResult result = mockMvc.perform(post("/api/email/captcha/verify")
                .param("email", testEmail)
                .param("captcha", "123456"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("验证码已过期", result.getResponse().getContentAsString());
    }
} 