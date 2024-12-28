package _712.final_project_712.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户对象")
public class User {
    private int id;
    private String name;
}
