package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.FavoriteMapper;
import _712.final_project_712.model.Favorite;
import _712.final_project_712.service.FavoriteService;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static _712.final_project_712.model.table.FavoriteTableDef.FAVORITE;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public List<Favorite> getFavoritesByUserId(long userId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(FAVORITE.USER_ID.eq(userId))
                .orderBy(FAVORITE.CREATE_TIME.desc());
        return favoriteMapper.selectListByQuery(queryWrapper);
    }
}