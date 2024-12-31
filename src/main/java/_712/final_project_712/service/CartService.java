package _712.final_project_712.service;

import _712.final_project_712.model.CartItem;
import java.util.List;

public interface CartService {
    /**
     * 获取购物车商品列表
     */
    List<CartItem> getCartItems(Long userId);
}