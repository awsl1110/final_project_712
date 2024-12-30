package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.UpdateProfileRequest;
import _712.final_project_712.model.dto.UserInfoResponse;
import _712.final_project_712.service.UserProfileService;
import _712.final_project_712.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户信息管理", description = "用户信息相关接口")
@RestController
@RequestMapping("/user/profile")
public class UserProfileController {
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Operation(summary = "更新用户信息", description = "更新用户的邮箱和地址信息")
    @PutMapping
    public Result<?> updateProfile(
            @Parameter(description = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateProfileRequest request
    ) {
        if (!StringUtils.hasText(token)) {
            return Result.error(401, "token不能为空");
        }
        
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "token已过期或无效");
        }
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        try {
            userProfileService.updateUserProfile(userId, request);
            return Result.success("用户信息更新成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "获取用户信息", description = "获取当前用户的所有信息")
    @GetMapping
    public Result<?> getUserInfo(
            @Parameter(description = "用户token", required = true)
            @RequestHeader("Authorization") String token
    ) {
        if (!StringUtils.hasText(token)) {
            return Result.error(401, "token不能为空");
        }
        
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "token已过期或无效");
        }
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        try {
            UserInfoResponse userInfo = userProfileService.getUserInfo(userId);
            return Result.success(userInfo);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "删除收货地址")
    @DeleteMapping("/address/{addressId}")
    public Result<?> deleteAddress(
            @Parameter(description = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @PathVariable("addressId") Long addressId
    ) {
        if (!StringUtils.hasText(token)) {
            return Result.error(401, "token不能为空");
        }
        
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "token已过期或无效");
        }
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        try {
            userProfileService.deleteAddress(userId, addressId);
            return Result.success("地址删除成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
} 