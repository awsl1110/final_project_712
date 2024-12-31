package _712.final_project_712.controller;

import _712.final_project_712.model.dto.UserCouponDTO;
import _712.final_project_712.service.UserCouponService;
import _712.final_project_712.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<UserCouponDTO>> getUserCoupons(
            @Parameter(description = "用户token", required = true)
            @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<UserCouponDTO> userCoupons = userCouponService.getUserCoupons(userId);
        return ResponseEntity.ok(userCoupons);
    }
} 