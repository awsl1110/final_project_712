package _712.final_project_712.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String categoryName;  // 额外添加分类名称
    private String brand;
    private String model;
    private String specifications;
    private String imageUrl;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 