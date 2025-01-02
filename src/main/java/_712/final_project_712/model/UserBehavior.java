package _712.final_project_712.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserBehavior {
    private Long id;
    private Long userId;
    private Long productId;
    private String behaviorType;  // VIEW/CART/PURCHASE/FAVORITE
    private LocalDateTime createTime;
} 