package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.UserDTO;
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
    
    @Operation(summary = "修改用户信息", description = "修改用户的基本信息")
    @PutMapping("/info")
    public Result<?> updateUserInfo(
            @Parameter(description = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @RequestBody UserDTO.UpdateUserRequest request
    ) {
        if (!StringUtils.hasText(token)) {
            return Result.error(401, "token不能为空");
        }
        
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "token已过期或无效");
        }
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        try {
            userProfileService.updateUserInfo(userId, request);
            return Result.success("用户信息修改成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "新增收货地址", description = "新增用户收货地址")
    @PostMapping("/address")
    public Result<?> addAddress(
            @Parameter(description = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @RequestBody UserDTO.AddAddressRequest request
    ) {
        if (!StringUtils.hasText(token)) {
            return Result.error(401, "token不能为空");
        }
        
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "token已过期或无效");
        }
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        try {
            userProfileService.addAddress(userId, request);
            return Result.success("收货地址添加成功");
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
            UserDTO.UserInfoResponse userInfo = userProfileService.getUserInfo(userId);
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
    
    @Operation(summary = "修改收货地址", description = "修改用户的收货地址")
    @PutMapping("/address/{addressId}")
    public Result<?> updateAddress(
            @Parameter(description = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "地址ID", required = true)
            @PathVariable Long addressId,
            @RequestBody UserDTO.UpdateAddressRequest request
    ) {
        if (!StringUtils.hasText(token)) {
            return Result.error(401, "token不能为空");
        }
        
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "token已过期或无效");
        }
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        try {
            userProfileService.updateAddress(userId, addressId, request);
            return Result.success("收货地址修改成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
} 