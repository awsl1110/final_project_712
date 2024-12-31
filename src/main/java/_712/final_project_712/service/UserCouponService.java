package _712.final_project_712.service;

import _712.final_project_712.model.dto.UserCouponDTO;
import java.util.List;

public interface UserCouponService {
    List<UserCouponDTO> getUserCoupons(Long userId);
} 