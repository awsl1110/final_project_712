import request from '@/utils/request'
import type { ProductReviewListResponse, Result } from '@/types/api'

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

/**
 * 添加商品评价
 * @param orderId 订单ID
 * @param productId 商品ID
 * @param data 评价数据
 * @returns Promise<Result>
 */
export function addProductReview(
  orderId: number,
  productId: number,
  data: {
    rating: number
    content: string
    files?: File[]
  }
) {
  const formData = new FormData()
  formData.append('rating', data.rating.toString())
  formData.append('content', data.content)
  if (data.files) {
    data.files.forEach(file => {
      formData.append('file', file)
    })
  }

  return request<Result>({
    url: `/review/${orderId}/${productId}`,
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  })
} 