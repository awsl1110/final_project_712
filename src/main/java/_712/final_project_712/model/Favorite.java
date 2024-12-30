package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("product_favorite")
public class Favorite {
    
    @Id
    private Long id;
    
    private Long userId;
    
    private Long productId;
    
    private LocalDateTime createTime;

} 