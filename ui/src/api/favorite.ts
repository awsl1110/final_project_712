import request from '@/utils/request'
import type { Result } from '@/types/api'

// 商品信息接口
export interface Product {
  id: number
  name: string
  description: string
  price: number
  stock: number
  categoryId: number
  brand: string
  model: string
  specifications: string
  imageUrl: string
  status: number
  createTime: string
  updateTime: string
}

// 收藏商品项接口
export interface FavoriteItem {
  id: number
  productId: number
  createTime: string
  product: Product
}

// 收藏商品参数接口
export interface AddToFavoriteParams {
  productId: number
}

// 收藏商品响应接口
export interface AddToFavoriteRes {
  code: number
  message: string
  data: null
}

// 获取收藏列表参数接口
export interface GetFavoritesParams {
  page?: number
  pageSize?: number
}

// 获取收藏列表响应接口
export interface GetFavoritesRes {
  code: number
  message: string
  data: FavoriteItem[]
}

/** 
 * 获取收藏列表
 * @returns {Promise} 收藏列表
 */
export function getFavorites() {
  return request({
    url: '/favorites',
    method: 'get'
  })
}

/** 
 * 添加商品到收藏
 * @param {number} productId 商品ID
 * @returns {Promise<Result<boolean>>} 添加结果
 */
export function addToFavorite(productId: number) {
  console.log('Sending favorite request for productId:', productId)
  return request<Result<boolean>>({
    url: `/favorites/${productId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    // 添加 transformRequest 来处理请求数据
    transformRequest: [(data, headers) => {
      console.log('Request headers:', headers)
      return ''  // POST 请求不需要请求体，因为参数在 URL 中
    }]
  })
}

/** 
 * 取消收藏商品
 * @param {number} favoriteId 收藏ID
 * @returns {Promise} 取消结果
 */
export function removeFavorite(favoriteId: number) {
  return request({
    url: `/favorites/${favoriteId}`,
    method: 'delete'
  })
} 