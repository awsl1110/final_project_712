package _712.final_project_712.service.impl;

import _712.final_project_712.model.dto.FavoriteDTO;
import _712.final_project_712.mapper.FavoriteMapper;
import _712.final_project_712.model.Favorite;
import _712.final_project_712.service.FavoriteService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

import static _712.final_project_712.model.table.FavoriteTableDef.FAVORITE;
import static _712.final_project_712.model.table.ProductTableDef.PRODUCT;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public List<FavoriteDTO> getFavoritesByUserId(long userId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(FAVORITE.ALL_COLUMNS)
                .select(PRODUCT.NAME.as("productName"))
                .from(FAVORITE)
                .leftJoin(PRODUCT).on(FAVORITE.PRODUCT_ID.eq(PRODUCT.ID))
                .where(FAVORITE.USER_ID.eq(userId))
                .orderBy(FAVORITE.CREATE_TIME.desc());
        return favoriteMapper.selectListByQueryAs(queryWrapper, FavoriteDTO.class);
    }

    @Override
    public boolean deleteFavorite(long favoriteId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(FAVORITE.ID.eq(favoriteId));
        return favoriteMapper.deleteByQuery(queryWrapper) > 0;
    }

    @Override
    public boolean addFavorite(long userId, long productId) {
        // 检查是否已经收藏
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(FAVORITE.USER_ID.eq(userId))
                .and(FAVORITE.PRODUCT_ID.eq(productId));
        
        if (favoriteMapper.selectCountByQuery(queryWrapper) > 0) {
            return false;
        }

        // 创建新的收藏记录
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        favorite.setCreateTime(LocalDateTime.now());
        
        return favoriteMapper.insert(favorite) > 0;
    }
}