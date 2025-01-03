package _712.final_project_712.service;

import _712.final_project_712.model.ReturnOrder;
import java.util.List;

public interface ReturnOrderService {
    ReturnOrder getReturnOrderByOrderId(Long orderId);
    void updateReturnStatus(Long returnId, Integer status, String handleNote);
    List<ReturnOrder> getUserReturns(Long userId);
} 