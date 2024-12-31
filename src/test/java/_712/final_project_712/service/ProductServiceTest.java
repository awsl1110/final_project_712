package _712.final_project_712.service;

import _712.final_project_712.mapper.ProductMapper;
import _712.final_project_712.model.dto.ProductDTO;
import _712.final_project_712.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDTO testProduct;
    private List<ProductDTO> testProducts;

    @BeforeEach
    void setUp() {
        // 创建测试数据
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

        // 配置Mock行为
        when(productMapper.findByCategoryId(1L)).thenReturn(testProducts);
    }

    @Test
    void testGetProductsByCategory() {
        // 测试正常情况
        List<ProductDTO> products = productService.getProductsByCategory(1L);
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("测试商品", products.get(0).getName());

        // 测试分类ID为空的情况
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductsByCategory(null);
        });
        assertEquals("分类ID不能为空", exception.getMessage());
    }
} 