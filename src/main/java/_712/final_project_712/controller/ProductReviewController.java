package _712.final_project_712.controller;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.model.ProductReview;
import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.ReviewQueryDTO;
import _712.final_project_712.service.ProductReviewService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "商品评价管理", description = "商品评价相关接口")
@RestController
@RequestMapping("/review")
public class ProductReviewController {

    @Autowired
    private ProductReviewService reviewService;

    @Operation(summary = "获取评价列表")
    @GetMapping("/list")
    public Result<Page<ProductReview>> getReviewList(ReviewQueryDTO queryDTO) {
        try {
            Page<ProductReview> page = reviewService.getReviewList(queryDTO);
            return Result.success(page);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取评价列表失败");
        }
    }

    @Operation(summary = "获取评价详情")
    @GetMapping("/{reviewId}")
    public Result<ProductReview> getReviewDetail(
            @Parameter(description = "评价ID") @PathVariable Long reviewId) {
        try {
            ProductReview review = reviewService.getReviewDetail(reviewId);
            if (review == null) {
                return Result.error("无评价信息");
            }
            return Result.success(review);
        } catch (Exception e) {
            return Result.error("获取评价详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "添加评价")
    @PostMapping("/{orderId}/{productId}")
    public Result<?> addReview(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            @Parameter(description = "商品ID") @PathVariable Long productId,
            @RequestBody ProductReview review) {
        // 基础参数校验
        if (review == null) {
            return Result.error("评价信息不能为空");
        }
        
        // 设置订单ID和商品ID
        review.setOrderId(orderId);
        review.setProductId(productId);
        
        // 评分校验
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            return Result.error("评分必须在1-5之间");
        }
        
        try {
            boolean success = reviewService.addReview(review);
            return success ? Result.success() : Result.error("添加失败");
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("添加评价失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新评价状态")
    @PutMapping("/{reviewId}/status/{status}")
    public Result<?> updateReviewStatus(
            @Parameter(description = "评价ID") @PathVariable Long reviewId,
            @Parameter(description = "状态:0-隐藏,1-显示") @PathVariable Integer status) {
        boolean success = reviewService.updateReviewStatus(reviewId, status);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除评价")
    @DeleteMapping("/{reviewId}")
    public Result<?> deleteReview(
            @Parameter(description = "评价ID") @PathVariable Long reviewId) {
        boolean success = reviewService.deleteReview(reviewId);
        return success ? Result.success() : Result.error("删除失败");
    }
} 