package _712.final_project_712.service;

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
} 