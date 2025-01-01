package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("cart")
public class CartItem {
    @Id
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Integer selected;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 非数据库字段，用于显示
    private transient String productName;
}