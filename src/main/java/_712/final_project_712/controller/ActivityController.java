package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.ActivityDTO;
import _712.final_project_712.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "活动管理", description = "活动相关接口")
@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Operation(summary = "获取活动详情")
    @GetMapping("/{activityId}")
    public Result<ActivityDTO> getActivityDetail(
            @Parameter(description = "活动ID", required = true)
            @PathVariable Long activityId) {
        try {
            ActivityDTO activity = activityService.getActivityDetail(activityId);
            return Result.success(activity);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取进行中的活动")
    @GetMapping("/ongoing")
    public Result<List<ActivityDTO>> getOngoingActivities() {
        try {
            List<ActivityDTO> activities = activityService.getOngoingActivities();
            return Result.success(activities);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取即将开始的活动")
    @GetMapping("/upcoming")
    public Result<List<ActivityDTO>> getUpcomingActivities() {
        try {
            List<ActivityDTO> activities = activityService.getUpcomingActivities();
            return Result.success(activities);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 