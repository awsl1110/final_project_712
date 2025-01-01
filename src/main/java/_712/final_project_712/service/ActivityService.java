package _712.final_project_712.service;

import _712.final_project_712.model.dto.ActivityDTO;
import java.util.List;

public interface ActivityService {
    ActivityDTO getActivityDetail(Long activityId);
    List<ActivityDTO> getOngoingActivities();
    List<ActivityDTO> getUpcomingActivities();
} 