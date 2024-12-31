package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.ProductMapper;
import _712.final_project_712.model.dto.ProductDTO;
import _712.final_project_712.service.ProductService;
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
} 