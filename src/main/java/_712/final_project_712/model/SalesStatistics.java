package _712.final_project_712.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SalesStatistics {
    private Long productId;
    private String productName;
    private Integer salesCount;  // 销售数量
    private BigDecimal salesAmount;  // 销售金额
    private LocalDateTime statisticsTime;  // 统计时间
    private String timeRange;  // 时间范围(日/周/月)
} 