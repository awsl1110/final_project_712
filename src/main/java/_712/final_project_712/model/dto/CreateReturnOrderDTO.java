package _712.final_project_712.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "创建退货申请请求参数")
public class CreateReturnOrderDTO {
    @Schema(description = "订单ID", required = true)
    private Long orderId;
    
    @Schema(description = "退货原因", required = true)
    private String returnReason;
    
    @Schema(description = "退款金额", required = true)
    private BigDecimal returnAmount;
    
    @Schema(description = "图片URL列表，多个URL用逗号分隔")
    private String images;
} 