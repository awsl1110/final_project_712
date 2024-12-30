package _712.final_project_712.service;

import _712.final_project_712.model.dto.AddAddressRequest;
import _712.final_project_712.model.dto.UpdateUserRequest;
import _712.final_project_712.model.dto.UserInfoResponse;
import _712.final_project_712.model.dto.UpdateAddressRequest;

public interface UserProfileService {
    /**
     * 修改用户信息
     * @param userId 用户ID
     * @param request 修改信息请求
     */
    void updateUserInfo(Long userId, UpdateUserRequest request);
    
    /**
     * 新增收货地址
     * @param userId 用户ID
     * @param request 新增地址请求
     */
    void addAddress(Long userId, AddAddressRequest request);
    
    /**
     * 获取用户完整信息
     * @param userId 用户ID
     * @return 用户信息响应
     */
    UserInfoResponse getUserInfo(Long userId);
    
    /**
     * 删除用户地址
     * @param userId 用户ID
     * @param addressId 地址ID
     */
    void deleteAddress(Long userId, Long addressId);
    
    /**
     * 修改收货地址
     * @param userId 用户ID
     * @param addressId 地址ID
     * @param request 修改地址请求
     */
    void updateAddress(Long userId, Long addressId, UpdateAddressRequest request);
} 