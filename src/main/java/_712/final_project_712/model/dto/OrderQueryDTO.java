package _712.final_project_712.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
public class OrderQueryDTO {
    private String orderNo;
    private Long userId;
    private Integer status;
    private String receiverPhone;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String startTime;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String endTime;
    
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    // 添加日期格式验证方法
    public boolean isValidDateFormat() {
        try {
            if (startTime != null && !startTime.trim().isEmpty()) {
                LocalDateTime.parse(startTime.trim());
            }
            if (endTime != null && !endTime.trim().isEmpty()) {
                LocalDateTime.parse(endTime.trim());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 