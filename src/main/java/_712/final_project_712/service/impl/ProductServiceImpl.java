package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.ProductMapper;
import _712.final_project_712.model.dto.ProductDTO;
import _712.final_project_712.service.ProductService;
import _712.final_project_712.Exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("分类ID不能为空");
        }
        return productMapper.findByCategoryId(categoryId);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productMapper.findAllProducts();
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        ProductDTO product = productMapper.findById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        return product;
    }
} 