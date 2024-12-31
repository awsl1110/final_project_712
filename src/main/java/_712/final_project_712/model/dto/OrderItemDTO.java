package _712.final_project_712.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "订单商品数据传输对象")
public class OrderItemDTO {
    @Schema(description = "订单商品ID")
    private Long id;
    
    @Schema(description = "订单ID")
    private Long orderId;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "商品价格")
    private BigDecimal productPrice;
    
    @Schema(description = "商品图片")
    private String productImage;
    
    @Schema(description = "购买数量")
    private Integer quantity;
    
    @Schema(description = "商品小计")
    private BigDecimal subtotal;
} 