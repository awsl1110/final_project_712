import request from '@/utils/request'

// 购物车商品接口
export interface CartProduct {
  productName: string
  productPrice: number
  productImage: string
  productDescription: string
  productStock: number
  productStatus: number
}

// 购物车项目接口
export interface CartItem {
  id: number
  userId: number
  productId: number
  quantity: number
  selected: number
  updateTime: string
  product: CartProduct
}

// 购物车列表响应接口
export interface CartListRes {
  code: number
  message: string
  data: CartItem[]
}

// 购物车参数接口
export interface AddToCartParams {
  productId: number
  quantity: number
}

// 购物车响应接口
export interface CartResponse {
  code: number
  message: string
  data: null
}

/** 
 * 获取购物车列表
 * @returns {Promise} 购物车列表
 */
export function getCartList() {
  return request<CartListRes>({
    url: '/cart/list',
    method: 'get'
  })
}

/** 
 * 添加商品到购物车
 * @param {AddToCartParams} params 购物车参数
 * @returns {Promise} 添加结果
 */
export function addToCart(params: AddToCartParams) {
  return request<CartResponse>({
    url: '/cart/add',
    method: 'post',
    params: {
      productId: params.productId,
      quantity: params.quantity
    },
    headers: {
      'Authorization': localStorage.getItem('token') || ''
    }
  })
}

/** 
 * 更新购物车商品数量
 * @param {number} id 购物车项ID
 * @param {number} quantity 数量
 * @returns {Promise} 更新结果
 */
export function updateCartQuantity(id: number, quantity: number) {
  return request<CartResponse>({
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
 * @returns {Promise} 删除结果
 */
export function removeFromCart(productId: number) {
  return request<CartResponse>({
    url: '/cart/remove',
    method: 'delete',
    params: {
      productId
    },
    headers: {
      'Authorization': localStorage.getItem('token') || ''
    }
  })
}

/** 
 * 更新购物车商品选中状态
 * @param {number} cartId 购物车项ID
 * @param {number} selected 选中状态
 * @returns {Promise} 更新结果
 */
export function updateCartSelection(cartId: number, selected: number) {
  return request<CartResponse>({
    url: '/cart/selected',
    method: 'put',
    params: {
      cartId,
      selected
    },
    headers: {
      'Authorization': localStorage.getItem('token') || ''
    }
  })
}

/** 
 * 获取选中的购物车商品
 * @returns {Promise} 选中的购物车商品列表
 */
export function getSelectedCartItems() {
  return request<CartListRes>({
    url: '/cart/selected',
    method: 'get',
    headers: {
      'Authorization': localStorage.getItem('token') || ''
    }
  })
} 