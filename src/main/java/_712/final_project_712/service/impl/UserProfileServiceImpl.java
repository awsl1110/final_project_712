package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.UserMapper;
import _712.final_project_712.mapper.UserAddressMapper;
import _712.final_project_712.model.User;
import _712.final_project_712.model.UserAddress;
import _712.final_project_712.model.dto.UpdateProfileRequest;
import _712.final_project_712.model.dto.UserInfoResponse;
import _712.final_project_712.service.UserProfileService;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAddressMapper addressMapper;

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    @Transactional
    public void updateUserProfile(Long userId, UpdateProfileRequest request) {
        User user = QueryChain.of(User.class)
                .where(User::getId).eq(userId)
                .one();
                
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 1. 更新邮箱
        if (StringUtils.hasText(request.getEmail())) {
            // 验证邮箱格式
            if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
                throw new RuntimeException("邮箱格式不正确");
            }
            user.setEmail(request.getEmail());
            userMapper.update(user);
        }
        
        // 2. 更新地址
        if (request.getAddress() != null) {
            UpdateProfileRequest.AddressInfo addrInfo = request.getAddress();
            
            // 验证地址信息完整性
            if (!StringUtils.hasText(addrInfo.getReceiverName())) {
                throw new RuntimeException("收货人姓名不能为空");
            }
            if (!StringUtils.hasText(addrInfo.getReceiverPhone())) {
                throw new RuntimeException("收货人电话不能为空");
            }
            if (!StringUtils.hasText(addrInfo.getProvince())) {
                throw new RuntimeException("省份不能为空");
            }
            if (!StringUtils.hasText(addrInfo.getCity())) {
                throw new RuntimeException("城市不能为空");
            }
            if (!StringUtils.hasText(addrInfo.getDistrict())) {
                throw new RuntimeException("区/县不能为空");
            }
            if (!StringUtils.hasText(addrInfo.getDetailAddress())) {
                throw new RuntimeException("详细地址不能为空");
            }
            
            // 如果设置为默认地址，先将其他地址设置为非默认
            if (Boolean.TRUE.equals(addrInfo.getIsDefault())) {
                addressMapper.updateNonDefault(userId);
            }
            
            // 创建新地址
            UserAddress address = new UserAddress();
            address.setUserId(userId);
            address.setReceiverName(addrInfo.getReceiverName());
            address.setReceiverPhone(addrInfo.getReceiverPhone());
            address.setProvince(addrInfo.getProvince());
            address.setCity(addrInfo.getCity());
            address.setDistrict(addrInfo.getDistrict());
            address.setDetailAddress(addrInfo.getDetailAddress());
            address.setIsDefault(addrInfo.getIsDefault());
            address.setCreateTime(LocalDateTime.now());
            address.setUpdateTime(LocalDateTime.now());
            
            addressMapper.insert(address);
        }
    }

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        // 查询用户基本信息
        User user = QueryChain.of(User.class)
                .where(User::getId).eq(userId)
                .one();
                
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 查询用户的所有地址
        List<UserAddress> addresses = QueryChain.of(UserAddress.class)
                .where(UserAddress::getUserId).eq(userId)
                .orderBy(UserAddress::getIsDefault, false)
                .list();
        
        // 构建响应DTO
        UserInfoResponse response = new UserInfoResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        
        // 转换地址信息
        List<UserInfoResponse.AddressInfo> addressInfos = addresses.stream()
                .map(addr -> {
                    UserInfoResponse.AddressInfo info = new UserInfoResponse.AddressInfo();
                    info.setId(addr.getId());
                    info.setReceiverName(addr.getReceiverName());
                    info.setReceiverPhone(addr.getReceiverPhone());
                    info.setProvince(addr.getProvince());
                    info.setCity(addr.getCity());
                    info.setDistrict(addr.getDistrict());
                    info.setDetailAddress(addr.getDetailAddress());
                    info.setIsDefault(addr.getIsDefault());
                    return info;
                })
                .collect(Collectors.toList());
                
        response.setAddresses(addressInfos);
        
        return response;
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        // 查询地址是否存在且属于该用户
        UserAddress address = QueryChain.of(UserAddress.class)
                .where(UserAddress::getId).eq(addressId)
                .and(UserAddress::getUserId).eq(userId)
                .one();
                
        if (address == null) {
            throw new RuntimeException("地址不存在或不属于该用户");
        }
        
        // 删除地址
        addressMapper.deleteById(addressId);
    }
} 