package _712.final_project_712.service;

import _712.final_project_712.model.dto.UpdateProfileRequest;
import _712.final_project_712.model.dto.UserInfoResponse;

public interface UserProfileService {
    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param request 更新信息请求
     */
    void updateUserProfile(Long userId, UpdateProfileRequest request);
    
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
} 