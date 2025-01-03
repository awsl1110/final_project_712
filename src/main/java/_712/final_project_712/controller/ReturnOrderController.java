package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.ReturnOrder;
import _712.final_project_712.service.ReturnOrderService;
import _712.final_project_712.util.JwtUtil;
import _712.final_project_712.model.dto.CreateReturnOrderDTO;
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
    @GetMapping("/info")
    public Result<ReturnOrder> getReturnOrder(
        @RequestHeader("Authorization") String token
    ) {
        try {
            // 从token获取用户ID
            Long userId = getUserIdFromToken(token);
            
            // 查询用户最近的退货记录
            List<ReturnOrder> returns = returnOrderService.getUserReturns(userId);
            
            if (returns == null || returns.isEmpty()) {
                return Result.error(404, "未找到退货记录");
            }
            
            // 返回最新的退货记录
            return Result.success(returns.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询用户的退货列表")
    @GetMapping("/list")
    public Result<List<ReturnOrder>> getUserReturns(
        @RequestHeader("Authorization") String token
    ) {
        try {
            // 从token获取用户ID
            Long userId = getUserIdFromToken(token);
            List<ReturnOrder> returns = returnOrderService.getUserReturns(userId);
            return Result.success(returns);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新退货状态")
    @PutMapping("/{returnId}/status")
    public Result<Void> updateReturnStatus(
        @Parameter(description = "退货ID") @PathVariable Long returnId,
        @Parameter(description = "状态：0-待处理，1-已同意，2-已拒绝，3-已完成") @RequestParam Integer status,
        @Parameter(description = "处理备注") @RequestParam(required = false) String handleNote,
        @RequestHeader("Authorization") String token
    ) {
        try {
            returnOrderService.updateReturnStatus(returnId, status, handleNote);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "创建退货申请")
    @PostMapping
    public Result<ReturnOrder> createReturnOrder(
        @RequestBody CreateReturnOrderDTO dto,
        @RequestHeader("Authorization") String token
    ) {
        try {
            // 基本参数验证
            if (dto.getOrderId() == null || dto.getReturnAmount() == null || 
                dto.getReturnReason() == null || dto.getReturnReason().trim().isEmpty()) {
                return Result.error(400, "请填写完整的退货信息");
            }

            // 处理token
            String processedToken = token;
            if (token.startsWith("Bearer ")) {
                processedToken = token.substring(7);
            }

            Long userId = jwtUtil.getUserIdFromToken(processedToken);
            if (userId == null) {
                return Result.error(401, "token无效，请重新登录");
            }

            // 创建退货申请
            ReturnOrder created = returnOrderService.createReturnOrder(dto, userId);
            return Result.success(created);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("退货记录已存在")) {
                return Result.error(400, e.getMessage());
            }
            return Result.error(500, "创建退货申请失败：" + e.getMessage());
        }
    }

    private Long getUserIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
} 