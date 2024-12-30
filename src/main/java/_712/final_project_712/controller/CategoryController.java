package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.CategoryDTO;
import _712.final_project_712.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "商品分类", description = "商品分类相关接口")
@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "获取所有分类")
    @GetMapping
    public Result<List<CategoryDTO>> getAllCategories() {
        return Result.success(categoryService.getAllCategories());
    }

    @Operation(summary = "获取指定层级的分类")
    @GetMapping("/level/{level}")
    public Result<List<CategoryDTO>> getCategoriesByLevel(
        @Parameter(description = "分类层级") @PathVariable Integer level
    ) {
        return Result.success(categoryService.getCategoriesByLevel(level));
    }

    @Operation(summary = "获取子分类")
    @GetMapping("/{parentId}/children")
    public Result<List<CategoryDTO>> getSubCategories(
        @Parameter(description = "父分类ID") @PathVariable Long parentId
    ) {
        return Result.success(categoryService.getSubCategories(parentId));
    }
} 