package _712.final_project_712.service;

import _712.final_project_712.model.ReturnOrder;
import _712.final_project_712.model.dto.CreateReturnOrderDTO;
import java.util.List;

public interface ReturnOrderService {
    ReturnOrder getReturnOrderByOrderId(Long orderId);
    void updateReturnStatus(Long returnId, Integer status, String handleNote);
    List<ReturnOrder> getUserReturns(Long userId);
    ReturnOrder createReturnOrder(CreateReturnOrderDTO dto, Long userId);
    ReturnOrder getReturnOrderById(Long returnId);
} 