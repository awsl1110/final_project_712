package _712.final_project_712.controller;

import _712.final_project_712.model.dto.UserCouponDTO;
import _712.final_project_712.service.UserCouponService;
import _712.final_project_712.util.JwtUtil;
import _712.final_project_712.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户优惠券接口", description = "用户优惠券接口")
@RestController
@RequestMapping("/user-coupons")
public class UserCouponController {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "获取当前用户的优惠券列表")
    @GetMapping
    public Result<List<UserCouponDTO>> getUserCoupons(
            @Parameter(description = "用户token", required = true)
            @RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<UserCouponDTO> userCoupons = userCouponService.getUserCoupons(userId);
            return Result.success(userCoupons);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "领取优惠券")
    @PostMapping("/{couponId}")
    public Result<?> receiveCoupon(
            @Parameter(description = "用户token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "优惠券ID", required = true)
            @PathVariable Long couponId) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            boolean success = userCouponService.receiveCoupon(userId, couponId);
            return success ? Result.success("领取成功") : Result.error("领取失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 