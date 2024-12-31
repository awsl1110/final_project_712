package _712.final_project_712.controller;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.model.CartItem;
import _712.final_project_712.model.Result;
import _712.final_project_712.service.CartService;
import _712.final_project_712.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "购物车管理")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "添加商品到购物车")
    @PostMapping("/add")
    public Result<?> addToCart(@RequestParam Long productId, 
                              @RequestParam Integer quantity,
                              @RequestHeader("Authorization") String token) {
        try {
            // 检查参数
            if (quantity <= 0) {
                return Result.error("商品数量必须大于0");
            }
            
            // 验证并获取用户ID
            if (token == null) {
                return Result.error("未登录");
            }
            
            try {
                // 如果 token 带有 Bearer 前缀就去掉，否则直接使用
                String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
                
                Long userId = jwtUtil.getUserIdFromToken(actualToken);
                if (userId == null) {
                    return Result.error("无效的token");
                }
                
                cartService.addToCart(userId, productId, quantity);
                return Result.success();
            } catch (SignatureException | MalformedJwtException e) {
                return Result.error("无效的token");
            } catch (ExpiredJwtException e) {
                return Result.error("token已过期，请重新登录");
            }
            
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统繁忙，请稍后重试");
        }
    }

    // ... 其他方法保持不变
} 