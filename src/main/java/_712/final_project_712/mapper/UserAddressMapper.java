package _712.final_project_712.mapper;

import _712.final_project_712.model.UserAddress;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Update;

public interface UserAddressMapper extends BaseMapper<UserAddress> {
    
    @Update("UPDATE user_address SET is_default = 0 WHERE user_id = #{userId}")
    void updateNonDefault(Long userId);
} 