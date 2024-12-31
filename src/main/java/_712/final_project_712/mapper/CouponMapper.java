package _712.final_project_712.mapper;

import _712.final_project_712.model.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CouponMapper {
    @Select("SELECT * FROM coupon WHERE id = #{id}")
    Coupon selectOneById(Long id);
    
    @Update("UPDATE coupon SET remain = #{remain}, update_time = NOW() WHERE id = #{id}")
    int updateById(Coupon coupon);
} 