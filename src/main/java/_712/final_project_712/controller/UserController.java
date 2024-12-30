package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.User;
import _712.final_project_712.service.UserService;
import _712.final_project_712.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Operation(summary = "用户登录", description = "通过用户名、密码和验证码进行登录")
    @PostMapping("/login")
    public Result<?> login(
            @Parameter(description = "用户名", required = true) @RequestParam String username,
            @Parameter(description = "密码", required = true) @RequestParam String password,
            @Parameter(description = "验证码", required = true) @RequestParam String captcha,
            HttpSession session) {
        
        try {
            // 获取session中的验证码
            String realCaptcha = (String) session.getAttribute("captcha");
            
            // 验证码为空或已过期
            if (realCaptcha == null) {
                return Result.error("验证码已过期，请重新获取");
            }
            
            // 验证码不匹配（忽略大小写）
            if (!realCaptcha.equalsIgnoreCase(captcha)) {
                return Result.error("验证码错误");
            }
            
            // 验证码正确，清除session中的验证码
            session.removeAttribute("captcha");
            
            // 调用登录服务
            String token = userService.login(username, password);
            return Result.success(token);
        } catch (Exception e) {
            return Result.error("系统错误：" + e.getMessage());
        }
    }
    
    @Operation(summary = "用户注册", description = "注册新用户")
    @PostMapping("/register")
    public Result<?> register(
            @Parameter(description = "用户名", required = true) @RequestParam String username,
            @Parameter(description = "密码", required = true) @RequestParam String password,
            @Parameter(description = "邮箱", required = true) @RequestParam String email) {
        try {
            // 创建用户对象
            User user = new User();
            user.setName(username);
            user.setPassword(password);
            user.setEmail(email);
            
            // 调用注册服务
            boolean success = userService.register(user);
            
            if (success) {
                return Result.success("注册成功");
            } else {
                return Result.error("注册失败");
            }
        } catch (Exception e) {
            return Result.error("注册失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "修改密码")
    @PostMapping("/update_password")
    public Result<?> updatePassword(
            @RequestHeader("Authorization") String token,
            @Parameter(description = "原密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword,
            @Parameter(description = "邮箱验证码") @RequestParam String emailCode) {
        try {
            // 验证token是否为空
            if (token == null || token.isEmpty()) {
                return Result.error(401, "token不能为空");
            }
            
            // 验证token是否有效
            if (!jwtUtil.validateToken(token)) {
                return Result.error(401, "token已过期或无效");
            }
            
            // 从token中获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            // 获取用户邮箱
            String email = userService.getUserEmail(userId);
            if (email == null) {
                return Result.error("用户邮箱不存在");
            }
            
            // 验证邮箱验证码
            String key = "email:captcha:" + email;
            String correctCode = redisTemplate.opsForValue().get(key);
            System.out.println("Email: " + email);
            System.out.println("Redis key: " + key);
            System.out.println("Stored code: " + correctCode);
            System.out.println("Input code: " + emailCode);
            
            if (correctCode == null) {
                return Result.error("验证码已过期");
            }
            
            if (!correctCode.equals(emailCode)) {
                return Result.error("验证码错误");
            }
            
            // 验证码正确，删除验证码
            redisTemplate.delete(key);
            
            // 修改密码
            boolean result = userService.updatePassword(userId, oldPassword, newPassword);
            return Result.success("密码修改成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
} 