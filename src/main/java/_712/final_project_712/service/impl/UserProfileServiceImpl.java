package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.UserMapper;
import _712.final_project_712.mapper.UserAddressMapper;
import _712.final_project_712.model.User;
import _712.final_project_712.model.UserAddress;
import _712.final_project_712.model.dto.UserDTO;
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
    public void updateUserInfo(Long userId, UserDTO.UpdateUserRequest request) {
        User user = QueryChain.of(User.class)
                .where(User::getId).eq(userId)
                .one();
                
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 更新用户名
        if (StringUtils.hasText(request.getName())) {
            // 检查用户名是否已被使用
            User existingUser = QueryChain.of(User.class)
                    .where(User::getName).eq(request.getName())
                    .and(User::getId).ne(userId)
                    .one();
            if (existingUser != null) {
                throw new RuntimeException("用户名已被使用");
            }
            user.setName(request.getName());
        }
        
        // 更新邮箱
        if (StringUtils.hasText(request.getEmail())) {
            // 验证邮箱格式
            if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
                throw new RuntimeException("邮箱格式不正确");
            }
            user.setEmail(request.getEmail());
        }
        
        userMapper.update(user);
    }

    @Override
    @Transactional
    public void addAddress(Long userId, UserDTO.AddAddressRequest request) {
        // 验证地址信息完整性
        if (!StringUtils.hasText(request.getReceiverName())) {
            throw new RuntimeException("收货人姓名不能为空");
        }
        if (!StringUtils.hasText(request.getReceiverPhone())) {
            throw new RuntimeException("收货人电话不能为空");
        }
        if (!StringUtils.hasText(request.getProvince())) {
            throw new RuntimeException("省份不能为空");
        }
        if (!StringUtils.hasText(request.getCity())) {
            throw new RuntimeException("城市不能为空");
        }
        if (!StringUtils.hasText(request.getDistrict())) {
            throw new RuntimeException("区/县不能为空");
        }
        if (!StringUtils.hasText(request.getDetailAddress())) {
            throw new RuntimeException("详细地址不能为空");
        }
        
        // 如果设置为默认地址，先将其他地址设置为非默认
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressMapper.updateNonDefault(userId);
        }
        
        // 创建新地址
        UserAddress address = new UserAddress();
        address.setUserId(userId);
        address.setReceiverName(request.getReceiverName());
        address.setReceiverPhone(request.getReceiverPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDetailAddress(request.getDetailAddress());
        address.setIsDefault(request.getIsDefault());
        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        
        addressMapper.insert(address);
    }

    @Override
    public UserDTO.UserInfoResponse getUserInfo(Long userId) {
        // 使用关联查询获取用户完整信息
        UserDTO.UserInfoResponse userInfo = userMapper.getUserFullInfo(userId);
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }
        return userInfo;
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

    @Override
    @Transactional
    public void updateAddress(Long userId, Long addressId, UserDTO.UpdateAddressRequest request) {
        // 查询地址是否存在且属于该用户
        UserAddress address = QueryChain.of(UserAddress.class)
                .where(UserAddress::getId).eq(addressId)
                .and(UserAddress::getUserId).eq(userId)
                .one();
                
        if (address == null) {
            throw new RuntimeException("地址不存在或不属于该用户");
        }
        
        // 验证地址信息完整性
        if (!StringUtils.hasText(request.getReceiverName())) {
            throw new RuntimeException("收货人姓名不能为空");
        }
        if (!StringUtils.hasText(request.getReceiverPhone())) {
            throw new RuntimeException("收货人电话不能为空");
        }
        if (!StringUtils.hasText(request.getProvince())) {
            throw new RuntimeException("省份不能为空");
        }
        if (!StringUtils.hasText(request.getCity())) {
            throw new RuntimeException("城市不能为空");
        }
        if (!StringUtils.hasText(request.getDistrict())) {
            throw new RuntimeException("区/县不能为空");
        }
        if (!StringUtils.hasText(request.getDetailAddress())) {
            throw new RuntimeException("详细地址不能为空");
        }
        
        // 如果设置为默认地址，先将其他地址设置为非默认
        if (Boolean.TRUE.equals(request.getIsDefault()) && !Boolean.TRUE.equals(address.getIsDefault())) {
            addressMapper.updateNonDefault(userId);
        }
        
        // 更新地址信息
        address.setReceiverName(request.getReceiverName());
        address.setReceiverPhone(request.getReceiverPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDetailAddress(request.getDetailAddress());
        address.setIsDefault(request.getIsDefault());
        address.setUpdateTime(LocalDateTime.now());
        
        addressMapper.update(address);
    }
} 