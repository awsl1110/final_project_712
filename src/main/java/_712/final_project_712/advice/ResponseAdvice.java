package _712.final_project_712.advice;

import _712.final_project_712.model.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "_712.final_project_712")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper;

    public ResponseAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果接口返回的类型本身就是Result或ResponseEntity，则不需要封装
        return !returnType.getParameterType().equals(Result.class) 
               && !returnType.getParameterType().equals(ResponseEntity.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {
        // 如果返回值是ResponseEntity类型，直接返回其body部分
        if (body instanceof ResponseEntity) {
            return ((ResponseEntity<?>) body).getBody();
        }
        
        // 如果是Result类型，直接返回
        if (body instanceof Result) {
            return body;
        }
        
        // 如果返回值是String类型，需要特殊处理
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                return Result.error("响应结果序列化失败");
            }
        }
        
        // 其他类型直接返回
        return body;
    }
} 