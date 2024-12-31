package _712.final_project_712.model;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("cart")
public class CartItem {
    @Id
    private Long id;
    
    @Column("user_id")
    private Long userId;
    
    @Column("product_id")
    private Long productId;
    
    private Integer quantity;
    
    private Integer selected;
    
    @Column("create_time")
    private LocalDateTime createTime;
    
    @Column("update_time")
    private LocalDateTime updateTime;
}