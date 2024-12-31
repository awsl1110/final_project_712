package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("product")
@Schema(description = "商品实体类")
public class Product {
    @Id
    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "商品名称", required = true)
    private String name;

    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "商品价格", required = true)
    private BigDecimal price;

    @Schema(description = "库存数量", required = true)
    private Integer stock;

    @Schema(description = "分类ID", required = true)
    private Long categoryId;

    @Schema(description = "品牌")
    private String brand;

    @Schema(description = "型号")
    private String model;

    @Schema(description = "规格")
    private String specifications;

    @Schema(description = "商品图片URL")
    private String imageUrl;

    @Schema(description = "商品状态：0-下架，1-上架", required = true)
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 