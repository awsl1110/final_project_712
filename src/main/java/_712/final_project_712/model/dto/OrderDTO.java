package _712.final_project_712.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    @Data
    @Schema(description = "创建订单请求")
    public static class CreateOrderRequest {
        @Schema(description = "购物车ID列表")
        private List<Long> cartIds;
        
        @Schema(description = "收货地址ID")
        private Long addressId;
        
        @Schema(description = "备注")
        private String remark;
    }

    @Data
    @Schema(description = "订单信息")
    public static class OrderInfo {
        @Schema(description = "订单ID")
        private Long id;
        
        @Schema(description = "用户ID")
        private Long userId;
        
        @Schema(description = "用户名")
        private String userName;
        
        @Schema(description = "用户邮箱") 
        private String userEmail;
        
        @Schema(description = "订单总金额")
        private BigDecimal totalAmount;
        
        @Schema(description = "订单状态:0-待付款,1-待发货,2-待收货,3-已完成,4-已取消")
        private Integer status;
        
        @Schema(description = "收货地址ID")
        private Long addressId;
        
        @Schema(description = "备注")
        private String remark;
        
        @Schema(description = "订单商品列表")
        private List<OrderItemInfo> items;
        
        @Schema(description = "创建时间")
        private LocalDateTime createTime;
        
        @Schema(description = "更新时间")
        private LocalDateTime updateTime;
    }

    @Data
    @Schema(description = "订单商品信息")
    public static class OrderItemInfo {
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
} 