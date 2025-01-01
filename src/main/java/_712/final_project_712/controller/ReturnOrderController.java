package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.ReturnOrder;
import _712.final_project_712.service.ReturnOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "退货管理", description = "退货相关接口")
@RestController
@RequestMapping("/return-orders")
public class ReturnOrderController {

    @Autowired
    private ReturnOrderService returnOrderService;

    @Operation(summary = "查询退货信息")
    @GetMapping("/order/{orderId}")
    public Result<ReturnOrder> getReturnOrderByOrderId(
        @Parameter(description = "订单ID") @PathVariable Long orderId
    ) {
        try {
            ReturnOrder returnOrder = returnOrderService.getReturnOrderByOrderId(orderId);
            if (returnOrder == null) {
                return Result.error(404, "未找到退货记录");
            }
            return Result.success(returnOrder);
        } catch (Exception e) {
            return Result.error(500, "系统繁忙，请稍后重试");
        }
    }
} 