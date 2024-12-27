package _712.final_project_712.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha kaptcha() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 设置边框
        properties.setProperty("kaptcha.border", "no");
        // 设置边距
        properties.setProperty("kaptcha.border.thickness", "1");
        // 设置背景颜色
        properties.setProperty("kaptcha.background.clear.from", "white");
        properties.setProperty("kaptcha.background.clear.to", "white");
        // 设置文本颜色
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        // 设置字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "40");
        // 设置字体
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");
        // 设置验证码的长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 设置验证码的Noise，即噪点
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        // 设置验证码的样式
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}