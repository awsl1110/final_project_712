package _712.final_project_712.mapper;

import _712.final_project_712.model.Product;
import _712.final_project_712.model.dto.ProductDTO;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    @Select("SELECT p.*, pc.name as category_name " +
            "FROM product p " +
            "LEFT JOIN product_category pc ON p.category_id = pc.id " +
            "WHERE p.category_id = #{categoryId} " +
            "AND p.status = 1")
    List<ProductDTO> findByCategoryId(Long categoryId);
} 