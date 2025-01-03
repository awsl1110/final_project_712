package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("order_return")
@Schema(description = "退货订单实体类")
public class ReturnOrder {
    @Id(keyType = KeyType.Auto)
    private Long id;
    
    private Long orderId;
    
    private Long userId;
    
    @Schema(description = "退货原因")
    private String returnReason;
    
    @Schema(description = "退款金额")
    private BigDecimal returnAmount;
    
    @Schema(description = "状态：0-待处理，1-已同意，2-已拒绝，3-已完成")
    private Integer status;
    
    @Schema(description = "图片凭证")
    private String images;
    
    private LocalDateTime applyTime;
    
    private LocalDateTime handleTime;
    
    private String handleNote;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 