package _712.final_project_712.service;

import _712.final_project_712.model.dto.ProductDTO;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getProductsByCategory(Long categoryId);
} 