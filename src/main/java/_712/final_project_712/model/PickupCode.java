package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("pickup_code")
public class PickupCode {
    @Id
    private Long id;
    private Long userId;
    private Long orderId;
    private String pickupCode;
    private Integer status;  // 0-未取件，1-已取件
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 