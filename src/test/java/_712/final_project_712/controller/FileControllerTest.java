package _712.final_project_712.controller;

import _712.final_project_712.service.FileService;
import _712.final_project_712.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @MockBean
    private JwtUtil jwtUtil;

    private static final String TEST_TOKEN = "Bearer test-token";

    @Test
    public void testUploadAvatar() throws Exception {
        // 创建测试目录
        String testOutputDir = "test-output/test-images";
        Files.createDirectories(Paths.get(testOutputDir));

        // 创建测试图片文件
        Path testImagePath = Paths.get(testOutputDir, "test-image.jpg");
        if (!Files.exists(testImagePath)) {
            // 创建一个简单的测试图片（1x1像素的黑色图片）
            byte[] imageData = new byte[]{
                (byte) 0xFF, (byte) 0xD8, // JPEG文件头
                (byte) 0xFF, (byte) 0xE0,
                0x00, 0x10, 'J', 'F', 'I', 'F', 0x00,
                0x01, 0x01, 0x00, 0x00, 0x01,
                0x00, 0x01, 0x00, 0x00,
                (byte) 0xFF, (byte) 0xDB,
                0x00, 0x43, 0x00,
                (byte) 0xFF, (byte) 0xC0,
                0x00, 0x0B, 0x08, 0x00, 0x01, 0x00, 0x01, 0x01, 0x01, 0x11, 0x00,
                (byte) 0xFF, (byte) 0xC4,
                0x00, 0x14, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03,
                (byte) 0xFF, (byte) 0xDA,
                0x00, 0x08, 0x01, 0x01, 0x00, 0x00, 0x3F, 0x00, 0x37, (byte) 0xFF, (byte) 0xD9
            };
            Files.write(testImagePath, imageData);
        }

        // Mock JWT验证
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(1L);

        // Mock FileService
        when(fileService.saveAvatar(any(), anyLong())).thenReturn("/file/avatar/1/test-image.jpg");

        // 创建MockMultipartFile
        MockMultipartFile file = new MockMultipartFile(
            "file",                       // 参数名
            "test-image.jpg",            // 原始文件名
            "image/jpeg",                // 内容类型
            Files.readAllBytes(testImagePath) // 文件内容
        );

        // 执行测试请求
        mockMvc.perform(multipart("/api/file/avatar")
                .file(file)
                .param("userId", "1")
                .header("Authorization", TEST_TOKEN))
            .andDo(print())  // 打印请求和响应详情
            .andExpect(status().isOk())  // 验证响应状态是否为200
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("/file/avatar/1/test-image.jpg"));
    }

    @Test
    public void testUploadInvalidFile() throws Exception {
        // Mock JWT验证
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(1L);

        // 测试上传空文件
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file",
            "empty.jpg",
            "image/jpeg",
            new byte[0]
        );

        mockMvc.perform(multipart("/api/file/avatar")
                .file(emptyFile)
                .param("userId", "1")
                .header("Authorization", TEST_TOKEN))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("请选择要上传的文件"));

        // 测试上传非图片文件
        MockMultipartFile textFile = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "Hello World".getBytes()
        );

        mockMvc.perform(multipart("/api/file/avatar")
                .file(textFile)
                .param("userId", "1")
                .header("Authorization", TEST_TOKEN))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("只支持图片文件上传"));

        // 测试上传不支持的图片格式
        MockMultipartFile webpFile = new MockMultipartFile(
            "file",
            "test.webp",
            "image/webp",
            "fake webp content".getBytes()
        );

        // Mock FileService 抛出异常
        when(fileService.saveAvatar(any(), anyLong()))
            .thenThrow(new IllegalArgumentException("只支持 JPG、PNG、GIF 格式的图片文件"));

        mockMvc.perform(multipart("/api/file/avatar")
                .file(webpFile)
                .param("userId", "1")
                .header("Authorization", TEST_TOKEN))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("只支持 JPG、PNG、GIF 格式的图片文件"));
    }
} 