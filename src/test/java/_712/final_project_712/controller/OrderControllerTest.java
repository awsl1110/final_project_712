package _712.final_project_712.controller;

import _712.final_project_712.mapper.OrderMapper;
import _712.final_project_712.model.Orders;
import _712.final_project_712.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Orders testOrder;

    @BeforeEach
    void setUp() {
        // 创建测试订单数据
        testOrder = new Orders();
        testOrder.setId(1L);
        testOrder.setUserId(1L);
        testOrder.setTotalAmount(new BigDecimal("999.99"));
        testOrder.setStatus(0);
        testOrder.setAddress("测试地址");
        testOrder.setReceiverName("测试用户");
        testOrder.setReceiverPhone("13800138000");
        testOrder.setCreateTime(LocalDateTime.now());
        testOrder.setUpdateTime(LocalDateTime.now());

        // 插入测试数据到数据库
        orderMapper.insert(testOrder);
    }


    @Test
    void testGetOrderDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order/{orderId}", testOrder.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/order/{orderId}/status/{status}", 
                testOrder.getId(), 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }


    @Test
    void testUpdateOrderStatusWithInvalidStatus() throws Exception {
        // 测试无效的订单状态
        mockMvc.perform(MockMvcRequestBuilders.put("/order/{orderId}/status/{status}", 
                testOrder.getId(), 999))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testDeleteNonExistentOrder() throws Exception {
        // 测试删除不存在的订单
        mockMvc.perform(MockMvcRequestBuilders.delete("/order/{orderId}", 999999))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }
} 