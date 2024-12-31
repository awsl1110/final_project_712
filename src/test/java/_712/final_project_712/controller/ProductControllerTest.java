package _712.final_project_712.controller;

import _712.final_project_712.model.dto.ProductDTO;
import _712.final_project_712.model.ProductCategory;
import _712.final_project_712.service.ProductService;
import _712.final_project_712.service.ProductCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    
    @MockBean
    private ProductCategoryService categoryService;

    private ProductDTO testProduct;
    private List<ProductDTO> testProducts;
    private List<ProductCategory> testCategories;

    @BeforeEach
    void setUp() {
        // 创建商品测试数据
        testProduct = new ProductDTO();
        testProduct.setId(1L);
        testProduct.setName("测试商品");
        testProduct.setDescription("测试商品描述");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setStock(100);
        testProduct.setCategoryId(1L);
        testProduct.setCategoryName("测试分类");
        testProduct.setBrand("测试品牌");
        testProduct.setModel("测试型号");
        testProduct.setStatus(1);
        testProduct.setCreateTime(LocalDateTime.now());
        testProduct.setUpdateTime(LocalDateTime.now());

        testProducts = Arrays.asList(testProduct);

        // 创建分类测试数据
        ProductCategory category = new ProductCategory();
        category.setId(1L);
        category.setName("测试分类");
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        testCategories = Arrays.asList(category);

        // 配置Mock行为
        when(productService.getProductsByCategory(1L)).thenReturn(testProducts);
        when(categoryService.getAllCategories()).thenReturn(testCategories);
    }

    @Test
    void testGetProductsByCategory() throws Exception {
        mockMvc.perform(get("/product/category/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].name").value("测试商品"))
                .andExpect(jsonPath("$.data[0].categoryName").value("测试分类"));

        // 测试无效的分类ID
        when(productService.getProductsByCategory(-1L))
                .thenThrow(new IllegalArgumentException("无效的分类ID"));

        mockMvc.perform(get("/product/category/-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("无效的分类ID"));
    }

    @Test
    void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/product/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].name").value("测试分类"));

        // 测试异常情况
        when(categoryService.getAllCategories())
                .thenThrow(new RuntimeException("测试异常"));

        mockMvc.perform(get("/product/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("获取分类列表失败：测试异常"));
    }
} 