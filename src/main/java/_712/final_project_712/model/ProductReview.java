package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("product_review")
public class ProductReview {
    @Id
    private Long id;
    private Long userId;
    private Long productId;
    private Long orderId;
    private Integer rating;
    private String content;
    private String images;  // JSON字符串存储图片URL数组
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 