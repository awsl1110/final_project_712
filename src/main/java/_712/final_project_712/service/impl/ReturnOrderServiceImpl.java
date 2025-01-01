package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.ReturnOrderMapper;
import _712.final_project_712.model.ReturnOrder;
import _712.final_project_712.service.ReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReturnOrderServiceImpl implements ReturnOrderService {

    @Autowired
    private ReturnOrderMapper returnOrderMapper;

    @Override
    public ReturnOrder getReturnOrderByOrderId(Long orderId) {
        return returnOrderMapper.findByOrderId(orderId);
    }

    @Override
    public void updateReturnStatus(Long returnId, Integer status, String handleNote) {
        if (status < 0 || status > 3) {
            throw new IllegalArgumentException("无效的状态值");
        }
        int rows = returnOrderMapper.updateStatus(returnId, status, handleNote);
        if (rows == 0) {
            throw new RuntimeException("退货记录不存在");
        }
    }

    @Override
    public List<ReturnOrder> getUserReturns(Long userId) {
        return returnOrderMapper.findByUserId(userId);
    }
} 