package _712.final_project_712.controller;

import _712.final_project_712.mapper.CouponMapper;
import _712.final_project_712.model.Coupon;
import _712.final_project_712.model.Result;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@Tag(name = "优惠券管理", description = "优惠券相关接口")
public class CouponController {

    @Autowired
    private CouponMapper couponMapper;

    @GetMapping
    @Operation(summary = "获取所有优惠券列表")
    public Result<List<Coupon>> getAllCoupons() {
        try {
            List<Coupon> coupons = couponMapper.selectListByQuery(new QueryWrapper());
            return Result.success(coupons);
        } catch (Exception e) {
            return Result.error("获取优惠券列表失败：" + e.getMessage());
        }
    }
} 