package _712.final_project_712.service;

import _712.final_project_712.dto.CategoryDTO;
import _712.final_project_712.mapper.ProductCategoryMapper;
import _712.final_project_712.model.ProductCategory;
import _712.final_project_712.service.impl.CategoryServiceImpl;
import com.mybatisflex.core.query.QueryChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private ProductCategoryMapper categoryMapper;

    private ProductCategory parentCategory;
    private ProductCategory childCategory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        // 准备测试数据
        parentCategory = new ProductCategory();
        parentCategory.setId(1L);
        parentCategory.setName("电脑");
        parentCategory.setLevel(1);
        parentCategory.setParentId(0L);
        parentCategory.setStatus(1);

        childCategory = new ProductCategory();
        childCategory.setId(2L);
        childCategory.setName("笔记本");
        childCategory.setLevel(2);
        childCategory.setParentId(1L);
        childCategory.setStatus(1);
        
        // 模拟 mapper 的基本方法
        when(categoryMapper.selectListByQuery(any())).thenAnswer(invocation -> {
            // 根据不同的测试场景返回不同的数据
            return Arrays.asList(parentCategory, childCategory);
        });
    }

    @Test
    public void testGetAllCategories() {
        // 执行测试
        List<CategoryDTO> result = categoryService.getAllCategories();

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("电脑", result.get(0).getName());
        assertEquals(1, result.get(0).getChildren().size());
        assertEquals("笔记本", result.get(0).getChildren().get(0).getName());
    }

    @Test
    public void testGetCategoriesByLevel() {
        // 模拟特定层级的查询结果
        when(categoryMapper.selectListByQuery(any())).thenReturn(Arrays.asList(parentCategory));

        // 执行测试
        List<CategoryDTO> result = categoryService.getCategoriesByLevel(1);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("电脑", result.get(0).getName());
        assertEquals(1, result.get(0).getLevel());
    }

    @Test
    public void testGetSubCategories() {
        // 模拟子分类查询结果
        when(categoryMapper.selectListByQuery(any())).thenReturn(Arrays.asList(childCategory));

        // 执行测试
        List<CategoryDTO> result = categoryService.getSubCategories(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("笔记本", result.get(0).getName());
        assertEquals(1L, result.get(0).getParentId());
    }
} 