package _712.final_project_712.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SalesStatistics {
    private Long productId;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private Integer salesCount;
    private BigDecimal salesAmount;
    private LocalDateTime statisticsTime;
    private String timeRange;  // DAY/MONTH/CATEGORY
} 