package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("orders")
@Schema(description = "订单实体类")
public class Orders {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "订单ID", example = "1")
    private Long id;
    
    private Long userId;
    
    private BigDecimal totalAmount;
    
    private BigDecimal discountAmount;
    
    private BigDecimal payAmount;
    
    private Long userCouponId;
    
    private Integer status;
    
    private String address;
    
    private String receiverName;
    
    private String receiverPhone;
    
    private String remark;
    
    private LocalDateTime payTime;
    
    private LocalDateTime shipTime;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 