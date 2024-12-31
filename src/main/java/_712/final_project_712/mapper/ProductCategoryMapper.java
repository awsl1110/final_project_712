package _712.final_project_712.mapper;

import _712.final_project_712.model.ProductCategory;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
    @Select("SELECT * FROM product_category")
    List<ProductCategory> findAllCategories();
} 