package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.ReturnOrderMapper;
import _712.final_project_712.model.ReturnOrder;
import _712.final_project_712.service.ReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnOrderServiceImpl implements ReturnOrderService {

    @Autowired
    private ReturnOrderMapper returnOrderMapper;

    @Override
    public ReturnOrder getReturnOrderByOrderId(Long orderId) {
        return returnOrderMapper.findByOrderId(orderId);
    }
} 