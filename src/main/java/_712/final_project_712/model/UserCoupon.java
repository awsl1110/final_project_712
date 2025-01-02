package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("user_coupon")
@Schema(description = "用户优惠券实体类")
public class UserCoupon {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "用户优惠券ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "优惠券ID")
    private Long couponId;

    @Schema(description = "状态：0-未使用，1-已使用，2-已过期")
    private Integer status;

    @Schema(description = "使用时间")
    private LocalDateTime useTime;

    @Schema(description = "使用的订单ID")
    private Long orderId;

    @Schema(description = "领取时间")
    private LocalDateTime createTime;
}