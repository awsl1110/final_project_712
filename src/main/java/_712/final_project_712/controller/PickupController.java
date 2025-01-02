package _712.final_project_712.controller;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.model.PickupCode;
import _712.final_project_712.model.Result;
import _712.final_project_712.service.PickupService;
import _712.final_project_712.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "取件管理")
@RestController
@RequestMapping("/api/pickup")
public class PickupController {

    @Autowired
    private PickupService pickupService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "生成取件码")
    @PostMapping("/generate")
    public PickupCode generatePickupCode(@RequestHeader("Authorization") String token) {
        String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Long userId = jwtUtil.getUserIdFromToken(actualToken);
        if (userId == null) {
            throw new BusinessException("无效的token");
        }
        
        return pickupService.generatePickupCode(userId);
    }
    
    @Operation(summary = "验证取件码并完成取件")
    @PostMapping("/verify")
    public Result<?> pickup(@RequestParam String phone, @RequestParam String pickupCode) {
        try {
            pickupService.pickup(phone, pickupCode);
            return Result.success("取件成功");
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("取件失败，请稍后重试");
        }
    }
} 