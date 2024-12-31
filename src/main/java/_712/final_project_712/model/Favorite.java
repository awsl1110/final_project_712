package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("product_favorite")
@Schema(description = "商品收藏实体类")
public class Favorite {
    
    @Id
    @Schema(description = "收藏ID", example = "1")
    private Long id;
    
    @Schema(description = "用户ID", example = "10001", required = true)
    private Long userId;
    
    @Schema(description = "商品ID", example = "2001", required = true)
    private Long productId;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 