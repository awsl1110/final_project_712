package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("product_category")
public class ProductCategory {
    @Id
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 