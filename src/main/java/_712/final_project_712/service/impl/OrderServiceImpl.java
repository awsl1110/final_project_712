package _712.final_project_712.service.impl;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.OrderMapper;
import _712.final_project_712.model.Orders;
import _712.final_project_712.model.dto.OrderQueryDTO;
import _712.final_project_712.service.OrderService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static _712.final_project_712.model.table.OrdersTableDef.ORDERS;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Page<Orders> getOrderList(OrderQueryDTO queryDTO) {
        // 确保分页参数有效
        if (queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1) {
            queryDTO.setPageNum(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(10);
        }

        try {
            QueryWrapper queryWrapper = QueryWrapper.create()
                .select("*")
                .from("orders")
                .orderBy("create_time desc");

            System.out.println("Generated SQL: " + queryWrapper.toSQL());
            return orderMapper.paginate(queryDTO.getPageNum(), queryDTO.getPageSize(), queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取订单列表失败：" + e.getMessage());
        }
    }

    @Override
    public Orders getOrderDetail(Long orderId) {
        return orderMapper.selectOneById(orderId);
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