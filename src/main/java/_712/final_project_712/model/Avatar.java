package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("avatar")
@Schema(description = "用户头像实体类")
public class Avatar {
    
    @Id
    @Schema(description = "头像ID", example = "1")
    private Long id;
    
    @Schema(description = "用户ID", example = "10001", required = true)
    private Long userId;
    
    @Schema(description = "文件名", example = "avatar.jpg", required = true)
    private String fileName;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @RelationOneToOne(selfField = "userId", targetField = "id")
    @Schema(description = "关联的用户信息")
    private User user;
} 