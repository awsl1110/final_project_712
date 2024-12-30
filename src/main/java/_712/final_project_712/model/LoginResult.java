package _712.final_project_712.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResult {
    private Integer code;
    private String message;
    private String token;

    public LoginResult(boolean success, String message, String token) {
        this.code = success ? 200 : 400;
        this.message = message;
        this.token = token;
    }

    public static LoginResult success(String token) {
        return new LoginResult(true, "登录成功", token);
    }

    public static LoginResult fail(String message) {
        return new LoginResult(false, message, null);
    }
} 