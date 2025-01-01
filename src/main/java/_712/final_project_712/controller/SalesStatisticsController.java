package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.SalesStatistics;
import _712.final_project_712.service.SalesStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "销售统计", description = "销售统计相关接口")
@RestController
@RequestMapping("/sales")
public class SalesStatisticsController {

    @Autowired
    private SalesStatisticsService salesStatisticsService;

    @Operation(summary = "获取每日销售统计")
    @GetMapping("/daily")
    public Result<List<SalesStatistics>> getDailySalesStatistics() {
        try {
            List<SalesStatistics> statistics = salesStatisticsService.getDailySalesStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取每日销售统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取每月销售统计")
    @GetMapping("/monthly")
    public Result<List<SalesStatistics>> getMonthlySalesStatistics() {
        try {
            List<SalesStatistics> statistics = salesStatisticsService.getMonthlySalesStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取每月销售统计失败：" + e.getMessage());
        }
    }
} 