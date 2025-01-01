package _712.final_project_712.model;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "供应商商品关联")
@Table("supplier_product")
public class SupplierProduct {
    
    @Id
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "供应商ID")
    private Long supplierId;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "供应价格")
    private BigDecimal price;

    @Schema(description = "创建时间")
    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @Column(onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updateTime;
} 