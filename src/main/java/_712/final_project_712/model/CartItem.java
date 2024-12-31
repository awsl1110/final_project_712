package _712.final_project_712.model;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("cart")
@Schema(description = "购物车项目实体")
public class CartItem {
    @Id
    @Schema(description = "购物车项目ID", example = "1")
    private Long id;

    @Column("user_id")
    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Column("product_id")
    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long productId;

    @Column("quantity")
    @Schema(description = "商品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer quantity;
    
    @Column("selected")
    @Schema(description = "是否选中：0-未选中，1-已选中", example = "1")
    private Integer selected;
    
    @Column("create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Column("update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    @Column(ignore = true)
    @Schema(description = "商品名称（非数据库字段）")
    private String productName;
}