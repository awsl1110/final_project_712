package _712.final_project_712.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActivityDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private List<ActivityProductDTO> products;
    
    @Data
    public static class ActivityProductDTO {
        private Long productId;
        private String productName;
        private String productImage;
        private BigDecimal originalPrice;
        private BigDecimal activityPrice;
        private Integer stock;
    }
} 