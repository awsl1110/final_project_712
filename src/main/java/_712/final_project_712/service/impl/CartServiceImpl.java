package _712.final_project_712.service.impl;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.CartMapper;
import _712.final_project_712.mapper.ProductMapper;
import _712.final_project_712.model.CartItem;
import _712.final_project_712.model.Product;
import _712.final_project_712.model.dto.CartDTO;
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
    private CartMapper cartMapper;
    
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<CartDTO> getCartList(Long userId) {
        try {
            System.out.println("Getting cart list for user: " + userId); // 添加日志
            List<CartDTO> cartList = cartMapper.getCartList(userId);
            System.out.println("Found " + (cartList != null ? cartList.size() : 0) + " items"); // 添加日志
            return cartList;
        } catch (Exception e) {
            e.printStackTrace(); // 打印错误堆栈
            throw new BusinessException("获取购物车列表失败: " + e.getMessage());
        }
    }
    
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
            cartMapper.update(existingItem);
        } else {
            // 新增购物车项
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setSelected(1);  // 默认选中
            cartItem.setCreateTime(LocalDateTime.now());
            cartItem.setUpdateTime(LocalDateTime.now());
            cartMapper.insert(cartItem);
        }
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        // 从购物车中删除商品
        cartMapper.deleteByQuery(
            QueryChain.of(CartItem.class)
                .where("user_id = ?", userId)
                .and("product_id = ?", productId)
        );
    }

    @Override
    @Transactional
    public void updateCartItemSelected(Long userId, Long cartId, Integer selected) {
        CartItem cartItem = QueryChain.of(CartItem.class)
                .where("id = ?", cartId)
                .and("user_id = ?", userId)
                .one();
                
        if (cartItem == null) {
            throw new BusinessException("购物车商品不存在");
        }
        
        cartItem.setSelected(selected);
        cartItem.setUpdateTime(LocalDateTime.now());
        cartMapper.update(cartItem);
    }
    
    @Override
    public List<CartDTO> getSelectedCartItems(Long userId) {
        try {
            List<CartDTO> cartList = cartMapper.getSelectedCartList(userId);
            if (cartList == null || cartList.isEmpty()) {
                throw new BusinessException("未选择任何商品");
            }
            return cartList;
        } catch (Exception e) {
            throw new BusinessException("获取选中商品失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void clearSettledItems(List<Long> cartIds) {
        if (cartIds == null || cartIds.isEmpty()) {
            return;
        }
        
        cartMapper.deleteByQuery(
            QueryChain.of(CartItem.class)
                .where(CartItem::getId).in(cartIds)
        );
    }
} 