package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table("user")
public class User {
    @Id
    private Long id;
    
    private String name;
    
    private String password;
    
    private String email;
}
