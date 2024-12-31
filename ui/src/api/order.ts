import request from '@/utils/request'

export interface OrderItem {
  id: number
  orderId: number
  productId: number
  productName: string
  productPrice: number
  productImage: string
  quantity: number
  subtotal: number
}

export interface Order {
  id: number
  userId: number
  userName: string
  userEmail: string
  totalAmount: number
  status: number
  addressId: number
  remark: string
  items: OrderItem[]
  createTime: string
  updateTime: string
}

// 获取订单列表
export function getOrders() {
  return request({
    url: '/order/list',
    method: 'get'
  })
}

// 创建订单
export function createOrder(data: {
  addressId: number
  remark?: string
  items: { productId: number; quantity: number }[]
}) {
  return request({
    url: '/order/create',
    method: 'post',
    data
  })
}

// 取消订单
export function cancelOrder(id: number) {
  return request({
    url: `/order/cancel/${id}`,
    method: 'put'
  })
}

// 删除订单
export function deleteOrder(id: number) {
  return request({
    url: `/order/delete/${id}`,
    method: 'delete'
  })
} 