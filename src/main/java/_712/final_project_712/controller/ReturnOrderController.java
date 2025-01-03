package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.ReturnOrder;
import _712.final_project_712.service.ReturnOrderService;
import _712.final_project_712.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "退货管理", description = "退货相关接口")
@RestController
@RequestMapping("/api/return-orders")
public class ReturnOrderController {

    @Autowired
    private ReturnOrderService returnOrderService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "查询退货信息")
    @GetMapping("/order/{orderId}")
    public Result<ReturnOrder> getReturnOrderByOrderId(
        @Parameter(description = "订单ID") @PathVariable Long orderId,
        @RequestHeader(value = "Authorization", required = false) String token
    ) {
        try {
            // 处理token，去掉Bearer前缀
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            ReturnOrder returnOrder = returnOrderService.getReturnOrderByOrderId(orderId);
            if (returnOrder == null) {
                return Result.error(404, "未找到退货记录");
            }
            return Result.success(returnOrder);
        } catch (Exception e) {
            return Result.error(500, "系统繁忙，请稍后重试");
        }
    }

    @Operation(summary = "更新退货状态")
    @PutMapping("/{returnId}/status")
    public Result<Void> updateReturnStatus(
        @Parameter(description = "退货ID") @PathVariable Long returnId,
        @Parameter(description = "状态：0-待处理，1-已同意，2-已拒绝，3-已完成") @RequestParam Integer status,
        @Parameter(description = "处理备注") @RequestParam(required = false) String handleNote,
        @RequestHeader(value = "Authorization", required = false) String token
    ) {
        try {
            // 处理token，去掉Bearer前缀
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            returnOrderService.updateReturnStatus(returnId, status, handleNote);
            return Result.success();
        } catch (Exception e) {
            return Result.error(500, "更新状态失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询用户的退货列表")
    @GetMapping("/user/{userId}")
    public Result<List<ReturnOrder>> getUserReturns(
        @Parameter(description = "用户ID") @PathVariable Long userId,
        @RequestHeader(value = "Authorization", required = false) String token
    ) {
        try {
            // 处理token，去掉Bearer前缀
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            List<ReturnOrder> returns = returnOrderService.getUserReturns(userId);
            return Result.success(returns);
        } catch (Exception e) {
            return Result.error(500, "查询失败：" + e.getMessage());
        }
    }
} 