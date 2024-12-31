package _712.final_project_712.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Coupon {
    private Long id;
    private String name;
    private Integer type;
    private BigDecimal value;
    private BigDecimal minAmount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer total;
    private Integer remain;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 