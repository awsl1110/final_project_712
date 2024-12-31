package _712.final_project_712.service;

import _712.final_project_712.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    boolean updatePassword(Long userId, String oldPassword, String newPassword);
    String login(String username, String password);
    boolean register(User user);
    String getUserEmail(Long userId);
} 