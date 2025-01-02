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

/** 
 * 获取商品详情
 * @param {number} id 商品ID
 * @returns {Promise<Result<Product>>} 商品详情
 */
export function getProductDetail(id: number) {
  return request({
    url: `/product/${id}`,
    method: 'get'
  })
}

/**
 * 获取所有商品分类
 * @returns {Promise<ProductCategoryListResponse>} 商品分类列表
 */
export function getProductCategories() {
  return request({
    url: '/product/categories',
    method: 'get'
  })
}

/**
 * 获取分类商品列表
 * @param {number} categoryId 分类ID
 * @returns {Promise<Result<Product[]>>} 商品列表
 */
export function getProductsByCategory(categoryId: number) {
  return request({
    url: `/product/category/${categoryId}`,
    method: 'get'
  })
} 