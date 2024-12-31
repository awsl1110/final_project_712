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
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtil.getUserIdFromToken(actualToken);
            if (userId == null) {
                return Result.error("无效的token");
            }

            cartService.addToCart(userId, productId, quantity);
            return Result.success();
            
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        } catch (SignatureException | MalformedJwtException e) {
            return Result.error("无效的token");
        } catch (ExpiredJwtException e) {
            return Result.error("token已过期，请重新登录");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统繁忙，请稍后重试");
        }
    }

    @Operation(summary = "从购物车删除商品")
    @DeleteMapping("/remove")
    public Result<?> removeFromCart(@RequestParam Long productId,
                                  @RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtil.getUserIdFromToken(actualToken);
            if (userId == null) {
                return Result.error("无效的token");
            }

            cartService.removeFromCart(userId, productId);
            return Result.success();
            
        } catch (SignatureException | MalformedJwtException e) {
            return Result.error("无效的token");
        } catch (ExpiredJwtException e) {
            return Result.error("token已过期，请重新登录");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统繁忙，请稍后重试");
        }
    }

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