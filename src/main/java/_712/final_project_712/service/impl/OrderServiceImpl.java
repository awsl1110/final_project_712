package _712.final_project_712.service.impl;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.OrderMapper;
import _712.final_project_712.mapper.OrderItemMapper;
import _712.final_project_712.mapper.ProductMapper;
import _712.final_project_712.mapper.UserAddressMapper;
import _712.final_project_712.model.Orders;
import _712.final_project_712.model.OrderItem;
import _712.final_project_712.model.Product;
import _712.final_project_712.model.UserAddress;
import _712.final_project_712.model.dto.CartDTO;
import _712.final_project_712.model.dto.OrderDTO;
import _712.final_project_712.service.CartService;
import _712.final_project_712.service.OrderService;
import _712.final_project_712.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private UserCouponService userCouponService;

    @Override
    public List<OrderDTO.OrderInfo> getUserOrders(Long userId) {
        try {
            // 获取用户的订单
            List<OrderDTO.OrderInfo> orders = orderMapper.getUserOrders(userId);
            
            // 获取每个订单的商品信息
            for (OrderDTO.OrderInfo order : orders) {
                List<OrderDTO.OrderItemInfo> items = orderMapper.getOrderItems(order.getId());
                order.setItems(items);
            }
            
            return orders;
        } catch (Exception e) {
            throw new BusinessException("获取订单列表失败：" + e.getMessage());
        }
    }

    @Override
    public OrderDTO.OrderInfo getOrderDetail(Long orderId) {
        // 获取订单基本信息和用户信息
        OrderDTO.OrderInfo orderInfo = orderMapper.getOrderWithUser(orderId);
        if (orderInfo == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 获取订单商品信息
        List<OrderDTO.OrderItemInfo> items = orderMapper.getOrderItems(orderId);
        orderInfo.setItems(items);
        
        return orderInfo;
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(Long orderId, Integer status) {
        // 验证订单状态的有效性
        if (status < 0 || status > 4) {
            throw new BusinessException("无效的订单状态");
        }
        
        // 验证订单是否存在
        Orders existingOrder = orderMapper.selectOneById(orderId);
        if (existingOrder == null) {
            throw new BusinessException("订单不存在");
        }

        Orders order = new Orders();
        order.setId(orderId);
        order.setStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        return orderMapper.update(order) > 0;
    }

    @Override
    @Transactional
    public boolean deleteOrder(Long orderId) {
        // 验证订单是否存在
        Orders existingOrder = orderMapper.selectOneById(orderId);
        if (existingOrder == null) {
            throw new BusinessException("订单不存在");
        }
        return orderMapper.deleteById(orderId) > 0;
    }

    @Override
    @Transactional
    public OrderDTO.OrderInfo createOrder(Long userId, OrderDTO.CreateOrderRequest request) {
        // 1. 获取选中的购物车商品
        List<CartDTO> selectedItems = cartService.getSelectedCartItems(userId);
        if (selectedItems == null || selectedItems.isEmpty()) {
            throw new BusinessException("未选择任何商品");
        }
        
        // 2. 获取收货地址信息
        UserAddress address = userAddressMapper.selectOneById(request.getAddressId());
        if (address == null) {
            throw new BusinessException("收货地址不存在");
        }
        
        // 3. 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartDTO item : selectedItems) {
            BigDecimal price = item.getProduct().getProductPrice();
            BigDecimal quantity = new BigDecimal(item.getQuantity());
            totalAmount = totalAmount.add(price.multiply(quantity));
        }
        
        try {
            // 4. 创建订单
            Orders order = new Orders();
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setDiscountAmount(BigDecimal.ZERO);  // 默认无优惠
            order.setPayAmount(totalAmount);  // 默认实付金额等于总金额
            order.setStatus(0);  // 0-待支付
            order.setReceiverName(address.getReceiverName());
            order.setReceiverPhone(address.getReceiverPhone());
            order.setAddress(String.format("%s%s%s%s", 
                address.getProvince(), 
                address.getCity(), 
                address.getDistrict(), 
                address.getDetailAddress()));
            order.setRemark(request.getRemark());
            order.setCreateTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());

            // 5. 处理优惠券
            if (request.getUserCouponId() != null) {
                // 使用优惠券并获取优惠后的金额
                BigDecimal discountedAmount = userCouponService.useCoupon(
                    request.getUserCouponId(), 
                    order.getId(), 
                    totalAmount
                );
                BigDecimal discountAmount = totalAmount.subtract(discountedAmount);
                order.setDiscountAmount(discountAmount);
                order.setPayAmount(discountedAmount);
                order.setUserCouponId(request.getUserCouponId());
            }
            
            // 6. 插入订单并获取自动生成的主键
            orderMapper.insert(order);
            
            if (order.getId() == null) {
                throw new BusinessException("创建订单失败：无法获取订单ID");
            }
            
            // 7. 创建订单商品项
            for (CartDTO item : selectedItems) {
                // 获取最新的商品信息
                Product product = productMapper.selectOneById(item.getProductId());
                if (product == null) {
                    throw new BusinessException("商品不存在：" + item.getProductId());
                }
                if (product.getStock() < item.getQuantity()) {
                    throw new BusinessException("商品库存不足：" + product.getName());
                }
                
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setProductId(item.getProductId());
                orderItem.setProductName(product.getName());  // 使用最新的商品名称
                orderItem.setPrice(product.getPrice());      // 使用最新的商品价格
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSubtotal(product.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())));
                orderItemMapper.insert(orderItem);
                
                // 8. 更新商品库存
                product.setStock(product.getStock() - item.getQuantity());
                productMapper.update(product);
            }
            
            // 9. 清空已结算的购物车商品
            List<Long> cartIds = selectedItems.stream()
                    .map(CartDTO::getId)
                    .collect(Collectors.toList());
            cartService.clearSettledItems(cartIds);
            
            // 10. 返回订单信息
            return getOrderDetail(order.getId());
            
        } catch (Exception e) {
            throw new BusinessException("创建订单失败：" + e.getMessage());
        }
    }
} 