package _712.final_project_712.service.impl;

import _712.final_project_712.dto.CategoryDTO;
import _712.final_project_712.mapper.ProductCategoryMapper;
import _712.final_project_712.model.ProductCategory;
import _712.final_project_712.service.CategoryService;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static _712.final_project_712.model.table.ProductCategoryTableDef.PRODUCT_CATEGORY;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private ProductCategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<ProductCategory> categories = categoryMapper.selectListByQuery(
            QueryChain.of(ProductCategory.class)
                .where(PRODUCT_CATEGORY.STATUS.eq(1))
                .orderBy(PRODUCT_CATEGORY.SORT_ORDER.asc())
        );
        return buildCategoryTree(categories);
    }

    @Override
    public List<CategoryDTO> getCategoriesByLevel(Integer level) {
        List<ProductCategory> categories = categoryMapper.selectListByQuery(
            QueryChain.of(ProductCategory.class)
                .where(PRODUCT_CATEGORY.LEVEL.eq(level))
                .and(PRODUCT_CATEGORY.STATUS.eq(1))
                .orderBy(PRODUCT_CATEGORY.SORT_ORDER.asc())
        );
        return convertToDTO(categories);
    }

    @Override
    public List<CategoryDTO> getSubCategories(Long parentId) {
        List<ProductCategory> categories = categoryMapper.selectListByQuery(
            QueryChain.of(ProductCategory.class)
                .where(PRODUCT_CATEGORY.PARENT_ID.eq(parentId))
                .and(PRODUCT_CATEGORY.STATUS.eq(1))
                .orderBy(PRODUCT_CATEGORY.SORT_ORDER.asc())
        );
        return convertToDTO(categories);
    }

    private List<CategoryDTO> buildCategoryTree(List<ProductCategory> categories) {
        List<CategoryDTO> allCategories = convertToDTO(categories);
        Map<Long, List<CategoryDTO>> parentIdMap = allCategories.stream()
            .collect(Collectors.groupingBy(dto -> dto.getParentId() == null ? 0L : dto.getParentId()));
        List<CategoryDTO> rootCategories = parentIdMap.getOrDefault(0L, new ArrayList<>());
        rootCategories.forEach(root -> setChildren(root, parentIdMap));
        return rootCategories;
    }

    private void setChildren(CategoryDTO parent, Map<Long, List<CategoryDTO>> parentIdMap) {
        List<CategoryDTO> children = parentIdMap.get(parent.getId());
        if (children != null) {
            parent.setChildren(children);
            children.forEach(child -> setChildren(child, parentIdMap));
        }
    }

    private List<CategoryDTO> convertToDTO(List<ProductCategory> categories) {
        return categories.stream().map(category -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setParentId(category.getParentId());
            dto.setLevel(category.getLevel());
            dto.setSortOrder(category.getSortOrder());
            return dto;
        }).collect(Collectors.toList());
    }
} 