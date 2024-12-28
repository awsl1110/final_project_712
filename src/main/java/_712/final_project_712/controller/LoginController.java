package _712.final_project_712.controller;

import _712.final_project_712.model.LoginResult;
import _712.final_project_712.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "登录接口", description = "处理用户登录相关的接口")
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户登录", description = "通过用户名、密码和验证码进行登录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "登录成功"),
        @ApiResponse(responseCode = "400", description = "登录失败")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResult> login(
            @Parameter(description = "用户名", required = true)
            @RequestParam String username,
            @Parameter(description = "密码", required = true)
            @RequestParam String password,
            @Parameter(description = "验证码", required = true)
            @RequestParam String captcha,
            HttpSession session) {
        
        try {
            // 获取session中的验证码
            String realCaptcha = (String) session.getAttribute("captcha");
            System.out.println("Session ID: " + session.getId());
            System.out.println("Input Captcha: " + captcha);
            System.out.println("Real Captcha: " + realCaptcha);
            
            // 验证码为空或已过期
            if (realCaptcha == null) {
                return ResponseEntity.ok(LoginResult.fail("验证码已过期，请重新获取"));
            }
            
            // 验证码不匹配（忽略大小写）
            if (!realCaptcha.equalsIgnoreCase(captcha)) {
                return ResponseEntity.ok(LoginResult.fail("验证码错误"));
            }
            
            // 验证码正确，清除session中的验证码
            session.removeAttribute("captcha");
            
            // 调用登录服务
            LoginResult result = userService.login(username, password);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(LoginResult.fail("系统错误：" + e.getMessage()));
        }
    }
}