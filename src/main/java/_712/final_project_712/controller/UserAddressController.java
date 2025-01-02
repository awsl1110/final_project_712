package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.UserAddress;
import _712.final_project_712.service.UserAddressService;
import _712.final_project_712.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户地址管理", description = "用户收货地址相关接口")
@RestController
@RequestMapping("/api/address")
public class UserAddressController {

    @Autowired
    private UserAddressService addressService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "获取用户的所有收货地址")
    @GetMapping("/list")
    public Result<List<UserAddress>> getUserAddresses(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtil.getUserIdFromToken(actualToken);
            if (userId == null) {
                return Result.error("无效的token");
            }
            
            List<UserAddress> addresses = addressService.getUserAddresses(userId);
            return Result.success(addresses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取用户的默认收货地址")
    @GetMapping("/default")
    public Result<UserAddress> getDefaultAddress(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtil.getUserIdFromToken(actualToken);
            if (userId == null) {
                return Result.error("无效的token");
            }
            
            UserAddress address = addressService.getDefaultAddress(userId);
            return Result.success(address);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "添加收货地址")
    @PostMapping("/add")
    public Result<?> addAddress(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "收货地址信息", required = true)
            @RequestBody UserAddress address) {
        try {
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtil.getUserIdFromToken(actualToken);
            if (userId == null) {
                return Result.error("无效的token");
            }
            
            address.setUserId(userId);
            addressService.addAddress(address);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "更新收货地址")
    @PutMapping("/update")
    public Result<?> updateAddress(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "收货地址信息", required = true)
            @RequestBody UserAddress address) {
        try {
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtil.getUserIdFromToken(actualToken);
            if (userId == null) {
                return Result.error("无效的token");
            }
            
            address.setUserId(userId);
            addressService.updateAddress(address);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "删除收货地址")
    @DeleteMapping("/{addressId}")
    public Result<?> deleteAddress(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "地址ID", required = true)
            @PathVariable Long addressId) {
        try {
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtil.getUserIdFromToken(actualToken);
            if (userId == null) {
                return Result.error("无效的token");
            }
            
            addressService.deleteAddress(userId, addressId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "设置默认收货地址")
    @PutMapping("/{addressId}/default")
    public Result<?> setDefaultAddress(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "地址ID", required = true)
            @PathVariable Long addressId) {
        try {
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtil.getUserIdFromToken(actualToken);
            if (userId == null) {
                return Result.error("无效的token");
            }
            
            addressService.setDefaultAddress(userId, addressId);
            return Result.success("设置成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 