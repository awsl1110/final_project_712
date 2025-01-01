package _712.final_project_712.mapper;

import _712.final_project_712.model.ReturnOrder;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReturnOrderMapper extends BaseMapper<ReturnOrder> {
    @Select("SELECT * FROM order_return WHERE order_id = #{orderId}")
    ReturnOrder findByOrderId(Long orderId);
} 