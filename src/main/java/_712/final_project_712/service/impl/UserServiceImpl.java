package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.UserMapper;
import _712.final_project_712.model.LoginResult;
import _712.final_project_712.model.User;
import _712.final_project_712.service.UserService;
import _712.final_project_712.util.JwtUtil;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

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
    public LoginResult login(String username, String password) {
        // 参数校验
        if (!StringUtils.hasText(username)) {
            return LoginResult.fail("用户名不能为空");
        }
        if (!StringUtils.hasText(password)) {
            return LoginResult.fail("密码不能为空");
        }
        
        try {
            // 查询用户
            User user = QueryChain.of(User.class)
                    .where(User::getName).eq(username)
                    .one();
                    
            // 用户不存在
            if (user == null) {
                return LoginResult.fail("用户不存在");
            }
            
            // 密码验证
            if (!password.equals(user.getPassword())) {
                return LoginResult.fail("密码错误");
            }
            
            // 生成JWT token
            String token = jwtUtil.generateToken(user.getId(), user.getName());
            
            // 返回成功结果
            return LoginResult.success(token, user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return LoginResult.fail("登录失败：" + e.getMessage());
        }
    }

    @Override
    public boolean register(User user) {
        // 实现注册逻辑
        return userMapper.insert(user) > 0;
    }
} 