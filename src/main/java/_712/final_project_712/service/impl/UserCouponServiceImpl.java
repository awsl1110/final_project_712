package _712.final_project_712.service.impl;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.UserCouponMapper;
import _712.final_project_712.mapper.CouponMapper;
import _712.final_project_712.model.Coupon;
import _712.final_project_712.model.UserCoupon;
import _712.final_project_712.model.dto.UserCouponDTO;
import _712.final_project_712.service.UserCouponService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        // 1. 检查用户是否已经领取过该优惠券
        if (userCouponMapper.checkUserCouponExists(userId, couponId)) {
            throw new BusinessException("您已领取过该优惠券");
        }
        
        // 2. 获取优惠券信息并加锁
        Coupon coupon = couponMapper.selectByIdForUpdate(couponId);
        if (coupon == null) {
            throw new BusinessException("优惠券不存在");
        }
        
        // 3. 检查优惠券状态
        if (coupon.getStatus() != 1) {
            throw new BusinessException("优惠券已禁用");
        }
        
        // 4. 检查优惠券是否在有效期内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            throw new BusinessException("优惠券不在有效期内");
        }
        
        // 5. 检查优惠券是否还有剩余数量
        if (coupon.getRemain() <= 0) {
            throw new BusinessException("优惠券已被领完");
        }
        
        // 6. 创建用户优惠券记录
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(0); // 0-未使用
        userCoupon.setCreateTime(now);
        
        // 7. 减少优惠券剩余数量并更新
        int oldRemain = coupon.getRemain();
        int newRemain = oldRemain - 1;
        int rows = couponMapper.updateRemainById(couponId, newRemain, oldRemain, now);
        if (rows <= 0) {
            throw new BusinessException("优惠券已被领完或更新失败");
        }
        
        // 8. 保存用户优惠券记录
        return userCouponMapper.insert(userCoupon) > 0;
    }

    @Override
    public UserCouponDTO getAvailableCoupon(Long userCouponId) {
        return userCouponMapper.findAvailableById(userCouponId);
    }

    @Override
    @Transactional
    public BigDecimal useCoupon(Long userCouponId, Long orderId, BigDecimal orderAmount) {
        // 1. 获取优惠券信息
        UserCouponDTO userCoupon = getAvailableCoupon(userCouponId);
        if (userCoupon == null) {
            throw new BusinessException("优惠券不存在或已使用");
        }

        // 2. 检查优惠券是否在有效期内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(userCoupon.getCoupon().getStartTime()) || 
            now.isAfter(userCoupon.getCoupon().getEndTime())) {
            throw new BusinessException("优惠券已过期");
        }

        // 3. 检查订单金额是否满足优惠券使用条件
        if (orderAmount.compareTo(userCoupon.getCoupon().getMinAmount()) < 0) {
            throw new BusinessException("订单金额未达到优惠券使用条件");
        }

        // 4. 计算优惠后金额
        BigDecimal finalAmount;
        if (userCoupon.getCoupon().getType() == 1) {
            // 满减券
            finalAmount = orderAmount.subtract(userCoupon.getCoupon().getValue());
        } else if (userCoupon.getCoupon().getType() == 2) {
            // 折扣券，value存储的是折扣率，例如80表示8折
            BigDecimal discountRate = userCoupon.getCoupon().getValue().divide(new BigDecimal("100"));
            finalAmount = orderAmount.multiply(discountRate);
        } else {
            throw new BusinessException("不支持的优惠券类型");
        }

        // 5. 确保优惠后金额不小于0
        finalAmount = finalAmount.max(BigDecimal.ZERO);

        // 6. 更新优惠券状态
        UserCoupon updateCoupon = new UserCoupon();
        updateCoupon.setId(userCouponId);
        updateCoupon.setStatus(1); // 1-已使用
        updateCoupon.setUseTime(now);
        updateCoupon.setOrderId(orderId);
        userCouponMapper.updateStatus(updateCoupon);

        return finalAmount;
    }
} 