package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.UserCouponMapper;
import _712.final_project_712.model.dto.UserCouponDTO;
import _712.final_project_712.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCouponServiceImpl implements UserCouponService {

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    public List<UserCouponDTO> getUserCoupons(Long userId) {
        return userCouponMapper.findByUserId(userId);
    }
} 