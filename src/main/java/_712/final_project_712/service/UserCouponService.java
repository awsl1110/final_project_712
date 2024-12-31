package _712.final_project_712.service;

import _712.final_project_712.model.dto.UserCouponDTO;
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
} 