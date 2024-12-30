package _712.final_project_712.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "商品分类DTO")
public class CategoryDTO {
    @Schema(description = "分类ID")
    private Long id;
    
    @Schema(description = "分类名称")
    private String name;
    
    @Schema(description = "父分类ID")
    private Long parentId;
    
    @Schema(description = "层级")
    private Integer level;
    
    @Schema(description = "排序")
    private Integer sortOrder;
    
    @Schema(description = "子分类")
    private List<CategoryDTO> children;
} 