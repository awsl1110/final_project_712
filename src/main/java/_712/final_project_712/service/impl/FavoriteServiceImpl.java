package _712.final_project_712.service.impl;

import _712.final_project_712.model.dto.FavoriteDTO;
import _712.final_project_712.mapper.FavoriteMapper;
import _712.final_project_712.service.FavoriteService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}