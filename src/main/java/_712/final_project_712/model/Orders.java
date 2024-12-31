package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("orders")
@Schema(description = "订单实体类")
public class Orders {
    @Id
    @Schema(description = "订单ID", example = "1")
    private Long id;

    @Schema(description = "订单编号", example = "202403150001", required = true)
    private String orderNo;

    @Schema(description = "用户ID", example = "10001", required = true)
    private Long userId;

    @Schema(description = "订单总金额", example = "9999.99", required = true)
    private BigDecimal totalAmount;

    @Schema(description = "订单状态：0-待付款，1-待发货，2-待收货，3-已完成，4-已取消", example = "0", required = true)
    private Integer status;

    @Schema(description = "收货地址", example = "广东省深圳市南山区科技园南区1号楼", required = true)
    private String address;

    @Schema(description = "收货人姓名", example = "张三", required = true)
    private String receiverName;

    @Schema(description = "收货人电话", example = "13800138000", required = true)
    private String receiverPhone;

    @Schema(description = "订单备注", example = "请尽快发货")
    private String remark;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "发货时间")
    private LocalDateTime shipTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 