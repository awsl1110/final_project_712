package _712.final_project_712.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
public class ReviewQueryDTO {
    private Long userId;
    private Long productId;
    private Long orderId;
    private Integer rating;
    private Integer status;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    
    private Integer pageNum = 1;
    private Integer pageSize = 10;
} 