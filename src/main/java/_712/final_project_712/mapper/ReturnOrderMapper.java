package _712.final_project_712.mapper;

import _712.final_project_712.model.ReturnOrder;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReturnOrderMapper extends BaseMapper<ReturnOrder> {
    // 移除原有的@Select和@Update注解方法
    // 使用BaseMapper提供的方法和QueryWrapper来实现
} 