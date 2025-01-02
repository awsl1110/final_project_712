package _712.final_project_712.model.dto;

import _712.final_project_712.model.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "收藏商品DTO")
public class FavoriteDTO {
    @Schema(description = "收藏ID")
    private Long id;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "收藏时间")
    private LocalDateTime createTime;
    
    @Schema(description = "商品")
    private Product product;
} 