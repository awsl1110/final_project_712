package _712.final_project_712.service;

import _712.final_project_712.model.ProductReview;
import _712.final_project_712.model.dto.ReviewQueryDTO;
import com.mybatisflex.core.paginate.Page;

public interface ProductReviewService {
    // 分页查询评价列表
    Page<ProductReview> getReviewList(ReviewQueryDTO queryDTO);
    
    // 获取评价详情
    ProductReview getReviewDetail(Long reviewId);
    
    // 获取订单评价
    ProductReview getReviewByOrderId(Long orderId);
    
    // 添加评价
    boolean addReview(ProductReview review);
    
    // 修改评价状态
    boolean updateReviewStatus(Long reviewId, Integer status);
    
    // 删除评价
    boolean deleteReview(Long reviewId);
} 