package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.ActivityMapper;
import _712.final_project_712.model.dto.ActivityDTO;
import _712.final_project_712.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    
    @Autowired
    private ActivityMapper activityMapper;
    
    @Override
    public ActivityDTO getActivityDetail(Long activityId) {
        return activityMapper.getActivityWithProducts(activityId);
    }
    
    @Override
    public List<ActivityDTO> getOngoingActivities() {
        return activityMapper.getActivitiesByStatus(1); // 1-进行中
    }
    
    @Override
    public List<ActivityDTO> getUpcomingActivities() {
        return activityMapper.getActivitiesByStatus(0); // 0-未开始
    }
} 