package _712.final_project_712.mapper;

import _712.final_project_712.model.Coupon;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.time.LocalDateTime;

@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {
    @Select("SELECT * FROM coupon WHERE id = #{id} FOR UPDATE")
    Coupon selectByIdForUpdate(Long id);

    @Update("UPDATE coupon SET remain = #{remain}, update_time = #{updateTime} " +
            "WHERE id = #{id} AND status = 1 AND remain = #{oldRemain}")
    int updateRemainById(Long id, int remain, int oldRemain, LocalDateTime updateTime);

    @Delete("DELETE FROM coupon WHERE name IN ('满100减20券', '8折券', '过期优惠券')")
    int deleteTestCoupons();
} 