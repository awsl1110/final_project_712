import request from '@/utils/request'

// 商品信息接口
export interface Product {
  id: number | null
  name: string
  description: string
  price: number
  stock: number
  categoryId: number
  brand: string
  model: string
  specifications: string | null
  imageUrl: string
  status: number
  createTime: string | null
  updateTime: string | null
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
  remark?: string
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
 * @param {GetFavoritesParams} params 查询参数
 * @returns {Promise<GetFavoritesRes>} 收藏列表
 */
export function getFavorites(params: GetFavoritesParams = {}): Promise<GetFavoritesRes> {
  return request({
    url: '/favorites',
    method: 'get',
    params
  })
}

/** 
 * 添加商品到收藏
 * @param {number} productId 商品ID
 * @param {AddToFavoriteParams} params 其他参数
 * @returns {Promise<AddToFavoriteRes>} 添加结果
 */
export function addToFavorite(productId: number, params: AddToFavoriteParams = {}): Promise<AddToFavoriteRes> {
  return request({
    url: `/favorites/${productId}`,
    method: 'post',
    data: params
  })
}

/** 
 * 取消收藏商品
 * @param {number} favoriteId 收藏ID
 * @returns {Promise<AddToFavoriteRes>} 取消结果
 */
export function removeFavorite(favoriteId: number): Promise<AddToFavoriteRes> {
  return request({
    url: `/favorites/${favoriteId}`,
    method: 'delete'
  })
} 