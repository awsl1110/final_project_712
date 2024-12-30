package _712.final_project_712.controller;

import _712.final_project_712.dto.CategoryDTO;
import _712.final_project_712.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testGetAllCategories() throws Exception {
        // 准备测试数据
        CategoryDTO parent = new CategoryDTO();
        parent.setId(1L);
        parent.setName("电脑");
        parent.setLevel(1);
        parent.setParentId(0L);

        CategoryDTO child = new CategoryDTO();
        child.setId(2L);
        child.setName("笔记本");
        child.setLevel(2);
        child.setParentId(1L);

        parent.setChildren(Arrays.asList(child));
        List<CategoryDTO> categories = Arrays.asList(parent);

        // 模拟服务层方法
        when(categoryService.getAllCategories()).thenReturn(categories);

        // 执行测试并验证结果
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("电脑"))
                .andExpect(jsonPath("$.data[0].children[0].name").value("笔记本"));
    }

    @Test
    public void testGetCategoriesByLevel() throws Exception {
        // 准备测试数据
        CategoryDTO category = new CategoryDTO();
        category.setId(1L);
        category.setName("电脑");
        category.setLevel(1);

        List<CategoryDTO> categories = Arrays.asList(category);

        // 模拟服务层方法
        when(categoryService.getCategoriesByLevel(1)).thenReturn(categories);

        // 执行测试并验证结果
        mockMvc.perform(get("/categories/level/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("电脑"))
                .andExpect(jsonPath("$.data[0].level").value(1));
    }

    @Test
    public void testGetSubCategories() throws Exception {
        // 准备测试数据
        CategoryDTO category = new CategoryDTO();
        category.setId(2L);
        category.setName("笔记本");
        category.setParentId(1L);

        List<CategoryDTO> categories = Arrays.asList(category);

        // 模拟服务层方法
        when(categoryService.getSubCategories(1L)).thenReturn(categories);

        // 执行测试并验证结果
        mockMvc.perform(get("/categories/1/children"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(2))
                .andExpect(jsonPath("$.data[0].name").value("笔记本"))
                .andExpect(jsonPath("$.data[0].parentId").value(1));
    }
} 