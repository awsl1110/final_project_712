package _712.final_project_712.model;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("product")
public class Product {
    @Id
    private Long id;
    
    private String name;
    
    private Double price;
    
    private String description;
    
    private Integer stock;
    
    @Column("create_time")
    private LocalDateTime createTime;
    
    @Column("update_time")
    private LocalDateTime updateTime;
} 