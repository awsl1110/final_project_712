package _712.final_project_712.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class KaptchaConfigTest {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Test
    public void testKaptchaGeneration() throws IOException {
        // 创建测试输出目录
        String outputDir = "test-output/captcha";
        File directory = new File(outputDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // 生成验证码文本
        String text = defaultKaptcha.createText();
        System.out.println("生成的验证码文本: " + text);
        
        // 验证生成的文本长度是否符合预期（默认配置为4个字符）
        assertEquals(4, text.length());
        
        // 根据文本生成验证码图片
        BufferedImage image = defaultKaptcha.createImage(text);
        
        // 验证图片是否成功生成
        assertNotNull(image);
        
        // 验证图片尺寸是否符合配置
        assertEquals(200, image.getWidth());
        assertEquals(50, image.getHeight());
        
        // 将验证码图片保存到专门的测试输出目录
        File output = new File(directory, "captcha-" + text + ".jpg");
        ImageIO.write(image, "jpg", output);
        assertTrue(output.exists());
        
        System.out.println("验证码图片已保存到: " + output.getAbsolutePath());
    }
} 