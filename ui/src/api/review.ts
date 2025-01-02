import request from '@/utils/request'
import type { ProductReviewListResponse } from '@/types/api'

/**
 * 获取商品评价列表
 * @param productId 商品ID
 * @returns Promise<ProductReviewListResponse>
 */
export function getProductReviews(productId: number) {
  return request<ProductReviewListResponse>({
    url: '/review/list',
    method: 'GET',
    params: {
      productId
    }
  })
} 