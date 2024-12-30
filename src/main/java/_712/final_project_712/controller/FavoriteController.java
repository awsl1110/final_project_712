package _712.final_project_712.controller;

import _712.final_project_712.model.Favorite;
import _712.final_project_712.service.FavoriteService;
import _712.final_project_712.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/list")
    public List<Favorite> getFavorites(@RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return favoriteService.getFavoritesByUserId(userId);
    }
} 