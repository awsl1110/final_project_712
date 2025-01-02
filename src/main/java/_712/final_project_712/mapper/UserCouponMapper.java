package _712.final_project_712.mapper;

import _712.final_project_712.model.dto.UserCouponDTO;
import _712.final_project_712.model.UserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import java.util.List;

@Mapper
public interface UserCouponMapper {
    
    @Select("SELECT uc.id, uc.user_id, uc.coupon_id, uc.status, uc.use_time, uc.order_id, uc.create_time, " +
           "c.id as 'coupon.id', c.name as 'coupon.name', c.type as 'coupon.type', " +
           "c.value as 'coupon.value', c.min_amount as 'coupon.minAmount', " +
           "c.start_time as 'coupon.startTime', c.end_time as 'coupon.endTime', " +
           "c.total as 'coupon.total', c.remain as 'coupon.remain', " +
           "c.status as 'coupon.status', c.create_time as 'coupon.createTime', " +
           "c.update_time as 'coupon.updateTime' " +
           "FROM user_coupon uc " +
           "LEFT JOIN coupon c ON uc.coupon_id = c.id " +
           "WHERE uc.user_id = #{userId}")
    List<UserCouponDTO> findByUserId(Long userId);
    
    @Select("SELECT COUNT(*) > 0 FROM user_coupon WHERE user_id = #{userId} AND coupon_id = #{couponId}")
    boolean checkUserCouponExists(Long userId, Long couponId);
    
    @Insert("INSERT INTO user_coupon (user_id, coupon_id, status, create_time) " +
            "VALUES (#{userId}, #{couponId}, #{status}, #{createTime})")
    int insert(UserCoupon userCoupon);

    @Select("SELECT uc.id, uc.user_id, uc.coupon_id, uc.status, uc.use_time, uc.order_id, uc.create_time, " +
           "c.id as 'coupon.id', c.name as 'coupon.name', c.type as 'coupon.type', " +
           "c.value as 'coupon.value', c.min_amount as 'coupon.minAmount', " +
           "c.start_time as 'coupon.startTime', c.end_time as 'coupon.endTime', " +
           "c.total as 'coupon.total', c.remain as 'coupon.remain', " +
           "c.status as 'coupon.status', c.create_time as 'coupon.createTime', " +
           "c.update_time as 'coupon.updateTime' " +
           "FROM user_coupon uc " +
           "INNER JOIN coupon c ON uc.coupon_id = c.id " +
           "WHERE uc.id = #{userCouponId} AND uc.status = 0 AND c.status = 1 " +
           "AND NOW() BETWEEN c.start_time AND c.end_time")
    UserCouponDTO findAvailableById(Long userCouponId);

    @Update("UPDATE user_coupon SET status = #{status}, use_time = #{useTime}, order_id = #{orderId} " +
            "WHERE id = #{id}")
    int updateStatus(UserCoupon userCoupon);

    @Delete("DELETE FROM user_coupon WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);
} 