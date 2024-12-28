package _712.final_project_712.controller;

import _712.final_project_712.model.LoginResult;
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
        
        try {
            LoginResult result = userService.login(username, password);
            // 不管成功失败都返回200状态码，让前端根据success字段判断
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(LoginResult.fail("系统错误：" + e.getMessage()));
        }
    }
}