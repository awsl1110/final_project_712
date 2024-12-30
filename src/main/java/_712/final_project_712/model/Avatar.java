package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Table("avatar")
public class Avatar {
    
    @Id
    private Long id;
    
    private Long userId;
    
    private String fileName;
    
    private LocalDateTime createTime;
    
    @RelationOneToOne(selfField = "userId", targetField = "id")
    private User user;
} 