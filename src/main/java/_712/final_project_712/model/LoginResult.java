package _712.final_project_712.model;

import lombok.Data;

@Data
public class LoginResult {
    private boolean success;
    private String message;
    private String token;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public static LoginResult success(String token, Long userId) {
        LoginResult result = new LoginResult();
        result.setSuccess(true);
        result.setToken(token);
        result.setMessage("登录成功");
        return result;
    }

    public static LoginResult fail(String message) {
        LoginResult result = new LoginResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    @Override
    public String toString() {
        return String.format("LoginResult{success=%s, message='%s', token='%s'}",
                success,
                message,
                token != null ? token : "null");
    }
} 