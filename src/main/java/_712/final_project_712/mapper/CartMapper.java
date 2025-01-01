package _712.final_project_712.mapper;

import _712.final_project_712.model.CartItem;
import _712.final_project_712.model.dto.CartDTO;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<CartItem> {
    
    @Select("SELECT " +
            "c.id, " +
            "c.user_id AS userId, " +
            "c.product_id AS productId, " +
            "c.quantity, " +
            "c.selected, " +
            "c.update_time AS updateTime, " +
            "p.name AS 'product.productName', " +
            "p.price AS 'product.productPrice', " +
            "p.image_url AS 'product.productImage', " +
            "p.description AS 'product.productDescription', " +
            "p.stock AS 'product.productStock', " +
            "p.status AS 'product.productStatus' " +
            "FROM cart c " +
            "LEFT JOIN product p ON c.product_id = p.id " +
            "WHERE c.user_id = #{userId} " +
            "ORDER BY c.update_time DESC")
    List<CartDTO> getCartList(Long userId);
} 