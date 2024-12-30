package _712.final_project_712.service;

import _712.final_project_712.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    List<CategoryDTO> getCategoriesByLevel(Integer level);
    List<CategoryDTO> getSubCategories(Long parentId);
} 