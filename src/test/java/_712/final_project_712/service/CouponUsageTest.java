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
    private Coupon normalCoupon;
    private Coupon expiredCoupon;
    private Coupon disabledCoupon;
    private Coupon zeroCoupon;

    @BeforeEach
    void setUp() {
        // 清理所有测试数据
        couponMapper.deleteTestCoupons();
        userCouponMapper.deleteByUserId(TEST_USER_ID);
        
        // 创建正常优惠券
        normalCoupon = new Coupon();
        normalCoupon.setName("满100减20券");
        normalCoupon.setType(1); // 满减券
        normalCoupon.setValue(new BigDecimal("20.00"));
        normalCoupon.setMinAmount(new BigDecimal("100.00"));
        normalCoupon.setStartTime(LocalDateTime.now().minusDays(1));
        normalCoupon.setEndTime(LocalDateTime.now().plusDays(7));
        normalCoupon.setTotal(100);
        normalCoupon.setRemain(100);
        normalCoupon.setStatus(1);
        normalCoupon.setCreateTime(LocalDateTime.now());
        normalCoupon.setUpdateTime(LocalDateTime.now());
        couponMapper.insertSelective(normalCoupon);

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

        // 创建已禁用优惠券
        disabledCoupon = new Coupon();
        disabledCoupon.setName("禁用优惠券");
        disabledCoupon.setType(1);
        disabledCoupon.setValue(new BigDecimal("15.00"));
        disabledCoupon.setMinAmount(new BigDecimal("80.00"));
        disabledCoupon.setStartTime(LocalDateTime.now().minusDays(1));
        disabledCoupon.setEndTime(LocalDateTime.now().plusDays(7));
        disabledCoupon.setTotal(100);
        disabledCoupon.setRemain(100);
        disabledCoupon.setStatus(0); // 禁用状态
        disabledCoupon.setCreateTime(LocalDateTime.now());
        disabledCoupon.setUpdateTime(LocalDateTime.now());
        couponMapper.insertSelective(disabledCoupon);

        // 创建库存为0的优惠券
        zeroCoupon = new Coupon();
        zeroCoupon.setName("库存为0优惠券");
        zeroCoupon.setType(1);
        zeroCoupon.setValue(new BigDecimal("25.00"));
        zeroCoupon.setMinAmount(new BigDecimal("120.00"));
        zeroCoupon.setStartTime(LocalDateTime.now().minusDays(1));
        zeroCoupon.setEndTime(LocalDateTime.now().plusDays(7));
        zeroCoupon.setTotal(100);
        zeroCoupon.setRemain(0); // 库存为0
        zeroCoupon.setStatus(1);
        zeroCoupon.setCreateTime(LocalDateTime.now());
        zeroCoupon.setUpdateTime(LocalDateTime.now());
        couponMapper.insertSelective(zeroCoupon);
    }

    @Test
    @DisplayName("测试正常领取优惠券")
    void testReceiveCoupon() {
        // 测试领取正常优惠券
        assertTrue(userCouponService.receiveCoupon(TEST_USER_ID, normalCoupon.getId()));

        // 验证优惠券是否被正确领取
        List<UserCouponDTO> userCoupons = userCouponService.getUserCoupons(TEST_USER_ID);
        assertFalse(userCoupons.isEmpty());
        assertEquals(normalCoupon.getId(), userCoupons.get(0).getCouponId());
        assertEquals(0, userCoupons.get(0).getStatus()); // 未使用状态

        // 验证优惠券剩余数量是否减少
        Coupon updatedCoupon = couponMapper.selectByIdForUpdate(normalCoupon.getId());
        assertEquals(normalCoupon.getRemain() - 1, updatedCoupon.getRemain());
    }

    @Test
    @DisplayName("测试重复领取优惠券")
    void testReceiveCouponTwice() {
        // 第一次领取
        assertTrue(userCouponService.receiveCoupon(TEST_USER_ID, normalCoupon.getId()));

        // 第二次领取应该抛出异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userCouponService.receiveCoupon(TEST_USER_ID, normalCoupon.getId());
        });
        assertEquals("您已经领取过该优惠券", exception.getMessage());
    }

    @Test
    @DisplayName("测试领取过期优惠券")
    void testReceiveExpiredCoupon() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userCouponService.receiveCoupon(TEST_USER_ID, expiredCoupon.getId());
        });
        assertEquals("优惠券不在有效期内", exception.getMessage());
    }

    @Test
    @DisplayName("测试领取已禁用优惠券")
    void testReceiveDisabledCoupon() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userCouponService.receiveCoupon(TEST_USER_ID, disabledCoupon.getId());
        });
        assertEquals("优惠券已禁用", exception.getMessage());
    }

    @Test
    @DisplayName("测试领取库存为0的优惠券")
    void testReceiveZeroRemainCoupon() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userCouponService.receiveCoupon(TEST_USER_ID, zeroCoupon.getId());
        });
        assertEquals("优惠券已被领完", exception.getMessage());
    }
} 