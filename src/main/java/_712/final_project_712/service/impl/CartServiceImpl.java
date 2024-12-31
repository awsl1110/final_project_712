package _712.final_project_712.service.impl;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.CartItemMapper;
import _712.final_project_712.mapper.ProductMapper;
import _712.final_project_712.model.CartItem;
import _712.final_project_712.model.Product;
import _712.final_project_712.service.CartService;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemMapper cartItemMapper;
    
    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional
    public void addToCart(Long userId, Long productId, Integer quantity) {
        // 参数检查
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("商品数量必须大于0");
        }

        // 检查商品是否存在
        Product product = productMapper.selectOneById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        
        // 检查商品库存
        if (product.getStock() < quantity) {
            throw new BusinessException("商品库存不足");
        }

        // 检查购物车是否已有该商品
        CartItem existingItem = QueryChain.of(CartItem.class)
                .where("user_id = ?", userId)
                .and("product_id = ?", productId)
                .one();
        
        if (existingItem != null) {
            // 检查更新后的数量是否超过库存
            int newQuantity = existingItem.getQuantity() + quantity;
            if (product.getStock() < newQuantity) {
                throw new BusinessException("商品库存不足");
            }
            
            // 更新数量
            existingItem.setQuantity(newQuantity);
            existingItem.setUpdateTime(LocalDateTime.now());
            cartItemMapper.update(existingItem);
        } else {
            // 新增购物车项
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setSelected(1);  // 默认选中
            cartItem.setCreateTime(LocalDateTime.now());
            cartItem.setUpdateTime(LocalDateTime.now());
            cartItemMapper.insert(cartItem);
        }
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        cartItemMapper.deleteByQuery(
            QueryChain.of(CartItem.class)
                .where("user_id = ?", userId)
                .and("product_id = ?", productId)
        );
    }

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