package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.ReturnOrderMapper;
import _712.final_project_712.model.ReturnOrder;
import _712.final_project_712.service.ReturnOrderService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import static _712.final_project_712.model.table.ReturnOrderTableDef.RETURN_ORDER;

@Service
public class ReturnOrderServiceImpl implements ReturnOrderService {

    @Autowired
    private ReturnOrderMapper returnOrderMapper;

    @Override
    public ReturnOrder getReturnOrderByOrderId(Long orderId) {
        return returnOrderMapper.selectOneByQuery(
            QueryWrapper.create()
                .where(RETURN_ORDER.ORDER_ID.eq(orderId))
        );
    }

    @Override
    public void updateReturnStatus(Long returnId, Integer status, String handleNote) {
        if (status < 0 || status > 3) {
            throw new IllegalArgumentException("无效的状态值");
        }
        
        QueryWrapper query = QueryWrapper.create()
            .where(RETURN_ORDER.ID.eq(returnId));
            
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setStatus(status);
        returnOrder.setHandleNote(handleNote);
        returnOrder.setHandleTime(LocalDateTime.now());
        returnOrder.setUpdateTime(LocalDateTime.now());
        
        int rows = returnOrderMapper.updateByQuery(returnOrder, query);
        if (rows == 0) {
            throw new RuntimeException("退货记录不存在");
        }
    }

    @Override
    public List<ReturnOrder> getUserReturns(Long userId) {
        return returnOrderMapper.selectListByQuery(
            QueryWrapper.create()
                .where(RETURN_ORDER.USER_ID.eq(userId))
                .orderBy(RETURN_ORDER.CREATE_TIME.desc())
        );
    }
} 