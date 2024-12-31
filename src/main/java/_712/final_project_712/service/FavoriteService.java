package _712.final_project_712.service;

import _712.final_project_712.model.dto.FavoriteDTO;
import java.util.List;

public interface FavoriteService {
    List<FavoriteDTO> getFavoritesByUserId(long userId);
    boolean deleteFavorite(long favoriteId);
    boolean addFavorite(long userId, long productId);
} 