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

@Tag(name = "注册接口", description = "用户注册相关接口")
@RestController
@RequestMapping("/user")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<?> register(
            @Parameter(description = "用户ID") @RequestParam Long id,
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "密码") @RequestParam String password,
            @Parameter(description = "邮箱") @RequestParam String email) {
        try {
            // 参数校验
            if (id == null) {
                return Result.error("用户ID不能为空");
            }
            if (!StringUtils.hasText(username)) {
                return Result.error("用户名不能为空");
            }
            if (!StringUtils.hasText(password)) {
                return Result.error("密码不能为空");
            }
            if (!StringUtils.hasText(email)) {
                return Result.error("邮箱不能为空");
            }
            
            // 创建用户对象
            User user = new User();
            user.setId(id);
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