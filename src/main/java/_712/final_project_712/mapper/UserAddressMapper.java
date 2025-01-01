package _712.final_project_712.mapper;

import _712.final_project_712.model.UserAddress;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    /**
     * 将用户的所有地址设置为非默认
     */
    @Update("UPDATE user_address SET is_default = false, update_time = NOW() WHERE user_id = #{userId}")
    void updateNonDefault(@Param("userId") Long userId);
} 