package _712.final_project_712.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.mybatis.FlexConfiguration;
import com.mybatisflex.spring.FlexSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class MyBatisFlexConfig {
    
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        FlexSqlSessionFactoryBean factoryBean = new FlexSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        
        // 设置MyBatis-Flex的配置
        FlexConfiguration configuration = new FlexConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);
        
        // 设置实体类的包路径
        factoryBean.setTypeAliasesPackage("_712.final_project_712.model");
        
        // 关闭banner打印
        FlexGlobalConfig.getDefaultConfig().setPrintBanner(false);
        
        return factoryBean.getObject();
    }
} 