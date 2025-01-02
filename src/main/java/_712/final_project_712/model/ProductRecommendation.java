package _712.final_project_712.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductRecommendation {
    private Long id;
    private Long productId;
    private Long userId;
    private Integer score;  // 推荐分数
    private String recommendationType;  // 推荐类型(USER_BASED/ITEM_BASED/POPULAR)
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 