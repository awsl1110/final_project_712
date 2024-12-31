package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.ProductDTO;
import _712.final_project_712.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "商品查询", description = "商品相关接口")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "获取所有商品列表")
    @GetMapping("/list")
    public Result<List<ProductDTO>> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts();
            return Result.success(products);
        } catch (Exception e) {
            return Result.error("获取商品列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取分类商品列表")
    @GetMapping("/category/{categoryId}")
    public Result<List<ProductDTO>> getProductsByCategory(
            @Parameter(description = "分类ID") 
            @PathVariable Long categoryId) {
        try {
            List<ProductDTO> products = productService.getProductsByCategory(categoryId);
            return Result.success(products);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("获取商品列表失败：" + e.getMessage());
        }
    }
} 