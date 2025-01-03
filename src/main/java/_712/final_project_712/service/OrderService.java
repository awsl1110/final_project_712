package _712.final_project_712.service;

import _712.final_project_712.model.Orders;
import _712.final_project_712.model.dto.OrderDTO;
import java.util.List;

public interface OrderService {
    List<OrderDTO.OrderInfo> getUserOrders(Long userId);
    OrderDTO.OrderInfo getOrderDetail(Long orderId);
    boolean updateOrderStatus(Long orderId, Integer status);
    boolean deleteOrder(Long orderId);
    
    /**
     * 创建订单
     */
    OrderDTO.OrderInfo createOrder(Long userId, OrderDTO.CreateOrderRequest request);
} 