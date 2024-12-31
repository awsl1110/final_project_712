package _712.final_project_712.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "评价查询参数")
public class ReviewQueryDTO {
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "订单ID")
    private Long orderId;
    
    @Schema(description = "评分")
    private Integer rating;
    
    @Schema(description = "状态(0-隐藏,1-显示)")
    private Integer status;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "页码")
    private Integer pageNum = 1;
    
    @Schema(description = "每页数量")
    private Integer pageSize = 10;
} 