package _712.final_project_712.controller;

import _712.final_project_712.model.CartItem;
import _712.final_project_712.model.Result;
import _712.final_project_712.service.CartService;
import _712.final_project_712.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "获取购物车列表")
    @GetMapping("/list")
    public Result<?> getCartList(@RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtil.getUserIdFromToken(actualToken);
            if (userId == null) {
                return Result.error("无效的token");
            }

            List<CartItem> cartItems = cartService.getCartItems(userId);
            return Result.success(cartItems);
            
        } catch (SignatureException | MalformedJwtException e) {
            return Result.error("无效的token");
        } catch (ExpiredJwtException e) {
            return Result.error("token已过期，请重新登录");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统繁忙，请稍后重试");
        }
    }
} 