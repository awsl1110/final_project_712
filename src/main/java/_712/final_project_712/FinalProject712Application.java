package _712.final_project_712;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("_712.final_project_712.mapper")
public class FinalProject712Application {

	public static void main(String[] args) {
		SpringApplication.run(FinalProject712Application.class, args);
	}

}
