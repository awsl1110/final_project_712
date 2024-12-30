package _712.final_project_712.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改用户信息请求")
public class UpdateUserRequest {
    
    @Schema(description = "用户名", example = "张三")
    private String name;
    
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;
} 