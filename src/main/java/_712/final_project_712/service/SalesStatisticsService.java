package _712.final_project_712.service;

import _712.final_project_712.model.SalesStatistics;
import _712.final_project_712.mapper.SalesStatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SalesStatisticsService {

    @Autowired
    private SalesStatisticsMapper salesStatisticsMapper;

    public List<SalesStatistics> getDailySalesStatistics() {
        return salesStatisticsMapper.getDailySalesStatistics();
    }

    public List<SalesStatistics> getMonthlySalesStatistics() {
        return salesStatisticsMapper.getMonthlySalesStatistics();
    }
} 