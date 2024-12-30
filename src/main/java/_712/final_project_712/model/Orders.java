package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("orders")
public class Orders {
    @Id
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalAmount;
    private Integer status; // 0-待付款，1-待发货，2-待收货，3-已完成，4-已取消
    private String address;
    private String receiverName;
    private String receiverPhone;
    private String remark;
    private LocalDateTime payTime;
    private LocalDateTime shipTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 