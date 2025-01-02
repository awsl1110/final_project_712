package _712.final_project_712.service;

public interface EmailService {
    /**
     * 发送验证码
     * @param email 邮箱地址
     * @return 是否发送成功
     */
    boolean sendCaptcha(String email);
} 