package _712.final_project_712.mapper;

import _712.final_project_712.model.Activity;
import _712.final_project_712.model.dto.ActivityDTO;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
    
    @Select("SELECT a.id, a.name, a.description, a.start_time, a.end_time, a.status, " +
            "ap.id as product_id, ap.activity_price, ap.stock, " +
            "p.name as product_name, p.image_url as product_image, p.price as original_price " +
            "FROM tb_activity a " +
            "LEFT JOIN tb_activity_product ap ON a.id = ap.activity_id " +
            "LEFT JOIN product p ON ap.product_id = p.id " +
            "WHERE a.id = #{activityId}")
    ActivityDTO getActivityWithProducts(Long activityId);
    
    @Select("SELECT a.id, a.name, a.description, a.start_time, a.end_time, a.status, " +
            "ap.id as product_id, ap.activity_price, ap.stock, " +
            "p.name as product_name, p.image_url as product_image, p.price as original_price " +
            "FROM tb_activity a " +
            "LEFT JOIN tb_activity_product ap ON a.id = ap.activity_id " +
            "LEFT JOIN product p ON ap.product_id = p.id " +
            "WHERE a.status = #{status}")
    List<ActivityDTO> getActivitiesByStatus(Integer status);
} 