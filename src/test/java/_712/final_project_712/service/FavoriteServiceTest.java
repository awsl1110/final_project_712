package _712.final_project_712.service;

import _712.final_project_712.model.dto.FavoriteDTO;
import _712.final_project_712.model.Favorite;
import _712.final_project_712.mapper.FavoriteMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static _712.final_project_712.model.table.FavoriteTableDef.FAVORITE;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @BeforeEach
    public void setUp() {
        // 清理测试数据，添加where条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(FAVORITE.USER_ID.in(1L, 2L, 3L));
        favoriteMapper.deleteByQuery(queryWrapper);
    }

    @Test
    public void testAddFavorite() {
        // 测试添加收藏
        long userId = 1L;
        long productId = 1L;

        // 测试添加收藏
        boolean result = favoriteService.addFavorite(userId, productId);
        assertTrue(result, "添加收藏应该成功");

        // 验证收藏是否添加成功
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(FAVORITE.USER_ID.eq(userId))
                .and(FAVORITE.PRODUCT_ID.eq(productId));
        long count = favoriteMapper.selectCountByQuery(queryWrapper);
        assertEquals(1, count, "数据库中应该存在一条收藏记录");

        // 测试重复添加
        result = favoriteService.addFavorite(userId, productId);
        assertFalse(result, "重复添加收藏应该失败");
    }

    @Test
    public void testDeleteFavorite() {
        // 先添加一条收藏记录
        Favorite favorite = new Favorite();
        favorite.setUserId(2L);
        favorite.setProductId(2L);
        favorite.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(favorite);

        // 查询刚插入的记录
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(FAVORITE.USER_ID.eq(2L))
                .and(FAVORITE.PRODUCT_ID.eq(2L));
        Favorite insertedFavorite = favoriteMapper.selectOneByQuery(queryWrapper);
        assertNotNull(insertedFavorite, "插入的收藏记录不应为null");
        assertNotNull(insertedFavorite.getId(), "收藏记录ID不应为null");

        // 测试删除收藏
        boolean result = favoriteService.deleteFavorite(insertedFavorite.getId());
        assertTrue(result, "删除存在的收藏应该成功");

        // 验证是否删除成功
        long count = favoriteMapper.selectCountByQuery(
                QueryWrapper.create().where(FAVORITE.ID.eq(insertedFavorite.getId()))
        );
        assertEquals(0, count, "收藏记录应该已被删除");

        // 测试删除不存在的收藏
        result = favoriteService.deleteFavorite(999999L);
        assertFalse(result, "删除不存在的收藏应该失败");
    }

    @Test
    public void testGetFavoritesByUserId() {
        long userId = 3L;  // 使用不同的用户ID

        // 添加两条测试数据
        Favorite favorite1 = new Favorite();
        favorite1.setUserId(userId);
        favorite1.setProductId(3L);
        favorite1.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(favorite1);

        Favorite favorite2 = new Favorite();
        favorite2.setUserId(userId);
        favorite2.setProductId(4L);
        favorite2.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(favorite2);

        // 测试获取收藏列表
        List<FavoriteDTO> favorites = favoriteService.getFavoritesByUserId(userId);
        assertNotNull(favorites, "收藏列表不应为null");
        assertEquals(2, favorites.size(), "应该有两条收藏记录");
        
        // 测试获取其他用户的收藏列表
        List<FavoriteDTO> otherUserFavorites = favoriteService.getFavoritesByUserId(999L);
        assertTrue(otherUserFavorites.isEmpty(), "不存在的用户应该返回空列表");
    }
} 