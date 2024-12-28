package _712.final_project_712.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Tag(name = "验证码接口", description = "包含验证码的生成和验证功能")
@RestController
@RequestMapping(value = "/kaptcha", produces = MediaType.APPLICATION_JSON_VALUE)
public class KaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Operation(
        summary = "获取验证码",
        description = "生成图片验证码，并将验证码保存到Redis中，有效期5分钟。验证码标识在响应头的Kaptcha-Key中。"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "成功生成验证码图片",
            content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)
        )
    })
    @GetMapping(value = "/code", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getKaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        // 生成验证码文本
        String text = defaultKaptcha.createText();
        
        // 生成验证码图片
        BufferedImage image = defaultKaptcha.createImage(text);
        
        // 生成唯一标识
        String uuid = UUID.randomUUID().toString();
        
        // 将验证码存入Redis，设置5分钟过期
        redisTemplate.opsForValue().set("kaptcha:" + uuid, text, 5, TimeUnit.MINUTES);
        
        // 同时也存入Session，用于登录验证
        session.setAttribute("captcha", text);
        System.out.println("Generated Captcha: " + text); // 添加日志
        
        // 将uuid写入响应头
        response.setHeader("Kaptcha-Key", uuid);
        
        // 设置响应类型
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        
        // 输出图片
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Operation(
        summary = "验证验证码",
        description = "验证用户输入的验证码是否正确。验证完成后，无论成功与否，该验证码都将失效。"
    )
    @Parameters({
        @Parameter(name = "key", description = "验证码标识（从获取验证码接口响应头Kaptcha-Key中获取）", required = true),
        @Parameter(name = "code", description = "用户输入的验证码", required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "验证结果",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = String.class)
            )
        )
    })
    @GetMapping("/verify")
    public ResponseEntity<String> verifyKaptcha(
            @RequestParam String key,
            @RequestParam String code) {
        String redisKey = "kaptcha:" + key;
        String correctCode = redisTemplate.opsForValue().get(redisKey);
        
        if (correctCode == null) {
            return ResponseEntity.ok("验证码已过期");
        }
        
        // 验证完成后删除验证码
        redisTemplate.delete(redisKey);
        
        if (correctCode.equalsIgnoreCase(code)) {
            return ResponseEntity.ok("验证成功");
        } else {
            return ResponseEntity.ok("验证码错误");
        }
    }
} 