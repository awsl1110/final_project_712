package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.ActivityDTO;
import _712.final_project_712.service.ActivityService;
import _712.final_project_712.util.JwtUtil;
import _712.final_project_712.mapper.ActivityMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActivityController.class)
@AutoConfigureMybatis
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @MockBean
    private ActivityMapper activityMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private ActivityDTO mockActivity;
    private List<ActivityDTO> mockActivities;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        mockActivity = createMockActivity(1L, "测试活动", 1);
        mockActivities = Arrays.asList(
            createMockActivity(1L, "活动1", 1),
            createMockActivity(2L, "活动2", 1)
        );
    }

    @Test
    void getActivityDetail_ShouldReturnActivity() throws Exception {
        when(activityService.getActivityDetail(anyLong())).thenReturn(mockActivity);

        mockMvc.perform(get("/api/activity/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("测试活动"))
                .andExpect(jsonPath("$.data.products[0].productName").value("商品1"));
    }

    @Test
    void getOngoingActivities_ShouldReturnActivityList() throws Exception {
        when(activityService.getOngoingActivities()).thenReturn(mockActivities);

        mockMvc.perform(get("/api/activity/ongoing"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].name").value("活动1"))
                .andExpect(jsonPath("$.data[1].name").value("活动2"));
    }

    @Test
    void getUpcomingActivities_ShouldReturnActivityList() throws Exception {
        List<ActivityDTO> upcomingActivities = Arrays.asList(
            createMockActivity(1L, "即将开始1", 0),
            createMockActivity(2L, "即将开始2", 0)
        );
        
        when(activityService.getUpcomingActivities()).thenReturn(upcomingActivities);

        mockMvc.perform(get("/api/activity/upcoming"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].name").value("即将开始1"))
                .andExpect(jsonPath("$.data[1].name").value("即将开始2"));
    }

    private ActivityDTO createMockActivity(Long id, String name, Integer status) {
        ActivityDTO activity = new ActivityDTO();
        activity.setId(id);
        activity.setName(name);
        activity.setStatus(status);
        activity.setStartTime(LocalDateTime.now());
        activity.setEndTime(LocalDateTime.now().plusDays(7));
        
        ActivityDTO.ActivityProductDTO product = new ActivityDTO.ActivityProductDTO();
        product.setProductId(id);
        product.setProductName("商品" + id);
        product.setActivityPrice(new BigDecimal("99.99"));
        activity.setProducts(Arrays.asList(product));
        
        return activity;
    }
} 