package _712.final_project_712.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmailCaptchaControllerTest {

    @InjectMocks
    private EmailCaptchaController emailCaptchaController;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private final String testEmail = "test@example.com";
    private final String fromEmail = "noreply@example.com";

    @BeforeEach
    void setUp() {
        // 设置fromEmail
        ReflectionTestUtils.setField(emailCaptchaController, "fromEmail", fromEmail);
        
        // 设置Redis模拟
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void sendEmailCaptcha_Success() {
        // 准备测试数据
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        doNothing().when(valueOperations).set(anyString(), anyString(), anyLong(), any());

        // 执行测试
        ResponseEntity<String> response = emailCaptchaController.sendEmailCaptcha(testEmail);

        // 验证结果
        assertEquals(200, response.getStatusCode().value());
        assertEquals("验证码已发送", response.getBody());

        // 验证邮件发送
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        
        // 验证Redis存储
        verify(valueOperations, times(1)).set(
            matches("email:captcha:" + testEmail),
            matches("\\d{6}"),
            eq(5L),
            any()
        );
    }

    @Test
    void sendEmailCaptcha_MailSenderException() {
        // 模拟邮件发送异常
        doThrow(new RuntimeException("邮件发送失败")).when(mailSender).send(any(SimpleMailMessage.class));

        // 执行测试
        ResponseEntity<String> response = emailCaptchaController.sendEmailCaptcha(testEmail);

        // 验证结果
        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().contains("验证码发送失败"));
        
        // 验证没有进行Redis存储
        verify(valueOperations, never()).set(anyString(), anyString(), anyLong(), any());
    }

    @Test
    void sendEmailCaptcha_InvalidEmail() {
        // 执行测试 - 使用无效的邮箱地址
        ResponseEntity<String> response = emailCaptchaController.sendEmailCaptcha("");

        // 验证结果
        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().contains("验证码发送失败"));
        
        // 验证没有发送邮件和存储Redis
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
        verify(valueOperations, never()).set(anyString(), anyString(), anyLong(), any());
    }
} 