package _712.final_project_712.service.impl;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.UserAddressMapper;
import _712.final_project_712.model.UserAddress;
import _712.final_project_712.service.UserAddressService;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> getUserAddresses(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        
        return QueryChain.of(UserAddress.class)
                .where("user_id = ?", userId)
                .orderBy("is_default desc, update_time desc")
                .list();
    }

    @Override
    public UserAddress getDefaultAddress(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        
        return QueryChain.of(UserAddress.class)
                .where("user_id = ?", userId)
                .and("is_default = ?", true)
                .one();
    }

    @Override
    @Transactional
    public void addAddress(UserAddress address) {
        // 参数校验
        validateAddress(address);
        
        // 如果是默认地址，需要将其他地址设置为非默认
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            clearDefaultAddress(address.getUserId());
        }
        
        // 如果是用户的第一个地址，自动设置为默认地址
        long count = QueryChain.of(UserAddress.class)
                .where("user_id = ?", address.getUserId())
                .count();
        if (count == 0) {
            address.setIsDefault(true);
        }
        
        // 设置时间
        LocalDateTime now = LocalDateTime.now();
        address.setCreateTime(now);
        address.setUpdateTime(now);
        
        userAddressMapper.insert(address);
    }

    @Override
    @Transactional
    public void updateAddress(UserAddress address) {
        // 参数校验
        if (address.getId() == null) {
            throw new BusinessException("地址ID不能为空");
        }
        validateAddress(address);
        
        // 验证地址是否存在且属于当前用户
        UserAddress existingAddress = userAddressMapper.selectOneById(address.getId());
        if (existingAddress == null || !existingAddress.getUserId().equals(address.getUserId())) {
            throw new BusinessException("地址不存在或无权限修改");
        }
        
        // 如果设置为默认地址，需要将其他地址设置为非默认
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            clearDefaultAddress(address.getUserId());
        }
        
        // 更新时间
        address.setUpdateTime(LocalDateTime.now());
        
        userAddressMapper.update(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        // 参数校验
        if (userId == null || addressId == null) {
            throw new BusinessException("参数不能为空");
        }
        
        // 验证地址是否存在且属于当前用户
        UserAddress address = userAddressMapper.selectOneById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在或无权限删除");
        }
        
        // 如果删除的是默认地址，需要重新设置一个默认地址
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            UserAddress newDefault = QueryChain.of(UserAddress.class)
                    .where("user_id = ?", userId)
                    .and("id != ?", addressId)
                    .orderBy("update_time desc")
                    .one();
            if (newDefault != null) {
                newDefault.setIsDefault(true);
                userAddressMapper.update(newDefault);
            }
        }
        
        userAddressMapper.deleteById(addressId);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long userId, Long addressId) {
        // 参数校验
        if (userId == null || addressId == null) {
            throw new BusinessException("参数不能为空");
        }
        
        // 验证地址是否存在且属于当前用户
        UserAddress address = userAddressMapper.selectOneById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在或无权限设置");
        }
        
        // 清除其他默认地址
        clearDefaultAddress(userId);
        
        // 设置新的默认地址
        address.setIsDefault(true);
        address.setUpdateTime(LocalDateTime.now());
        userAddressMapper.update(address);
    }
    
    /**
     * 清除用户的默认地址
     */
    private void clearDefaultAddress(Long userId) {
        userAddressMapper.updateNonDefault(userId);
    }
    
    /**
     * 验证地址信息
     */
    private void validateAddress(UserAddress address) {
        if (address.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (address.getReceiverName() == null || address.getReceiverName().trim().isEmpty()) {
            throw new BusinessException("收货人姓名不能为空");
        }
        if (address.getReceiverPhone() == null || address.getReceiverPhone().trim().isEmpty()) {
            throw new BusinessException("收货人电话不能为空");
        }
        if (address.getProvince() == null || address.getProvince().trim().isEmpty()) {
            throw new BusinessException("省份不能为空");
        }
        if (address.getCity() == null || address.getCity().trim().isEmpty()) {
            throw new BusinessException("城市不能为空");
        }
        if (address.getDistrict() == null || address.getDistrict().trim().isEmpty()) {
            throw new BusinessException("区/县不能为空");
        }
        if (address.getDetailAddress() == null || address.getDetailAddress().trim().isEmpty()) {
            throw new BusinessException("详细地址不能为空");
        }
    }
} 