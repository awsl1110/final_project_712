package _712.final_project_712.controller;

import _712.final_project_712.model.Orders;
import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.OrderQueryDTO;
import _712.final_project_712.service.OrderService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单管理", description = "订单相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "获取订单列表")
    @PostMapping("/list")
    public Result<Page<Orders>> getOrderList(@RequestBody OrderQueryDTO queryDTO) {
        // 验证日期格式
        if (!queryDTO.isValidDateFormat()) {
            return Result.error("日期格式不正确，请使用yyyy-MM-dd'T'HH:mm:ss格式");
        }
        return Result.success(orderService.getOrderList(queryDTO));
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{orderId}")
    public Result<Orders> getOrderDetail(
            @Parameter(description = "订单ID") @PathVariable Long orderId) {
        return Result.success(orderService.getOrderDetail(orderId));
    }

    @Operation(summary = "更新订单状态")
    @PutMapping("/{orderId}/status/{status}")
    public Result<?> updateOrderStatus(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            @Parameter(description = "订单状态") @PathVariable Integer status) {
        boolean success = orderService.updateOrderStatus(orderId, status);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除订单")
    @DeleteMapping("/{orderId}")
    public Result<?> deleteOrder(
            @Parameter(description = "订单ID") @PathVariable Long orderId) {
        boolean success = orderService.deleteOrder(orderId);
        return success ? Result.success() : Result.error("删除失败");
    }
} 