package _712.final_project_712.controller;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.model.CartItem;
import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.CartDTO;
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
    public List<CartDTO> getCartList(@RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtil.getUserIdFromToken(actualToken);
            System.out.println("Token: " + actualToken);
            System.out.println("User ID: " + userId);
            
            if (userId == null) {
                throw new BusinessException("无效的token");
            }

            return cartService.getCartList(userId);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取购物车列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "添加商品到购物车")
    @PostMapping("/add")
    public void addToCart(@RequestParam Long productId, 
                         @RequestParam Integer quantity,
                         @RequestHeader("Authorization") String token) {
        String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Long userId = jwtUtil.getUserIdFromToken(actualToken);
        if (userId == null) {
            throw new BusinessException("无效的token");
        }

        cartService.addToCart(userId, productId, quantity);
    }

    @Operation(summary = "从购物车删除商品")
    @DeleteMapping("/remove")
    public void removeFromCart(@RequestParam Long productId,
                             @RequestHeader("Authorization") String token) {
        String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Long userId = jwtUtil.getUserIdFromToken(actualToken);
        if (userId == null) {
            throw new BusinessException("无效的token");
        }

        cartService.removeFromCart(userId, productId);
    }
} 