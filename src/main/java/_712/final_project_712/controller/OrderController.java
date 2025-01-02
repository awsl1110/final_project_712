package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.OrderDTO;
import _712.final_project_712.service.OrderService;
import _712.final_project_712.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "订单管理", description = "订单相关接口")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "获取用户订单列表")
    @GetMapping("/list")
    public Result<List<OrderDTO.OrderInfo>> getUserOrders(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token) {
        try {
            // 从token中获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<OrderDTO.OrderInfo> orders = orderService.getUserOrders(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "创建订单")
    @PostMapping("/create")
    public Result<OrderDTO.OrderInfo> createOrder(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "创建订单请求", required = true)
            @RequestBody OrderDTO.CreateOrderRequest request) {
        try {
            String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtil.getUserIdFromToken(actualToken);
            if (userId == null) {
                return Result.error("无效的token");
            }
            
            OrderDTO.OrderInfo order = orderService.createOrder(userId, request);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{orderId}")
    public Result<OrderDTO.OrderInfo> getOrderDetail(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId) {
        try {
            OrderDTO.OrderInfo order = orderService.getOrderDetail(orderId);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "更新订单状态")
    @PutMapping("/{orderId}/status/{status}")
    public Result<Boolean> updateOrderStatus(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId,
            @Parameter(description = "订单状态", required = true)
            @PathVariable Integer status) {
        try {
            boolean success = orderService.updateOrderStatus(orderId, status);
            return success ? Result.success(true) : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "删除订单")
    @DeleteMapping("/{orderId}")
    public Result<Boolean> deleteOrder(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId) {
        try {
            boolean success = orderService.deleteOrder(orderId);
            return success ? Result.success(true) : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 