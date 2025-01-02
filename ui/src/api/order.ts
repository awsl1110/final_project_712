import request from '@/utils/request'
import type { OrderListResponse } from '@/types/api'

// 删除订单响应接口
export interface DeleteOrderRes {
  code: number
  message: string
  data: null
}

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

// 创建订单请求接口
export interface CreateOrderRequest {
  cartIds: number[]
  addressId: number
  remark?: string
}

// 创建订单响应接口
export interface OrderInfo {
  id: number
  userId: number
  userName: string
  userEmail: string
  totalAmount: number
  discountAmount: number
  payAmount: number
  userCouponId: number
  receiverName: string
  receiverPhone: string
  address: string
  status: number
  remark: string
  createTime: string
  updateTime: string
  items: OrderItemInfo[]
}

export interface OrderItemInfo {
  id: number
  orderId: number
  productId: number
  productName: string
  productPrice: number
  productImage: string
  quantity: number
  subtotal: number
}

export interface ResultOrderInfo {
  code: number
  message: string
  data: OrderInfo
}

// 获取订单列表
export function getOrders() {
  const token = localStorage.getItem('token')
  return request<OrderListResponse>({
    url: '/order/list',
    method: 'get',
    headers: {
      'Authorization': token || ''
    }
  })
}

// 创建订单
export function createOrder(data: CreateOrderRequest) {
  const token = localStorage.getItem('token')
  return request<ResultOrderInfo>({
    url: '/order/create',
    method: 'post',
    data,
    headers: {
      'Authorization': token ? `Bearer ${token}` : ''
    }
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
export function deleteOrder(orderId: number): Promise<DeleteOrderRes> {
  return request({
    url: `/order/${orderId}`,
    method: 'delete'
  })
}

// 获取订单详情
export function getOrderDetail(orderId: number) {
  const token = localStorage.getItem('token')
  return request<ResultOrderInfo>({
    url: `/order/${orderId}`,
    method: 'get',
    headers: {
      'Authorization': token || ''
    }
  })
} 