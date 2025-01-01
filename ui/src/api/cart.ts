import request from '@/utils/request'

export interface CartItem {
  id: number
  userId: number
  productId: number
  quantity: number
  selected: number
  createTime: string
  updateTime: string
  productName: string
  price: number
}

// 添加购物车参数接口
export interface AddToCartParams {
  remark?: string
}

// 添加购物车响应接口
export interface AddToCartRes {
  code: number
  message: string
  data: null
}

// 删除购物车商品参数接口
export interface RemoveFromCartParams {
  remark?: string
}

// 删除购物车商品响应接口
export interface RemoveFromCartRes {
  code: number
  message: string
  data: null
}

export function getCartList() {
  return request({
    url: '/cart/list',
    method: 'get'
  })
}

/** 
 * 添加商品到购物车
 * @param {number} productId 商品ID
 * @param {number} quantity 商品数量
 * @param {AddToCartParams} params 其他参数
 * @returns {Promise<AddToCartRes>} 添加结果
 */
export function addToCart(productId: number, quantity: number = 1, params: AddToCartParams = {}): Promise<AddToCartRes> {
  return request({
    url: `/cart/add?productId=${productId}&quantity=${quantity}`,
    method: 'post',
    data: params
  })
}

export function updateCartQuantity(id: number, quantity: number) {
  return request({
    url: '/cart/update',
    method: 'put',
    data: {
      id,
      quantity
    }
  })
}

/** 
 * 从购物车删除商品
 * @param {number} productId 商品ID
 * @param {RemoveFromCartParams} params 其他参数
 * @returns {Promise<RemoveFromCartRes>} 删除结果
 */
export function removeFromCart(productId: number, params: RemoveFromCartParams = {}): Promise<RemoveFromCartRes> {
  return request({
    url: `/cart/remove?productId=${productId}`,
    method: 'delete',
    data: params
  })
}

export function updateCartSelection(id: number, selected: number) {
  return request({
    url: '/cart/select',
    method: 'put',
    data: {
      id,
      selected
    }
  })
} 