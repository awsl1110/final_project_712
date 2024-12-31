package _712.final_project_712.service.impl;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.UserCouponMapper;
import _712.final_project_712.mapper.CouponMapper;
import _712.final_project_712.model.Coupon;
import _712.final_project_712.model.UserCoupon;
import _712.final_project_712.model.dto.UserCouponDTO;
import _712.final_project_712.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserCouponServiceImpl implements UserCouponService {

    @Autowired
    private UserCouponMapper userCouponMapper;
    
    @Autowired
    private CouponMapper couponMapper;

    @Override
    public List<UserCouponDTO> getUserCoupons(Long userId) {
        return userCouponMapper.findByUserId(userId);
    }
    
    @Override
    @Transactional
    public boolean receiveCoupon(Long userId, Long couponId) {
        // 1. 检查优惠券是否存在且有效
        Coupon coupon = couponMapper.selectOneById(couponId);
        if (coupon == null) {
            throw new BusinessException("优惠券不存在");
        }
        
        // 2. 检查优惠券是否在有效期内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            throw new BusinessException("优惠券不在有效期内");
        }
        
        // 3. 检查优惠券是否还有剩余数量
        if (coupon.getRemain() <= 0) {
            throw new BusinessException("优惠券已被领完");
        }
        
        // 4. 检查用户是否已经领取过该优惠券
        if (userCouponMapper.checkUserCouponExists(userId, couponId)) {
            throw new BusinessException("您已领取过该优惠券");
        }
        
        // 5. 创建用户优惠券记录
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(0); // 0-未使用
        userCoupon.setCreateTime(now);
        
        // 6. 减少优惠券剩余数量
        coupon.setRemain(coupon.getRemain() - 1);
        couponMapper.updateById(coupon);
        
        // 7. 保存用户优惠券记录
        return userCouponMapper.insert(userCoupon) > 0;
    }
} 