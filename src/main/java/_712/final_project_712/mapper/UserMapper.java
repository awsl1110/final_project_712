package _712.final_project_712.mapper;

import _712.final_project_712.model.User;
import _712.final_project_712.model.UserAddress;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 获取用户信息及其默认地址
     */
    @Select("SELECT u.id, u.name, u.email, " +
            "ua.id as address_id, ua.receiver_name, ua.receiver_phone, " +
            "ua.province, ua.city, ua.district, ua.detail_address, ua.is_default " +
            "FROM user u " +
            "LEFT JOIN user_address ua ON u.id = ua.user_id AND ua.is_default = true " +
            "WHERE u.id = #{userId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "email", column = "email"),
        @Result(property = "defaultAddress.id", column = "address_id"),
        @Result(property = "defaultAddress.receiverName", column = "receiver_name"),
        @Result(property = "defaultAddress.receiverPhone", column = "receiver_phone"),
        @Result(property = "defaultAddress.province", column = "province"),
        @Result(property = "defaultAddress.city", column = "city"),
        @Result(property = "defaultAddress.district", column = "district"),
        @Result(property = "defaultAddress.detailAddress", column = "detail_address"),
        @Result(property = "defaultAddress.isDefault", column = "is_default")
    })
    User getUserWithDefaultAddress(Long userId);
} 