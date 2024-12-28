package _712.final_project_712.service.impl;


import _712.final_project_712.mapper.UserMapper;
import _712.final_project_712.model.User;
import _712.final_project_712.service.UserService;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        return QueryChain.of(User.class).list();
    }
} 