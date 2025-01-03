package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.ReturnOrderMapper;
import _712.final_project_712.model.ReturnOrder;
import _712.final_project_712.model.dto.CreateReturnOrderDTO;
import _712.final_project_712.service.ReturnOrderService;
import com.mybatisflex.core.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static _712.final_project_712.model.table.ReturnOrderTableDef.RETURN_ORDER;

@Service
public class ReturnOrderServiceImpl implements ReturnOrderService {

    @Autowired
    private ReturnOrderMapper returnOrderMapper;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Override
    @Transactional
    public ReturnOrder createReturnOrder(CreateReturnOrderDTO dto, Long userId) {
        // 检查是否已存在退货记录
        ReturnOrder existingOrder = returnOrderMapper.selectOneByQuery(
            QueryWrapper.create()
                .where(RETURN_ORDER.ORDER_ID.eq(dto.getOrderId()))
                .and(RETURN_ORDER.USER_ID.eq(userId))
        );

        if (existingOrder != null) {
            throw new RuntimeException("退货记录已存在");
        }

        // 创建新的退货记录
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setOrderId(dto.getOrderId());
        returnOrder.setUserId(userId);
        returnOrder.setReturnReason(dto.getReturnReason());
        returnOrder.setReturnAmount(dto.getReturnAmount());
        
        // 处理图片URL，转换为JSON格式
        try {
            String images = dto.getImages();
            if (images != null && !images.trim().isEmpty()) {
                // 将逗号分隔的URL转换为JSON数组
                String[] imageArray = images.split(",");
                String jsonImages = objectMapper.writeValueAsString(imageArray);
                returnOrder.setImages(jsonImages);
            }
        } catch (Exception e) {
            throw new RuntimeException("处理图片URL失败", e);
        }

        LocalDateTime now = LocalDateTime.now();
        returnOrder.setStatus(0); // 0-待处理
        returnOrder.setApplyTime(now);  // 设置申请时间
        returnOrder.setCreateTime(now);
        returnOrder.setUpdateTime(now);

        // 保存记录并获取生成的ID
        int rows = returnOrderMapper.insert(returnOrder);
        if (rows != 1) {
            throw new RuntimeException("创建退货记录失败");
        }
        
        // 确保ID已经被设置
        if (returnOrder.getId() == null) {
            throw new RuntimeException("退货记录ID生成失败");
        }
        
        return returnOrder;
    }

    @Override
    public ReturnOrder getReturnOrderById(Long returnId) {
        return returnOrderMapper.selectOneById(returnId);
    }
} 