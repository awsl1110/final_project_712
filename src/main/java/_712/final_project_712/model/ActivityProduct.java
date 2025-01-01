package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("tb_activity_product")
public class ActivityProduct {
    @Id
    private Long id;
    private Long activityId;
    private Long productId;
    private BigDecimal activityPrice;
    private Integer stock;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 