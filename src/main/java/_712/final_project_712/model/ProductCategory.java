package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("product_category")
@Schema(description = "商品分类实体类")
public class ProductCategory {
    @Id
    @Schema(description = "分类ID", example = "1")
    private Long id;

    @Schema(description = "分类名称", example = "笔记本电脑", required = true)
    private String name;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 