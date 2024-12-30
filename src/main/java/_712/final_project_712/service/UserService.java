package _712.final_project_712.service;

import _712.final_project_712.model.LoginResult;
import _712.final_project_712.model.User;

import java.util.List;

public interface UserService {
    /**
     * 获取所有用户列表
     */
    List<User> getAllUsers();
    
    /**
     * 修改用户密码
     * @param userId 用户ID（需要手动指定）
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 是否修改成功
     * @throws RuntimeException 当用户不存在或原密码错误时抛出异常
     */
    boolean updatePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    LoginResult login(String username, String password);
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册成功返回true，否则返回false
     */
    boolean register(User user);
    
    /**
     * 获取用户邮箱
     * @param userId 用户ID
     * @return 用户邮箱
     */
    String getUserEmail(Long userId);
} 