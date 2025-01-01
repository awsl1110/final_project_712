package _712.final_project_712.service;

import _712.final_project_712.model.PickupCode;

public interface PickupService {
    /**
     * 生成取件码
     */
    PickupCode generatePickupCode(Long userId);
    
    /**
     * 验证取件码并完成取件
     */
    void pickup(String phone, String pickupCode);
} 