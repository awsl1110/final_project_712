package _712.final_project_712.service.impl;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.mapper.ProductReviewMapper;
import _712.final_project_712.mapper.OrderMapper;
import _712.final_project_712.mapper.OrderItemMapper;
import _712.final_project_712.model.ProductReview;
import _712.final_project_712.model.Orders;
import _712.final_project_712.model.OrderItem;
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

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

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
        // 1. 校验订单是否存在
        Orders order = orderMapper.selectOneById(review.getOrderId());
        if (order == null) {
            throw new BusinessException("无此订单");
        }
        
        // 2. 校验订单商品是否匹配
        QueryWrapper queryWrapper = QueryWrapper.create()
            .select("*")
            .from("order_items")
            .where("order_id = ?", review.getOrderId())
            .and("product_id = ?", review.getProductId());
        
        OrderItem orderItem = orderItemMapper.selectOneByQuery(queryWrapper);
        if (orderItem == null) {
            throw new BusinessException("该订单中不包含此商品");
        }
        
        // 3. 设置默认值
        if (review.getStatus() == null) {
            review.setStatus(1);  // 默认显示
        }
        
        // 4. 设置时间
        LocalDateTime now = LocalDateTime.now();
        review.setCreateTime(now);
        review.setUpdateTime(now);
        
        // 5. 处理评价内容
        if (review.getContent() == null) {
            review.setContent("");
        }
        
        // 6. 处理图片数据
        if (review.getImages() == null || review.getImages().trim().isEmpty()) {
            review.setImages("[]");
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.readTree(review.getImages());
            } catch (Exception e) {
                throw new BusinessException("图片数据格式不正确，请使用JSON数组格式");
            }
        }
        
        // 7. 保存评价
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

    @Override
    public ProductReview getReviewByOrderId(Long orderId) {
        if (orderId == null) {
            throw new BusinessException("订单ID不能为空");
        }
        
        QueryWrapper queryWrapper = QueryWrapper.create()
            .select("*")
            .from("product_review")
            .where("order_id = ?", orderId);
            
        return reviewMapper.selectOneByQuery(queryWrapper);
    }
} 