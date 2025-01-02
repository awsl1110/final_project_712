package _712.final_project_712.service;

import _712.final_project_712.model.UserAddress;
import java.util.List;

public interface UserAddressService {
    /**
     * 获取用户的所有收货地址
     */
    List<UserAddress> getUserAddresses(Long userId);
    
    /**
     * 获取用户的默认收货地址
     */
    UserAddress getDefaultAddress(Long userId);
    
    /**
     * 添加收货地址
     */
    void addAddress(UserAddress address);
    
    /**
     * 更新收货地址
     */
    void updateAddress(UserAddress address);
    
    /**
     * 删除收货地址
     */
    void deleteAddress(Long userId, Long addressId);
    
    /**
     * 设置默认地址
     */
    void setDefaultAddress(Long userId, Long addressId);
} 