package _712.final_project_712.model;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("user_coupon")
public class UserCoupon {
    @Id
    private Long id;
    
    @Column("user_id")
    private Long userId;
    
    @Column("coupon_id")
    private Long couponId;
    
    @Column("status")
    private Integer status; // 0-未使用，1-已使用，2-已过期
    
    @Column("use_time")
    private LocalDateTime useTime;
    
    @Column("order_id")
    private Long orderId;
    
    @Column("create_time")
    private LocalDateTime createTime;
    
    @Column("update_time")
    private LocalDateTime updateTime;
}