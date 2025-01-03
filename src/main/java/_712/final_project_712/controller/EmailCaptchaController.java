package _712.final_project_712.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Tag(name = "邮箱验证码", description = "邮箱验证码的发送接口")
@RestController
@RequestMapping("/api/email")
public class EmailCaptchaController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/captcha/send")
    public ResponseEntity<String> sendEmailCaptcha(
            @Parameter(description = "邮箱地址") @RequestParam String email) {
        try {
            // 验证邮箱格式
            if (email == null || email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return ResponseEntity.badRequest().body("验证码发送失败：无效的邮箱地址");
            }

            // 生成6位随机验证码
            String captcha = generateCaptcha();
            
            // 发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("验证码");
            message.setText("您的验证码是：" + captcha + "，5分钟内有效。");
            mailSender.send(message);
            
            // 将验证码存入Redis，设置5分钟过期
            redisTemplate.opsForValue().set(
                "email:captcha:" + email,
                captcha,
                5,
                TimeUnit.MINUTES
            );
            
            return ResponseEntity.ok("验证码已发送");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("验证码发送失败：" + e.getMessage());
        }
    }

    // 生成6位随机验证码
    private String generateCaptcha() {
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            captcha.append(random.nextInt(10));
        }
        return captcha.toString();
    }
} 