package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.ProductDTO;
import _712.final_project_712.model.ProductCategory;
import _712.final_project_712.service.ProductService;
import _712.final_project_712.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "商品查询", description = "商品相关接口")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductCategoryService categoryService;

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

    @Operation(summary = "获取所有商品分类")
    @GetMapping("/categories")
    public Result<List<ProductCategory>> getAllCategories() {
        try {
            List<ProductCategory> categories = categoryService.getAllCategories();
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error("获取分类列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/{productId}")
    public Result<ProductDTO> getProductById(
            @Parameter(description = "商品ID") 
            @PathVariable Long productId) {
        try {
            ProductDTO product = productService.getProductById(productId);
            return Result.success(product);
        } catch (Exception e) {
            return Result.error("获取商品详情失败：" + e.getMessage());
        }
    }
} 