package _712.final_project_712.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "购物车信息")
public class CartDTO {
    @Schema(description = "购物车ID")
    private Long id;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "购买数量")
    private Integer quantity;
    
    @Schema(description = "是否选中")
    private Integer selected;
    
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
    
    @Schema(description = "商品信息")
    private ProductInfo product;
    
    @Data
    @Schema(description = "商品信息")
    public static class ProductInfo {
        @Schema(description = "商品名称")
        private String productName;
        
        @Schema(description = "商品价格")
        private BigDecimal productPrice;
        
        @Schema(description = "商品图片")
        private String productImage;
        
        @Schema(description = "商品描述")
        private String productDescription;
        
        @Schema(description = "商品库存")
        private Integer productStock;
        
        @Schema(description = "商品状态")
        private Integer productStatus;
    }
} 