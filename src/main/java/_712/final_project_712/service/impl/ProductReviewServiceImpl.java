package _712.final_project_712.service.impl;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.ProductReviewMapper;
import _712.final_project_712.model.ProductReview;
import _712.final_project_712.model.dto.ReviewQueryDTO;
import _712.final_project_712.service.ProductReviewService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static _712.final_project_712.model.table.ProductReviewTableDef.PRODUCT_REVIEW;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {

    @Autowired
    private ProductReviewMapper reviewMapper;

    @Override
    public Page<ProductReview> getReviewList(ReviewQueryDTO queryDTO) {
        try {
            // 确保分页参数有效
            if (queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1) {
                queryDTO.setPageNum(1);
            }
            if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
                queryDTO.setPageSize(10);
            }

            QueryWrapper queryWrapper = QueryWrapper.create()
                .select("*")  // 使用 * 代替 ALL_COLUMNS
                .from("product_review");  // 使用表名字符串
                
            // 添加查询条件
            if (queryDTO.getUserId() != null) {
                queryWrapper.where("user_id = ?", queryDTO.getUserId());
            }
            if (queryDTO.getProductId() != null) {
                queryWrapper.where("product_id = ?", queryDTO.getProductId());
            }
            if (queryDTO.getOrderId() != null) {
                queryWrapper.where("order_id = ?", queryDTO.getOrderId());
            }
            if (queryDTO.getRating() != null) {
                queryWrapper.where("rating = ?", queryDTO.getRating());
            }
            if (queryDTO.getStatus() != null) {
                queryWrapper.where("status = ?", queryDTO.getStatus());
            }
            if (queryDTO.getStartTime() != null) {
                queryWrapper.where("create_time >= ?", queryDTO.getStartTime());
            }
            if (queryDTO.getEndTime() != null) {
                queryWrapper.where("create_time <= ?", queryDTO.getEndTime());
            }

            // 添加排序
            queryWrapper.orderBy("create_time desc");

            // 打印SQL语句用于调试
            System.out.println("Generated SQL: " + queryWrapper.toSQL());
            
            return reviewMapper.paginate(queryDTO.getPageNum(), queryDTO.getPageSize(), queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取评价列表失败：" + e.getMessage());
        }
    }

    @Override
    public ProductReview getReviewDetail(Long reviewId) {
        if (reviewId == null) {
            throw new BusinessException("评价ID不能为空");
        }
        
        ProductReview review = reviewMapper.selectOneById(reviewId);
        if (review == null) {
            throw new BusinessException("无评价信息");
        }
        return review;
    }

    @Override
    @Transactional
    public boolean addReview(ProductReview review) {
        // 参数校验
        if (review.getProductId() == null) {
            throw new BusinessException("商品ID不能为空");
        }
        if (review.getOrderId() == null) {
            throw new BusinessException("订单ID不能为空");
        }
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new BusinessException("评分必须在1-5之间");
        }
        
        // 设置默认值
        review.setCreateTime(LocalDateTime.now());
        review.setUpdateTime(LocalDateTime.now());
        review.setStatus(1); // 默认显示
        
        // 处理 content
        if (review.getContent() == null) {
            review.setContent("");
        }
        
        // 处理 images
        if (review.getImages() == null || review.getImages().trim().isEmpty()) {
            review.setImages("[]"); // 设置为空JSON数组
        } else {
            // 验证images是否是有效的JSON数组格式
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.readTree(review.getImages());
            } catch (Exception e) {
                throw new BusinessException("图片数据格式不正确，请使用JSON数组格式");
            }
        }
        
        try {
            return reviewMapper.insert(review) > 0;
        } catch (Exception e) {
            throw new BusinessException("添加评价失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateReviewStatus(Long reviewId, Integer status) {
        if (status != 0 && status != 1) {
            throw new BusinessException("无效的状态值");
        }
        
        ProductReview review = new ProductReview();
        review.setId(reviewId);
        review.setStatus(status);
        review.setUpdateTime(LocalDateTime.now());
        
        return reviewMapper.update(review) > 0;
    }

    @Override
    @Transactional
    public boolean deleteReview(Long reviewId) {
        return reviewMapper.deleteById(reviewId) > 0;
    }
} 