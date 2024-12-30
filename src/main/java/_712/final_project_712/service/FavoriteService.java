package _712.final_project_712.service;

import _712.final_project_712.model.Favorite;
import java.util.List;

public interface FavoriteService {
    List<Favorite> getFavoritesByUserId(long userId);
} 