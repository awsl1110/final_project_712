package _712.final_project_712.interceptor;

import _712.final_project_712.model.Result;
import _712.final_project_712.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

@Slf4j
@Component
public class SupplierTokenInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public SupplierTokenInterceptor(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            writeErrorResponse(response, "请先登录");
            return false;
        }

        // 验证token是否有效
        if (!jwtUtil.validateToken(token)) {
            writeErrorResponse(response, "登录已过期，请重新登录");
            return false;
        }

        // 获取用户名
        String username = jwtUtil.getUsernameFromToken(token);
        if (!"admin".equals(username)) {
            writeErrorResponse(response, "仅管理员可操作");
            return false;
        }

        return true;
    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        Result<?> result = Result.error(message);
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(result));
        writer.flush();
    }
} 