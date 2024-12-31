package _712.final_project_712.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class KaptchaControllerTest {

    @Mock
    private DefaultKaptcha defaultKaptcha;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private KaptchaController kaptchaController;

    private MockHttpServletResponse response;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        response = new MockHttpServletResponse();
        session = new MockHttpSession();

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        
        // 模拟验证码生成
        when(defaultKaptcha.createText()).thenReturn("1234");
        when(defaultKaptcha.createImage(anyString())).thenReturn(new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB));
    }

    @Test
    void getKaptcha_ShouldGenerateValidCaptchaImageAndStoreInRedis() throws Exception {
        // 执行
        kaptchaController.getKaptcha(response, session);

        // 验证
        // 1. 验证响应头
        assertNotNull(response.getHeader("Kaptcha-Key"));
        assertEquals("image/jpeg", response.getContentType());
        assertEquals("no-cache", response.getHeader("Pragma"));
        
        // 2. 验证图片生成
        byte[] imageContent = response.getContentAsByteArray();
        assertNotNull(imageContent);
        assertTrue(imageContent.length > 0);
        
        // 验证图片是否为有效的JPEG
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageContent));
        assertNotNull(image);
        
        // 3. 验证Redis存储
        verify(valueOperations).set(
            argThat(key -> key.startsWith("kaptcha:")),
            eq("1234"),
            eq(5L),
            eq(TimeUnit.MINUTES)
        );
        
        // 4. 验证Session存储
        assertEquals("1234", session.getAttribute("captcha"));
    }
} 