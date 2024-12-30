package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.service.UserService;
import _712.final_project_712.util.JwtUtil;
import _712.final_project_712.dto.UserProfileUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    
    @Operation(summary = "更新用户信息", description = "更新用户的邮箱和地址信息")
    @PutMapping("/profile")
    public Result<?> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UserProfileUpdateDTO updateDTO
    ) {
        // 验证token
        if (!StringUtils.hasText(token)) {
            return Result.error(401, "token不能为空");
        }
        
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "token已过期或无效");
        }
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        try {
            userService.updateUserProfile(userId, updateDTO);
            return Result.success("用户信息更新成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
} 