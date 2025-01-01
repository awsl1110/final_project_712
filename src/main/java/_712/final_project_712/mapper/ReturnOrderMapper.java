package _712.final_project_712.mapper;

import _712.final_project_712.model.ReturnOrder;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface ReturnOrderMapper extends BaseMapper<ReturnOrder> {
    @Select("SELECT * FROM order_return WHERE order_id = #{orderId}")
    ReturnOrder findByOrderId(Long orderId);
    
    @Select("SELECT * FROM order_return WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<ReturnOrder> findByUserId(Long userId);
    
    @Update("UPDATE order_return SET status = #{status}, handle_note = #{handleNote}, " +
            "handle_time = NOW(), update_time = NOW() WHERE id = #{returnId}")
    int updateStatus(Long returnId, Integer status, String handleNote);
} 