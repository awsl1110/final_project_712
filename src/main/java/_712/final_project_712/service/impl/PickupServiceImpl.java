package _712.final_project_712.service.impl;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.OrderMapper;
import _712.final_project_712.mapper.PickupCodeMapper;
import _712.final_project_712.model.Orders;
import _712.final_project_712.model.PickupCode;
import _712.final_project_712.service.PickupService;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PickupServiceImpl implements PickupService {

    @Autowired
    private PickupCodeMapper pickupCodeMapper;
    
    @Autowired
    private OrderMapper orderMapper;
    
    private final Random random = new Random();

    @Override
    @Transactional
    public PickupCode generatePickupCode(Long userId) {
        System.out.println("开始生成取件码，用户ID: " + userId);
        
        // 查找用户的待收货订单
        System.out.println("正在查询订单，条件：user_id = " + userId + ", status = 2");
        Orders order = QueryChain.of(Orders.class)
                .where("user_id = ?", userId)
                .and("status = ?", 2)  // 2-待收货
                .one();
        System.out.println("SQL查询结果: " + (order != null ? "找到订单" : "未找到订单"));
        
        if (order == null) {
            throw new BusinessException("您当前没有订单到货");
        }
        
        // 检查是否已经生成过取件码
        PickupCode existingCode = QueryChain.of(PickupCode.class)
                .where("order_id = ?", order.getId())
                .and("status = ?", 0)  // 0-未取件
                .one();
                
        System.out.println("已存在的取件码: " + existingCode);  // 打印已存在的取件码
                
        if (existingCode != null) {
            return existingCode;
        }
        
        // 生成新的取件码
        String pickupCode = generateCode();
        System.out.println("生成的新取件码: " + pickupCode);
        
        // 保存取件码
        PickupCode code = new PickupCode();
        code.setUserId(userId);
        code.setOrderId(order.getId());
        code.setPickupCode(pickupCode);
        code.setStatus(0);  // 0-未取件
        code.setCreateTime(LocalDateTime.now());
        code.setUpdateTime(LocalDateTime.now());
        
        pickupCodeMapper.insert(code);
        System.out.println("保存的取件码: " + code);
        
        return code;
    }
    
    @Override
    @Transactional
    public void pickup(String phone, String pickupCode) {
        // 查找取件码
        PickupCode code = QueryChain.of(PickupCode.class)
                .where("pickup_code = ?", pickupCode)
                .and("status = ?", 0)  // 0-未取件
                .one();
                
        if (code == null) {
            throw new BusinessException("取件码无效或已使用");
        }
        
        // 验证手机号
        Orders order = orderMapper.selectOneById(code.getOrderId());
        if (!phone.equals(order.getReceiverPhone())) {
            throw new BusinessException("手机号码不匹配");
        }
        
        // 更新取件码状态
        code.setStatus(1);  // 1-已取件
        code.setUpdateTime(LocalDateTime.now());
        pickupCodeMapper.update(code);
        
        // 更新订单状态
        order.setStatus(3);  // 3-已完成
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.update(order);
    }
    
    private String generateCode() {
        // 生成格式：xxx-x-xxxx
        int part1 = random.nextInt(999) + 1;  // 1-999
        int part2 = random.nextInt(6) + 1;    // 1-6
        int part3 = random.nextInt(10000);    // 0-9999
        
        return String.format("%03d-%d-%04d", part1, part2, part3);
    }
} 