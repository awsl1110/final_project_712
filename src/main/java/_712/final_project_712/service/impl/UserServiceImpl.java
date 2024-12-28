package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.UserMapper;
import _712.final_project_712.model.LoginResult;
import _712.final_project_712.model.User;
import _712.final_project_712.service.UserService;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

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
        
        // TODO: 生成JWT token
        String token = "dummy-token-" + System.currentTimeMillis();
        
        // 返回成功结果
        return LoginResult.success(token, user.getId());
    }

    @Override
    public boolean register(User user) {
        // 实现注册逻辑
        return userMapper.insert(user) > 0;
    }
} 