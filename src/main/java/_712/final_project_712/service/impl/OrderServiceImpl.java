package _712.final_project_712.service.impl;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.OrderMapper;
import _712.final_project_712.model.Orders;
import _712.final_project_712.model.dto.OrderDTO;
import _712.final_project_712.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<OrderDTO.OrderInfo> getUserOrders(Long userId) {
        try {
            // 获取用户的订单
            List<OrderDTO.OrderInfo> orders = orderMapper.getUserOrders(userId);
            
            // 获取每个订单的商品信息
            for (OrderDTO.OrderInfo order : orders) {
                List<OrderDTO.OrderItemInfo> items = orderMapper.getOrderItems(order.getId());
                order.setItems(items);
            }
            
            return orders;
        } catch (Exception e) {
            throw new BusinessException("获取订单列表失败：" + e.getMessage());
        }
    }

    @Override
    public OrderDTO.OrderInfo getOrderDetail(Long orderId) {
        // 获取订单基本信息和用户信息
        OrderDTO.OrderInfo orderInfo = orderMapper.getOrderWithUser(orderId);
        if (orderInfo == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 获取订单商品信息
        List<OrderDTO.OrderItemInfo> items = orderMapper.getOrderItems(orderId);
        orderInfo.setItems(items);
        
        return orderInfo;
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(Long orderId, Integer status) {
        // 验证订单状态的有效性
        if (status < 0 || status > 4) {
            throw new BusinessException("无效的订单状态");
        }
        
        // 验证订单是否存在
        Orders existingOrder = orderMapper.selectOneById(orderId);
        if (existingOrder == null) {
            throw new BusinessException("订单不存在");
        }

        Orders order = new Orders();
        order.setId(orderId);
        order.setStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        return orderMapper.update(order) > 0;
    }

    @Override
    @Transactional
    public boolean deleteOrder(Long orderId) {
        // 验证订单是否存在
        Orders existingOrder = orderMapper.selectOneById(orderId);
        if (existingOrder == null) {
            throw new BusinessException("订单不存在");
        }
        return orderMapper.deleteById(orderId) > 0;
    }
} 