package _712.final_project_712.mapper;

import _712.final_project_712.model.SalesStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface SalesStatisticsMapper {
    
    @Select("SELECT p.id as productId, p.name as productName, " +
            "COUNT(DISTINCT o.id) as salesCount, " +
            "SUM(oi.subtotal) as salesAmount, " +
            "MIN(DATE(o.create_time)) as statisticsTime, " +
            "'DAY' as timeRange " +
            "FROM product p " +
            "LEFT JOIN order_items oi ON p.id = oi.product_id " +
            "LEFT JOIN orders o ON oi.order_id = o.id " +
            "WHERE o.status = 3 " +
            "GROUP BY p.id, p.name, DATE(o.create_time)")
    List<SalesStatistics> getDailySalesStatistics();

    @Select("SELECT p.id as productId, p.name as productName, " +
            "COUNT(DISTINCT o.id) as salesCount, " +
            "SUM(oi.subtotal) as salesAmount, " +
            "DATE(MIN(o.create_time)) as statisticsTime, " +
            "'MONTH' as timeRange " +
            "FROM product p " +
            "LEFT JOIN order_items oi ON p.id = oi.product_id " +
            "LEFT JOIN orders o ON oi.order_id = o.id " +
            "WHERE o.status = 3 " +
            "GROUP BY p.id, p.name, YEAR(o.create_time), MONTH(o.create_time)")
    List<SalesStatistics> getMonthlySalesStatistics();
} 