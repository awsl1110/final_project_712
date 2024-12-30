package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Table("user")
public class User {
    
    @Id
    private Long id;
    private String name;
    private String password;
    private String email;

    public User() {
    }

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
