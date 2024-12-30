package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.UserMapper;
import _712.final_project_712.mapper.UserAddressMapper;
import _712.final_project_712.model.User;
import _712.final_project_712.model.UserAddress;
import _712.final_project_712.service.UserService;
import _712.final_project_712.util.JwtUtil;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import _712.final_project_712.model.dto.UpdateProfileRequest;
import _712.final_project_712.model.dto.UserInfoResponse;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAddressMapper addressMapper;

    @Autowired
    private JwtUtil jwtUtil;

    // 添加验证用的正则表达式
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_]{4,16}$");
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$");

    // 在服务启动时初始化测试账号
    @PostConstruct
    public void init() {
        try {
            // 检查测试用户是否已存在
            User existingUser = QueryChain.of(User.class)
                    .where(User::getName).eq("admin")
                    .one();
            
            if (existingUser == null) {
                // 创建默认的测试账号
                User testUser = new User();
                testUser.setName("admin");
                testUser.setPassword("123456");
                testUser.setEmail("admin@test.com");
                userMapper.insert(testUser);
                System.out.println("测试账号创建成功 - 用户名: admin, 密码: 123456");
            }
        } catch (Exception e) {
            System.err.println("初始化测试账号失败: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        return QueryChain.of(User.class).list();
    }

    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        // 1. 查询用户是否存在
        User user = QueryChain.of(User.class)
                .where(User::getId).eq(userId)
                .one();
                
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 2. 验证原密码
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("原密码错误");
        }
        
        // 3. 更新密码
        user.setPassword(newPassword);
        return userMapper.update(user) > 0;
    }

    @Override
    public String login(String username, String password) {
        // 参数验证
        if (!StringUtils.hasText(username)) {
            throw new RuntimeException("用户名不能为空");
        }
        if (!StringUtils.hasText(password)) {
            throw new RuntimeException("密码不能为空");
        }

        // 查询用户
        User user = QueryChain.of(User.class)
                .where(User::getName).eq(username)
                .one();

        // 验证用户是否存在
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 生成token并返回
        return jwtUtil.generateToken(user.getId(), user.getName());
    }

    @Override
    public boolean register(User user) {
        // 验证用户名
        if (!StringUtils.hasText(user.getName()) || 
            !USERNAME_PATTERN.matcher(user.getName()).matches()) {
            throw new RuntimeException("用户名必须是4-16位字母、数字或下划线");
        }
        
        // 检查用户名是否已存在
        User existingUser = QueryChain.of(User.class)
                .where(User::getName).eq(user.getName())
                .one();
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 验证密码
        if (!StringUtils.hasText(user.getPassword()) || 
            !PASSWORD_PATTERN.matcher(user.getPassword()).matches()) {
            throw new RuntimeException("密码必须是8-20位，且包含字母和数字");
        }
        
        // 验证邮箱
        if (!StringUtils.hasText(user.getEmail()) || 
            !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new RuntimeException("邮箱格式不正确");
        }

        // 使用时间戳作为ID基础
        long timestamp = System.currentTimeMillis();
        // 添加随机数以避免并发冲突
        long random = (long)(Math.random() * 1000);
        user.setId(timestamp + random);
        
        // 实现注册逻辑
        return userMapper.insert(user) > 0;
    }

    @Override
    public String getUserEmail(Long userId) {
        User user = QueryChain.of(User.class)
                .where(User::getId).eq(userId)
                .one();
                
        return user != null ? user.getEmail() : null;
    }
} 