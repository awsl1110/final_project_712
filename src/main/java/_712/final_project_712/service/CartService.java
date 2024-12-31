package _712.final_project_712.service;

import _712.final_project_712.model.CartItem;
import java.util.List;

public interface CartService {
    /**
     * 获取购物车商品列表
     */
    List<CartItem> getCartItems(Long userId);
    
    /**
     * 添加商品到购物车
     */
    void addToCart(Long userId, Long productId, Integer quantity);
    
    /**
     * 从购物车删除商品
     */
    void removeFromCart(Long userId, Long productId);
}