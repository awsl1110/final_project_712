package _712.final_project_712.service;

import _712.final_project_712.model.dto.CartDTO;
import java.util.List;

public interface CartService {
    /**
     * 获取购物车商品列表
     */
    List<CartDTO> getCartList(Long userId);
    
    /**
     * 添加商品到购物车
     */
    void addToCart(Long userId, Long productId, Integer quantity);
    
    /**
     * 从购物车删除商品
     */
    void removeFromCart(Long userId, Long productId);
    
    /**
     * 更新购物车商品选中状态
     */
    void updateCartItemSelected(Long userId, Long cartId, Integer selected);
    
    /**
     * 获取用户选中的购物车商品
     */
    List<CartDTO> getSelectedCartItems(Long userId);
    
    /**
     * 清空已结算的购物车商品
     */
    void clearSettledItems(List<Long> cartIds);
}