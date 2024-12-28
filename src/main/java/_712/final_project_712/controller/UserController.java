package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.service.UserService;
import _712.final_project_712.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Operation(summary = "修改密码")
    @PostMapping("/password")
    public Result<?> updatePassword(
            @RequestHeader("Authorization") String token,
            @Parameter(description = "原密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword
    ) {
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
            
            boolean result = userService.updatePassword(userId, oldPassword, newPassword);
            return Result.success("密码修改成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
} 