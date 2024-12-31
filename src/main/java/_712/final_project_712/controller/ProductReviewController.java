package _712.final_project_712.controller;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.model.ProductReview;
import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.ReviewQueryDTO;
import _712.final_project_712.service.FileService;
import _712.final_project_712.service.ProductReviewService;
import _712.final_project_712.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "商品评价管理", description = "商品评价相关接口")
@RestController
@RequestMapping("/review")
public class ProductReviewController {

    @Autowired
    private ProductReviewService reviewService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FileService fileService;

    @Operation(summary = "获取订单评价详情")
    @GetMapping("/{orderId}")
    public Result<ProductReview> getReviewDetail(
            @Parameter(description = "订单ID") @PathVariable Long orderId) {
        try {
            ProductReview review = reviewService.getReviewByOrderId(orderId);
            if (review == null) {
                return Result.error("该订单暂无评价信息");
            }
            return Result.success(review);
        } catch (Exception e) {
            return Result.error("获取评价详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取商品评价列表")
    @GetMapping("/list")
    public Result<Page<ProductReview>> getReviewList(
            @Parameter(description = "商品ID", required = true) @RequestParam Long productId) {
        try {
            ReviewQueryDTO queryDTO = new ReviewQueryDTO();
            queryDTO.setProductId(productId);
            queryDTO.setStatus(1); // 只查询显示状态的评价
            
            Page<ProductReview> page = reviewService.getReviewList(queryDTO);
            
            // 检查是否有评价数据
            if (page.getRecords().isEmpty()) {
                return Result.error("此商品无评价");
            }
            
            return Result.success(page);
        } catch (Exception e) {
            return Result.error("获取商品评价列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "添加评价")
    @PostMapping("/{orderId}/{productId}")
    public Result<?> addReview(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            @Parameter(description = "商品ID") @PathVariable Long productId,
            @Parameter(description = "评分") @RequestParam Integer rating,
            @Parameter(description = "评价内容") @RequestParam String content,
            @Parameter(description = "评价图片") @RequestParam(value = "file", required = false) MultipartFile[] files,
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            ProductReview review = new ProductReview();
            review.setUserId(userId);
            review.setOrderId(orderId);
            review.setProductId(productId);
            review.setRating(rating);
            review.setContent(content);
            
            // 处理图片上传
            if (files != null && files.length > 0) {
                List<String> imageUrls = new ArrayList<>();
                for (MultipartFile file : files) {
                    // 使用现有的文件上传服务保存图片
                    String url = fileService.saveAvatar(file, userId);
                    imageUrls.add(url);
                }
                // 将图片URL列表转换为JSON数组字符串
                review.setImages(new ObjectMapper().writeValueAsString(imageUrls));
            } else {
                review.setImages("[]");
            }
            
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