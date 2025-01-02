package _712.final_project_712.service;

import _712.final_project_712.model.dto.UserCouponDTO;
import java.math.BigDecimal;
import java.util.List;

public interface UserCouponService {
    List<UserCouponDTO> getUserCoupons(Long userId);
    
    /**
     * 用户领取优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 是否领取成功
     */
    boolean receiveCoupon(Long userId, Long couponId);

    /**
     * 使用优惠券
     * @param userCouponId 用户优惠券ID
     * @param orderId 订单ID
     * @param orderAmount 订单金额
     * @return 优惠后的金额
     */
    BigDecimal useCoupon(Long userCouponId, Long orderId, BigDecimal orderAmount);

    /**
     * 获取可用的优惠券
     * @param userCouponId 用户优惠券ID
     * @return 优惠券信息
     */
    UserCouponDTO getAvailableCoupon(Long userCouponId);
} 