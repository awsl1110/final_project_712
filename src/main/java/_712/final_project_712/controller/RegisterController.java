package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.User;
import _712.final_project_712.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Tag(name = "注册接口", description = "用户注册相关接口")
@RestController
@RequestMapping("/user")
public class RegisterController {

    @Autowired
    private UserService userService;

    // 邮箱格式验证正则表达式
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    // 用户名格式验证正则表达式 (4-16位字母、数字、下划线)
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_]{4,16}$");
    
    // 密码格式验证正则表达式 (8-20位，必须包含字母和数字)
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$");

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<?> register(
            @Parameter(description = "用户名 (4-16位字母、数字、下划线)") @RequestParam String username,
            @Parameter(description = "密码 (8-20位，必须包含字母和数字)") @RequestParam String password,
            @Parameter(description = "邮箱") @RequestParam String email) {
        try {
            // 用户名验证
            if (!StringUtils.hasText(username)) {
                return Result.error("用户名不能为空");
            }
            if (!USERNAME_PATTERN.matcher(username).matches()) {
                return Result.error("用户名必须是4-16位字母、数字或下划线");
            }

            // 密码验证
            if (!StringUtils.hasText(password)) {
                return Result.error("密码不能为空");
            }
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                return Result.error("密码必须是8-20位，且包含字母和数字");
            }

            // 邮箱验证
            if (!StringUtils.hasText(email)) {
                return Result.error("邮箱不能为空");
            }
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                return Result.error("邮箱格式不正确");
            }
            
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
} 