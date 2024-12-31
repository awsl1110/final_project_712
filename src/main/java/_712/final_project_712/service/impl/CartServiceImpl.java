package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.CartItemMapper;
import _712.final_project_712.mapper.ProductMapper;
import _712.final_project_712.model.CartItem;
import _712.final_project_712.model.Product;
import _712.final_project_712.service.CartService;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemMapper cartItemMapper;
    
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<CartItem> getCartItems(Long userId) {
        // 获取购物车列表
        List<CartItem> cartItems = QueryChain.of(CartItem.class)
                .where("user_id = ?", userId)
                .orderBy("create_time desc")
                .list();
        
        // 填充商品名称
        for (CartItem cartItem : cartItems) {
            Product product = productMapper.selectOneById(cartItem.getProductId());
            if (product != null) {
                cartItem.setProductName(product.getName());
            }
        }
        
        return cartItems;
    }
} 