package _712.final_project_712.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FavoriteDTO {
    private Long id;
    private Long productId;
    private LocalDateTime createTime;
    private String productName;
} 