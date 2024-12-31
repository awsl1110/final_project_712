package _712.final_project_712.mapper;

import _712.final_project_712.model.UserAddress;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryChain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    
    /**
     * 将用户的所有地址设置为非默认
     */
    @Update("UPDATE user_address SET is_default = false WHERE user_id = #{userId}")
    void updateNonDefault(@Param("userId") Long userId);

    /**
     * 获取用户的所有地址信息（按是否默认地址降序排列）
     */
    default List<UserAddress> getUserAddresses(Long userId) {
        return QueryChain.of(UserAddress.class)
                .where(UserAddress::getUserId).eq(userId)
                .orderBy(UserAddress::getIsDefault, false)
                .list();
    }

    /**
     * 获取用户的默认地址
     */
    default UserAddress getDefaultAddress(Long userId) {
        return QueryChain.of(UserAddress.class)
                .where(UserAddress::getUserId).eq(userId)
                .and(UserAddress::getIsDefault).eq(true)
                .one();
    }
} 