package _712.final_project_712.controller;

import _712.final_project_712.model.LoginResult;
import _712.final_project_712.model.User;
import _712.final_project_712.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "登录接口", description = "处理用户登录相关的接口")
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户登录", description = "通过用户名和密码进行登录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "登录成功"),
        @ApiResponse(responseCode = "400", description = "登录失败")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResult> login(
            @Parameter(description = "用户名", required = true)
            @RequestParam String username,
            @Parameter(description = "密码", required = true)
            @RequestParam String password) {
        
        LoginResult result = userService.login(username, password);
        
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @Operation(summary = "用户注册", description = "注册新用户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "注册成功"),
        @ApiResponse(responseCode = "400", description = "注册失败")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // 参数校验
            if (user.getName() == null || user.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("用户名不能为空");
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("密码不能为空");
            }
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("邮箱不能为空");
            }

            // 调用注册服务
            boolean success = userService.register(user);
            
            if (success) {
                return ResponseEntity.ok("注册成功");
            } else {
                return ResponseEntity.badRequest().body("注册失败");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("注册失败: " + e.getMessage());
        }
    }

    @Operation(summary = "检查用户名是否可用", description = "检查用户名是否已被注册")
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(
            @Parameter(description = "要检查的用户名", required = true)
            @RequestParam String username) {
        // TODO: 实现用户名检查逻辑
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "退出登录", description = "用户退出登录")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Parameter(description = "用户ID", required = true)
            @RequestParam Long userId) {
        // TODO: 实现退出登录逻辑
        return ResponseEntity.ok("退出成功");
    }
}