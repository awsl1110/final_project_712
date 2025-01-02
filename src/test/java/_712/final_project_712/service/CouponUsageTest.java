package _712.final_project_712.service;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.CouponMapper;
import _712.final_project_712.mapper.UserCouponMapper;
import _712.final_project_712.model.Coupon;
import _712.final_project_712.model.dto.UserCouponDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CouponUsageTest {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    private final Long TEST_USER_ID = 1L;
    private Coupon fullReductionCoupon;
    private Coupon discountCoupon;
    private Coupon expiredCoupon;

    @BeforeEach
    void setUp() {
        // 清理所有测试数据
        couponMapper.deleteTestCoupons();
        userCouponMapper.deleteByUserId(TEST_USER_ID);
        
        // 创建满减券
        fullReductionCoupon = new Coupon();
        fullReductionCoupon.setName("满100减20券");
        fullReductionCoupon.setType(1); // 满减券
        fullReductionCoupon.setValue(new BigDecimal("20.00"));
        fullReductionCoupon.setMinAmount(new BigDecimal("100.00"));
        fullReductionCoupon.setStartTime(LocalDateTime.now().minusDays(1));
        fullReductionCoupon.setEndTime(LocalDateTime.now().plusDays(7));
        fullReductionCoupon.setTotal(100);
        fullReductionCoupon.setRemain(100);
        fullReductionCoupon.setStatus(1);
        fullReductionCoupon.setCreateTime(LocalDateTime.now());
        fullReductionCoupon.setUpdateTime(LocalDateTime.now());
        couponMapper.insertSelective(fullReductionCoupon);

        // 创建折扣券
        discountCoupon = new Coupon();
        discountCoupon.setName("8折券");
        discountCoupon.setType(2); // 折扣券
        discountCoupon.setValue(new BigDecimal("80")); // 8折
        discountCoupon.setMinAmount(new BigDecimal("50.00"));
        discountCoupon.setStartTime(LocalDateTime.now().minusDays(1));
        discountCoupon.setEndTime(LocalDateTime.now().plusDays(7));
        discountCoupon.setTotal(100);
        discountCoupon.setRemain(100);
        discountCoupon.setStatus(1);
        discountCoupon.setCreateTime(LocalDateTime.now());
        discountCoupon.setUpdateTime(LocalDateTime.now());
        couponMapper.insertSelective(discountCoupon);

        // 创建过期优惠券
        expiredCoupon = new Coupon();
        expiredCoupon.setName("过期优惠券");
        expiredCoupon.setType(1);
        expiredCoupon.setValue(new BigDecimal("10.00"));
        expiredCoupon.setMinAmount(new BigDecimal("50.00"));
        expiredCoupon.setStartTime(LocalDateTime.now().minusDays(10));
        expiredCoupon.setEndTime(LocalDateTime.now().minusDays(1));
        expiredCoupon.setTotal(100);
        expiredCoupon.setRemain(100);
        expiredCoupon.setStatus(1);
        expiredCoupon.setCreateTime(LocalDateTime.now());
        expiredCoupon.setUpdateTime(LocalDateTime.now());
        couponMapper.insertSelective(expiredCoupon);
    }

    @Test
    @DisplayName("测试领取优惠券")
    @Transactional
    void testReceiveCoupon() {
        // 清理用户优惠券数据
        userCouponMapper.deleteByUserId(TEST_USER_ID);
        
        // 获取原始剩余数量
        Coupon coupon = couponMapper.selectByIdForUpdate(fullReductionCoupon.getId());
        int originalRemain = coupon.getRemain();
        
        // 测试领取满减券
        assertTrue(userCouponService.receiveCoupon(TEST_USER_ID, fullReductionCoupon.getId()));

        // 验证优惠券是否被正确领取
        List<UserCouponDTO> userCoupons = userCouponService.getUserCoupons(TEST_USER_ID);
        assertFalse(userCoupons.isEmpty());
        assertEquals(fullReductionCoupon.getId(), userCoupons.get(0).getCouponId());
        assertEquals(0, userCoupons.get(0).getStatus()); // 未使用状态

        // 验证优惠券剩余数量是否减少
        Coupon updatedCoupon = couponMapper.selectByIdForUpdate(fullReductionCoupon.getId());
        assertEquals(originalRemain - 1, updatedCoupon.getRemain());
    }

    @Test
    @DisplayName("测试重复领取优惠券")
    void testReceiveCouponTwice() {
        // 第一次领取
        assertTrue(userCouponService.receiveCoupon(TEST_USER_ID, fullReductionCoupon.getId()));

        // 第二次领取应该抛出异常
        assertThrows(BusinessException.class, () -> {
            userCouponService.receiveCoupon(TEST_USER_ID, fullReductionCoupon.getId());
        });
    }

    @Test
    @DisplayName("测试使用满减券")
    void testUseFullReductionCoupon() {
        // 先领取优惠券
        userCouponService.receiveCoupon(TEST_USER_ID, fullReductionCoupon.getId());
        List<UserCouponDTO> userCoupons = userCouponService.getUserCoupons(TEST_USER_ID);
        UserCouponDTO userCoupon = userCoupons.stream()
                .filter(uc -> uc.getCouponId().equals(fullReductionCoupon.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("优惠券未找到"));

        // 使用优惠券
        BigDecimal orderAmount = new BigDecimal("150.00");
        BigDecimal discountedAmount = userCouponService.useCoupon(userCoupon.getId(), 1L, orderAmount);

        // 验证优惠后金额是否正确（150-20=130）
        assertEquals(new BigDecimal("130.00"), discountedAmount);

        // 验证优惠券状态是否更新为已使用
        UserCouponDTO updatedCoupon = userCouponService.getAvailableCoupon(userCoupon.getId());
        assertNull(updatedCoupon); // 已使用的优惠券不应该能被查询到
    }

    @Test
    @DisplayName("测试使用折扣券")
    void testUseDiscountCoupon() {
        // 先领取优惠券
        userCouponService.receiveCoupon(TEST_USER_ID, discountCoupon.getId());
        List<UserCouponDTO> userCoupons = userCouponService.getUserCoupons(TEST_USER_ID);
        UserCouponDTO userCoupon = userCoupons.stream()
                .filter(uc -> uc.getCouponId().equals(discountCoupon.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("优惠券未找到"));

        // 使用优惠券
        BigDecimal orderAmount = new BigDecimal("100.00");
        BigDecimal discountedAmount = userCouponService.useCoupon(userCoupon.getId(), 1L, orderAmount);

        // 验证优惠后金额是否正确（100*0.8=80）
        assertEquals(new BigDecimal("80.00"), discountedAmount.setScale(2));
    }

    @Test
    @DisplayName("测试使用过期优惠券")
    void testUseExpiredCoupon() {
        // 尝试领取过期优惠券
        assertThrows(BusinessException.class, () -> {
            userCouponService.receiveCoupon(TEST_USER_ID, expiredCoupon.getId());
        });
    }

    @Test
    @DisplayName("测试订单金额不满足条件")
    void testInsufficientOrderAmount() {
        // 先领取优惠券
        userCouponService.receiveCoupon(TEST_USER_ID, fullReductionCoupon.getId());
        List<UserCouponDTO> userCoupons = userCouponService.getUserCoupons(TEST_USER_ID);
        UserCouponDTO userCoupon = userCoupons.stream()
                .filter(uc -> uc.getCouponId().equals(fullReductionCoupon.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("优惠券未找到"));

        // 使用优惠券，但订单金额不满足最低使用金额
        BigDecimal orderAmount = new BigDecimal("90.00"); // 小于最低使用金额100
        
        // 应该抛出异常
        assertThrows(BusinessException.class, () -> {
            userCouponService.useCoupon(userCoupon.getId(), 1L, orderAmount);
        });
    }
} 