package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.ProductCategoryMapper;
import _712.final_project_712.model.ProductCategory;
import _712.final_project_712.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public List<ProductCategory> getAllCategories() {
        return productCategoryMapper.findAllCategories();
    }
} 