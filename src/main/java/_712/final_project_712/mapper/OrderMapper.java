package _712.final_project_712.mapper;

import _712.final_project_712.model.Orders;
import _712.final_project_712.model.dto.OrderDTO;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
    
    @Select("SELECT o.*, u.name as user_name, u.email as user_email, " +
            "o.receiver_name, o.receiver_phone, o.address " +
            "FROM orders o " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "WHERE o.user_id = #{userId} " +
            "ORDER BY o.create_time DESC")
    List<OrderDTO.OrderInfo> getUserOrders(Long userId);
    
    @Select("SELECT o.*, u.name as user_name, u.email as user_email, " +
            "o.receiver_name, o.receiver_phone, o.address " +
            "FROM orders o " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "ORDER BY o.create_time DESC")
    List<OrderDTO.OrderInfo> getAllOrders();
    
    @Select("SELECT o.*, u.name as user_name, u.email as user_email, " +
            "o.receiver_name, o.receiver_phone, o.address " +
            "FROM orders o " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "WHERE o.id = #{orderId}")
    OrderDTO.OrderInfo getOrderWithUser(Long orderId);
    
    @Select("SELECT oi.*, p.name as product_name, p.price as product_price, " +
            "p.image_url as product_image " +
            "FROM order_items oi " +
            "LEFT JOIN product p ON oi.product_id = p.id " +
            "WHERE oi.order_id = #{orderId}")
    List<OrderDTO.OrderItemInfo> getOrderItems(Long orderId);
} 