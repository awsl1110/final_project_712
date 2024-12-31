import request from '@/utils/request'

export interface Product {
  id: number
  name: string
  description: string
  price: number
  stock: number
  categoryId: number
  categoryName: string
  brand: string
  model: string
  specifications: string
  imageUrl: string
  status: number
  createTime: string
  updateTime: string
}

export function getProducts() {
  return request({
    url: '/product/list',
    method: 'get'
  })
} 