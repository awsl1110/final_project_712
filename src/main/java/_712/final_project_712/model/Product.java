package _712.final_project_712.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table("product")
public class Product {
    
    @Id
    private Long id;
    
    private String name;
    
    private String description;
    
    private Double price;
} 