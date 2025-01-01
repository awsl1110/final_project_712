package _712.final_project_712.service;

import _712.final_project_712.mapper.ActivityMapper;
import _712.final_project_712.model.dto.ActivityDTO;
import _712.final_project_712.service.impl.ActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ActivityServiceTest {

    @Mock
    private ActivityMapper activityMapper;

    @InjectMocks
    private ActivityServiceImpl activityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getActivityDetail_ShouldReturnActivityDTO() {
        // Arrange
        ActivityDTO mockActivity = new ActivityDTO();
        mockActivity.setId(1L);
        mockActivity.setName("测试活动");
        mockActivity.setStatus(1);
        
        ActivityDTO.ActivityProductDTO product = new ActivityDTO.ActivityProductDTO();
        product.setProductId(1L);
        product.setProductName("测试商品");
        product.setActivityPrice(new BigDecimal("99.99"));
        mockActivity.setProducts(Arrays.asList(product));

        when(activityMapper.getActivityWithProducts(anyLong())).thenReturn(mockActivity);

        // Act
        ActivityDTO result = activityService.getActivityDetail(1L);

        // Assert
        assertNotNull(result);
        assertEquals("测试活动", result.getName());
        assertFalse(result.getProducts().isEmpty());
        assertEquals("测试商品", result.getProducts().get(0).getProductName());
    }

    @Test
    void getOngoingActivities_ShouldReturnActivityList() {
        // Arrange
        List<ActivityDTO> mockActivities = Arrays.asList(
            createMockActivity(1L, "活动1", 1),
            createMockActivity(2L, "活动2", 1)
        );

        when(activityMapper.getActivitiesByStatus(1)).thenReturn(mockActivities);

        // Act
        List<ActivityDTO> result = activityService.getOngoingActivities();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("活动1", result.get(0).getName());
    }

    @Test
    void getUpcomingActivities_ShouldReturnActivityList() {
        // Arrange
        List<ActivityDTO> mockActivities = Arrays.asList(
            createMockActivity(1L, "即将开始1", 0),
            createMockActivity(2L, "即将开始2", 0)
        );

        when(activityMapper.getActivitiesByStatus(0)).thenReturn(mockActivities);

        // Act
        List<ActivityDTO> result = activityService.getUpcomingActivities();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("即将开始1", result.get(0).getName());
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