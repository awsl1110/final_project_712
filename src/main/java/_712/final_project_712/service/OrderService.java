package _712.final_project_712.service;

import _712.final_project_712.model.Orders;
import _712.final_project_712.model.dto.OrderQueryDTO;
import com.mybatisflex.core.paginate.Page;

public interface OrderService {
    Page<Orders> getOrderList(OrderQueryDTO queryDTO);
    Orders getOrderDetail(Long orderId);
    boolean updateOrderStatus(Long orderId, Integer status);
    boolean deleteOrder(Long orderId);
} 