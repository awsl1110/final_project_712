package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("coupon")
@Schema(description = "优惠券实体类")
public class Coupon {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "优惠券ID")
    private Long id;

    @Schema(description = "优惠券名称")
    private String name;

    @Schema(description = "类型：1-满减券，2-折扣券")
    private Integer type;

    @Schema(description = "优惠券面值")
    private BigDecimal value;

    @Schema(description = "最低使用金额")
    private BigDecimal minAmount;

    @Schema(description = "生效时间")
    private LocalDateTime startTime;

    @Schema(description = "失效时间")
    private LocalDateTime endTime;

    @Schema(description = "发行总量")
    private Integer total;

    @Schema(description = "剩余数量")
    private Integer remain;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 