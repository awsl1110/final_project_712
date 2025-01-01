package _712.final_project_712.mapper;

import _712.final_project_712.model.PickupCode;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PickupCodeMapper extends BaseMapper<PickupCode> {
    // 基础的 CRUD 操作由 BaseMapper 提供
    // 如果需要自定义查询方法可以在这里添加
} 