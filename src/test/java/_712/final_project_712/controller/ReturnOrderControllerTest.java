package _712.final_project_712.controller;

import _712.final_project_712.mapper.ReturnOrderMapper;
import _712.final_project_712.model.ReturnOrder;
import _712.final_project_712.model.dto.CreateReturnOrderDTO;
import _712.final_project_712.service.ReturnOrderService;
import _712.final_project_712.util.JwtUtil;
import com.mybatisflex.core.query.QueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static _712.final_project_712.model.table.ReturnOrderTableDef.RETURN_ORDER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class ReturnOrderControllerTest {

    @Autowired
    private ReturnOrderController returnOrderController;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ReturnOrderService returnOrderService;

    @Autowired
    private ReturnOrderMapper returnOrderMapper;

    private String validToken;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        // 设置测试数据
        validToken = "Bearer test_token";
        testUserId = 1001L;

        // Mock JWT验证
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(testUserId);

        // 清理测试数据
        try {
            // 先删除可能存在的记录
            returnOrderMapper.deleteByQuery(
                QueryWrapper.create()
                    .where(RETURN_ORDER.USER_ID.eq(testUserId))
                    .or(RETURN_ORDER.ORDER_ID.in(1001L, 1002L, 1003L))
            );
        } catch (Exception e) {
            // 记录异常但不中断测试
            e.printStackTrace();
        }
    }

    @Test
    void testCreateReturnOrder() {
        // 准备测试数据
        CreateReturnOrderDTO dto = new CreateReturnOrderDTO();
        dto.setOrderId(1003L);
        dto.setReturnReason("商品质量问题");
        dto.setReturnAmount(new BigDecimal("1999.99"));
        dto.setImages("http://example.com/image1.jpg");

        // 测试创建退货申请
        var result = returnOrderController.createReturnOrder(dto, validToken);

        // 验证结果
        assertEquals(200, result.getCode(), "创建退货申请应该成功");
        assertNotNull(result.getData(), "返回的数据不应该为空");
        assertEquals(dto.getOrderId(), result.getData().getOrderId());
        assertEquals(dto.getReturnReason(), result.getData().getReturnReason());
        assertEquals(0, dto.getReturnAmount().compareTo(result.getData().getReturnAmount()));
    }

    @Test
    void testGetReturnOrder() {
        // 先创建一个退货记录
        CreateReturnOrderDTO dto = new CreateReturnOrderDTO();
        dto.setOrderId(1001L);
        dto.setReturnReason("测试查询详情");
        dto.setReturnAmount(new BigDecimal("100"));
        var createResult = returnOrderController.createReturnOrder(dto, validToken);
        
        // 确保创建成功
        assertEquals(200, createResult.getCode());
        assertNotNull(createResult.getData());
        
        // 测试查询退货信息
        var result = returnOrderController.getReturnOrder(validToken);

        // 验证结果
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(testUserId, result.getData().getUserId());
        assertEquals(dto.getOrderId(), result.getData().getOrderId());
    }

    @Test
    void testGetReturnOrderWhenNoRecords() {
        // 确保没有退货记录
        returnOrderMapper.deleteByQuery(
            QueryWrapper.create()
                .where(RETURN_ORDER.USER_ID.eq(testUserId))
        );
        
        // 测试查询退货信息
        var result = returnOrderController.getReturnOrder(validToken);

        // 验证结果
        assertEquals(404, result.getCode());
        assertTrue(result.getMessage().contains("未找到退货记录"));
    }

    @Test
    void testGetReturnOrderUnauthorized() {
        // 先创建一个退货记录
        CreateReturnOrderDTO dto = new CreateReturnOrderDTO();
        dto.setOrderId(1001L);
        dto.setReturnReason("测试查询详情");
        dto.setReturnAmount(new BigDecimal("100"));
        var createResult = returnOrderController.createReturnOrder(dto, validToken);
        
        // 修改mock的用户ID，模拟其他用户访问
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(999L);
        
        // 测试查询退货信息
        var result = returnOrderController.getReturnOrder(validToken);

        // 验证结果
        assertEquals(404, result.getCode());  // 应该返回404，因为对于用户999来说没有退货记录
        assertTrue(result.getMessage().contains("未找到退货记录"));
    }

    @Test
    void testUpdateReturnStatus() {
        // 先创建一个退货申请，使用唯一的orderId
        CreateReturnOrderDTO dto = new CreateReturnOrderDTO();
        dto.setOrderId(1001L);  // 使用一个唯一的orderId
        dto.setReturnReason("测试更新状态");
        dto.setReturnAmount(new BigDecimal("100"));
        dto.setImages("http://example.com/image1.jpg");

        // 创建退货申请并验证
        var createResult = returnOrderController.createReturnOrder(dto, validToken);
        assertEquals(200, createResult.getCode(), "创建退货申请应该成功");
        assertNotNull(createResult.getData(), "创建退货申请失败");
        assertNotNull(createResult.getData().getId(), "退货记录ID不应为空");
        
        // 获取创建的退货记录ID
        Long returnId = createResult.getData().getId();
        assertNotNull(returnId, "退货记录ID不应为空");
        
        // 测试更新状态
        var result = returnOrderController.updateReturnStatus(
            returnId, 
            1, // 1-已同意
            "同意退货",
            validToken
        );

        // 验证结果
        assertEquals(200, result.getCode(), "更新状态应该成功");
        
        // 验证状态是否更新成功
        var updatedOrder = returnOrderService.getReturnOrderByOrderId(dto.getOrderId());
        assertNotNull(updatedOrder, "更新后的退货记录不应为空");
        assertEquals(1, updatedOrder.getStatus());
        assertEquals("同意退货", updatedOrder.getHandleNote());
    }

    @Test
    void testGetUserReturns() {
        // 创建一些测试数据
        CreateReturnOrderDTO dto = new CreateReturnOrderDTO();
        dto.setOrderId(1001L);
        dto.setReturnReason("测试查询列表");
        dto.setReturnAmount(new BigDecimal("100"));
        returnOrderController.createReturnOrder(dto, validToken);

        // 测试查询用户的退货列表
        var result = returnOrderController.getUserReturns(validToken);

        // 验证结果
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertFalse(result.getData().isEmpty(), "返回的列表不应为空");
        
        // 验证返回的列表中所有记录都属于测试用户
        for (ReturnOrder returnOrder : result.getData()) {
            assertEquals(testUserId, returnOrder.getUserId());
        }
    }

    @Test
    void testCreateDuplicateReturnOrder() {
        // 准备测试数据
        CreateReturnOrderDTO dto = new CreateReturnOrderDTO();
        dto.setOrderId(1002L);
        dto.setReturnReason("测试重复创建");
        dto.setReturnAmount(new BigDecimal("100"));
        dto.setImages("http://example.com/image1.jpg");

        // 第一次创建
        var result1 = returnOrderController.createReturnOrder(dto, validToken);
        assertEquals(200, result1.getCode(), "第一次创建应该成功");
        assertNotNull(result1.getData(), "第一次创建返回的数据不应为空");
        assertNotNull(result1.getData().getId(), "退货记录ID不应为空");

        // 尝试重复创建 - 预期会抛出异常
        var result2 = returnOrderController.createReturnOrder(dto, validToken);
        assertEquals(400, result2.getCode(), "重复创建应该失败");
        assertTrue(result2.getMessage().contains("退货记录已存在"), "错误信息应该提示记录已存在");
    }

    @Test
    void testCreateReturnOrderWithInvalidParams() {
        // 测试缺少必要参数
        CreateReturnOrderDTO dto = new CreateReturnOrderDTO();
        var result = returnOrderController.createReturnOrder(dto, validToken);
        
        assertEquals(400, result.getCode());
        assertTrue(result.getMessage().contains("请填写完整的退货信息"));
    }
} 