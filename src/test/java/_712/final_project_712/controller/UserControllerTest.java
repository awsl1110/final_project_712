package _712.final_project_712.controller;

import _712.final_project_712.mapper.*;
import _712.final_project_712.model.Result;
import _712.final_project_712.model.User;
import _712.final_project_712.service.UserService;
import _712.final_project_712.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private StringRedisTemplate redisTemplate;

    @MockBean
    private ValueOperations<String, String> valueOperations;

    @MockBean
    private ActivityMapper activityMapper;

    @MockBean
    private AvatarMapper avatarMapper;

    @MockBean
    private CartMapper cartMapper;

    @MockBean
    private CartItemMapper cartItemMapper;

    @MockBean
    private CouponMapper couponMapper;

    @MockBean
    private FavoriteMapper favoriteMapper;

    @MockBean
    private OrderMapper orderMapper;

    @MockBean
    private OrderItemMapper orderItemMapper;

    @MockBean
    private PickupCodeMapper pickupCodeMapper;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private ProductCategoryMapper productCategoryMapper;

    @MockBean
    private ProductReviewMapper productReviewMapper;

    @MockBean
    private ReturnOrderMapper returnOrderMapper;

    @MockBean
    private SalesStatisticsMapper salesStatisticsMapper;

    @MockBean
    private SupplierMapper supplierMapper;

    @MockBean
    private SupplierProductMapper supplierProductMapper;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserAddressMapper userAddressMapper;

    @MockBean
    private UserCouponMapper userCouponMapper;

    private ObjectMapper objectMapper;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        session = new MockHttpSession();
        session.setAttribute("captcha", "1234"); // 设置默认验证码
        
        // 配置 Redis 模拟
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void loginSuccess() throws Exception {
        // 模拟 UserService 的 login 方法返回 token
        when(userService.login(anyString(), anyString())).thenReturn("test-token");

        // 执行登录请求
        mockMvc.perform(post("/api/user/login")
                .param("username", "testuser")
                .param("password", "password123")
                .param("captcha", "1234")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").value("test-token"));
    }

    @Test
    void loginFailWithInvalidCaptcha() throws Exception {
        mockMvc.perform(post("/api/user/login")
                .param("username", "testuser")
                .param("password", "password123")
                .param("captcha", "wrong")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("验证码错误"));
    }

    @Test
    void loginFailWithExpiredCaptcha() throws Exception {
        MockHttpSession sessionWithoutCaptcha = new MockHttpSession();
        
        mockMvc.perform(post("/api/user/login")
                .param("username", "testuser")
                .param("password", "password123")
                .param("captcha", "1234")
                .session(sessionWithoutCaptcha)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("验证码已过期，请重新获取"));
    }

    @Test
    void registerSuccess() throws Exception {
        // 模拟 UserService 的 register 方法返回成功
        when(userService.register(any(User.class))).thenReturn(true);

        mockMvc.perform(post("/api/user/register")
                .param("username", "newuser")
                .param("password", "password123")
                .param("email", "test@example.com")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("注册成功"));
    }

    @Test
    void registerFailure() throws Exception {
        // 模拟 UserService 的 register 方法返回失败
        when(userService.register(any(User.class))).thenReturn(false);

        mockMvc.perform(post("/api/user/register")
                .param("username", "newuser")
                .param("password", "password123")
                .param("email", "test@example.com")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("注册失败"));
    }

    @Test
    void updatePasswordSuccess() throws Exception {
        String token = "Bearer test-token";
        String email = "test@example.com";
        String emailCode = "123456";
        
        // 模拟 token 验证
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(1L);
        
        // 模拟获取用户邮箱
        when(userService.getUserEmail(1L)).thenReturn(email);
        
        // 模拟 Redis 中的验证码
        when(valueOperations.get("email:captcha:" + email)).thenReturn(emailCode);
        
        // 模拟密码更新成功
        when(userService.updatePassword(1L, "oldPassword", "newPassword")).thenReturn(true);

        mockMvc.perform(post("/api/user/update_password")
                .header("Authorization", token)
                .param("oldPassword", "oldPassword")
                .param("newPassword", "newPassword")
                .param("emailCode", emailCode)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("密码修改成功"));
    }

    @Test
    void updatePasswordWithInvalidToken() throws Exception {
        String token = "Bearer invalid-token";
        
        // 模拟 token 验证失败
        when(jwtUtil.validateToken(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/user/update_password")
                .header("Authorization", token)
                .param("oldPassword", "oldPassword")
                .param("newPassword", "newPassword")
                .param("emailCode", "123456")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("token已过期或无效"));
    }

    @Test
    void updatePasswordWithInvalidEmailCode() throws Exception {
        String token = "Bearer test-token";
        String email = "test@example.com";
        
        // 模拟 token 验证
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(1L);
        
        // 模拟获取用户邮箱
        when(userService.getUserEmail(1L)).thenReturn(email);
        
        // 模拟 Redis 中的验证码已过期（返回 null）
        when(valueOperations.get("email:captcha:" + email)).thenReturn(null);

        mockMvc.perform(post("/api/user/update_password")
                .header("Authorization", token)
                .param("oldPassword", "oldPassword")
                .param("newPassword", "newPassword")
                .param("emailCode", "123456")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("验证码已过期"));
    }

    @Test
    void updatePasswordWithWrongEmailCode() throws Exception {
        String token = "Bearer test-token";
        String email = "test@example.com";
        String correctEmailCode = "123456";
        String wrongEmailCode = "654321";
        
        // 模拟 token 验证
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(1L);
        
        // 模拟获取用户邮箱
        when(userService.getUserEmail(1L)).thenReturn(email);
        
        // 模拟 Redis 中的验证码
        when(valueOperations.get("email:captcha:" + email)).thenReturn(correctEmailCode);

        mockMvc.perform(post("/api/user/update_password")
                .header("Authorization", token)
                .param("oldPassword", "oldPassword")
                .param("newPassword", "newPassword")
                .param("emailCode", wrongEmailCode)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("验证码错误"));
    }
}