package _712.final_project_712.controller;

import _712.final_project_712.model.Result;
import _712.final_project_712.model.dto.FavoriteDTO;
import _712.final_project_712.service.FavoriteService;
import _712.final_project_712.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "收藏管理", description = "收藏相关接口")
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(
        summary = "获取收藏列表",
        description = "获取当前用户的收藏列表",
        security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    )
    @GetMapping
    public Result<List<FavoriteDTO>> getFavorites(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<FavoriteDTO> favorites = favoriteService.getFavoritesByUserId(userId);
            return Result.success(favorites);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(
        summary = "删除收藏",
        description = "根据收藏ID删除收藏记录",
        security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    )
    @DeleteMapping("/{favoriteId}")
    public Result<Boolean> deleteFavorite(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "收藏ID", required = true)
            @PathVariable Long favoriteId) {
        try {
            boolean success = favoriteService.deleteFavorite(favoriteId);
            return success ? Result.success(success) : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(
        summary = "添加收藏",
        description = "添加商品到收藏列表",
        security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    )
    @PostMapping("/{productId}")
    public Result<Boolean> addFavorite(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "商品ID", required = true)
            @PathVariable Long productId) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            boolean success = favoriteService.addFavorite(userId, productId);
            return success ? Result.success(success) : Result.error("收藏失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 