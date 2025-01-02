package _712.final_project_712.controller;

import _712.final_project_712.model.ReturnOrder;
import _712.final_project_712.service.ReturnOrderService;
import _712.final_project_712.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReturnOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReturnOrderService returnOrderService;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setId(1L);
        returnOrder.setOrderId(100L);
        returnOrder.setUserId(1L);
        returnOrder.setReturnReason("商品质量问题");
        returnOrder.setReturnAmount(new BigDecimal("100.00"));
        returnOrder.setStatus(0);
        returnOrder.setApplyTime(LocalDateTime.now());
        returnOrder.setCreateTime(LocalDateTime.now());
        returnOrder.setUpdateTime(LocalDateTime.now());

        // 配置JWT验证通过
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(1L);
    }

    @Test
    void testGetReturnOrderByOrderId() throws Exception {
        // 准备测试数据
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setId(1L);
        returnOrder.setOrderId(100L);
        returnOrder.setUserId(1L);
        returnOrder.setReturnReason("商品质量问题");
        returnOrder.setReturnAmount(new BigDecimal("100.00"));
        returnOrder.setStatus(0);
        returnOrder.setApplyTime(LocalDateTime.now());
        returnOrder.setCreateTime(LocalDateTime.now());
        returnOrder.setUpdateTime(LocalDateTime.now());

        // 配置mock行为
        when(returnOrderService.getReturnOrderByOrderId(anyLong())).thenReturn(returnOrder);

        // 执行测试并验证结果
        mockMvc.perform(get("/api/return-orders/order/100")
                .header("Authorization", "Bearer test-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.orderId").value(100))
                .andExpect(jsonPath("$.data.returnReason").value("商品质量问题"))
                .andExpect(jsonPath("$.data.returnAmount").value(100.00))
                .andExpect(jsonPath("$.data.status").value(0));
    }

    @Test
    void testGetReturnOrderByOrderIdWhenNotFound() throws Exception {
        // 配置mock行为 - 返回null表示未找到退货记录
        when(returnOrderService.getReturnOrderByOrderId(anyLong())).thenReturn(null);

        // 执行测试并验证结果
        mockMvc.perform(get("/api/return-orders/order/999")
                .header("Authorization", "Bearer test-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("未找到退货记录"));
    }

    @Test
    void testGetReturnOrderByOrderIdWhenError() throws Exception {
        // 配置mock行为 - 抛出异常
        when(returnOrderService.getReturnOrderByOrderId(anyLong()))
                .thenThrow(new RuntimeException("数据库错误"));

        // 执行测试并验证结果
        mockMvc.perform(get("/api/return-orders/order/100")
                .header("Authorization", "Bearer test-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("系统繁忙，请稍后重试"));
    }

    @Test
    void testUpdateReturnStatus_Success() throws Exception {
        doNothing().when(returnOrderService).updateReturnStatus(anyLong(), anyInt(), anyString());

        mockMvc.perform(put("/api/return-orders/1/status")
                .header("Authorization", "Bearer test-token")
                .param("status", "1")
                .param("handleNote", "同意退货"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testUpdateReturnStatus_InvalidStatus() throws Exception {
        doThrow(new IllegalArgumentException("无效的状态值"))
                .when(returnOrderService).updateReturnStatus(anyLong(), anyInt(), anyString());

        mockMvc.perform(put("/api/return-orders/1/status")
                .header("Authorization", "Bearer test-token")
                .param("status", "5")
                .param("handleNote", "同意退货"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("更新状态失败：无效的状态值"));
    }

    @Test
    void testGetUserReturns_Success() throws Exception {
        ReturnOrder returnOrder1 = new ReturnOrder();
        returnOrder1.setId(1L);
        returnOrder1.setOrderId(100L);
        returnOrder1.setUserId(1L);
        returnOrder1.setReturnReason("商品质量问题");
        returnOrder1.setReturnAmount(new BigDecimal("100.00"));
        returnOrder1.setStatus(0);

        ReturnOrder returnOrder2 = new ReturnOrder();
        returnOrder2.setId(2L);
        returnOrder2.setOrderId(101L);
        returnOrder2.setUserId(1L);
        returnOrder2.setReturnReason("尺寸不合适");
        returnOrder2.setReturnAmount(new BigDecimal("200.00"));
        returnOrder2.setStatus(1);

        List<ReturnOrder> returnOrders = Arrays.asList(returnOrder1, returnOrder2);
        when(returnOrderService.getUserReturns(anyLong())).thenReturn(returnOrders);

        mockMvc.perform(get("/api/return-orders/user/1")
                .header("Authorization", "Bearer test-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].returnReason").value("商品质量问题"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].returnReason").value("尺寸不合适"));
    }

    @Test
    void testGetUserReturns_Error() throws Exception {
        when(returnOrderService.getUserReturns(anyLong()))
                .thenThrow(new RuntimeException("数据库错误"));

        mockMvc.perform(get("/api/return-orders/user/1")
                .header("Authorization", "Bearer test-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("查询失败：数据库错误"));
    }
} 