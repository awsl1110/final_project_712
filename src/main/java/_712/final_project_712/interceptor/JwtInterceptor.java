package _712.final_project_712.interceptor;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if (!StringUtils.hasText(token)) {
            throw new BusinessException("未登录，请先登录");
        }
        
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException("token无效或已过期");
        }
        
        // 将用户ID存入request属性中
        Long userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);
        
        return true;
    }
} 