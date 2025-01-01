package _712.final_project_712.controller;

import _712.final_project_712.model.ReturnOrder;
import _712.final_project_712.service.ReturnOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        mockMvc.perform(get("/return-orders/order/100"))
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
        mockMvc.perform(get("/return-orders/order/999"))
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
        mockMvc.perform(get("/return-orders/order/100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("系统繁忙，请稍后重试"));
    }
} 