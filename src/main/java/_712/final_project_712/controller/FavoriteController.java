package _712.final_project_712.controller;

import _712.final_project_712.model.dto.FavoriteDTO;
import _712.final_project_712.service.FavoriteService;
import _712.final_project_712.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "收藏管理", description = "收藏相关接口")
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(
        summary = "获取收藏列表",
        description = "获取当前登录用户的所有收藏商品列表，按收藏时间倒序排序",
        security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    )
    @GetMapping("/list")
    public List<FavoriteDTO> getFavorites(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return favoriteService.getFavoritesByUserId(userId);
    }

    @Operation(
        summary = "删除收藏",
        description = "根据收藏ID删除收藏记录",
        security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    )
    @DeleteMapping("/{favoriteId}")
    public boolean deleteFavorite(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "收藏ID", required = true)
            @PathVariable Long favoriteId) {
        return favoriteService.deleteFavorite(favoriteId);
    }

    @Operation(
        summary = "添加收藏",
        description = "添加商品到收藏列表",
        security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    )
    @PostMapping("/{productId}")
    public boolean addFavorite(
            @Parameter(description = "用户认证token", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "商品ID", required = true)
            @PathVariable Long productId) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return favoriteService.addFavorite(userId, productId);
    }
} 