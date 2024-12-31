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

export function getCartList() {
  return request({
    url: '/cart/list',
    method: 'get'
  })
}

export function addToCart(productId: number, quantity: number = 1) {
  return request({
    url: '/cart/add',
    method: 'post',
    data: {
      productId,
      quantity
    }
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

export function removeFromCart(id: number) {
  return request({
    url: `/cart/delete/${id}`,
    method: 'delete'
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