// API 接口类型定义
export interface LoginResponse {
  success: boolean
  message: string
  token: string | null
}

export interface Result<T = any> {
  code: number
  message: string
  data: T
}

export interface UserLoginParams {
  username: string
  password: string
  captcha: string
}

export interface UserRegisterParams {
  username: string
  password: string
  email: string
}

export interface Address {
  id: number
  userId: number
  receiverName: string
  receiverPhone: string
  province: string
  city: string
  district: string
  detailAddress: string
  isDefault: boolean
  createTime: string
  updateTime: string
}

export interface UserProfile {
  id: number
  name: string
  email: string
  addresses: Address[]
}

export interface UpdatePasswordParams {
  oldPassword: string
  newPassword: string
  emailCode: string
  confirmPassword: string
  email: string
}

// 优惠券类型定义
export interface Coupon {
  id: number
  name: string
  type: number
  value: number
  minAmount: number
  startTime: string
  endTime: string
  total: number
  remain: number
  status: number
  createTime: string
  updateTime: string
}

// 用户优惠券类型定义
export interface UserCoupon {
  id: number
  userId: number
  couponId: number
  status: number
  useTime: string
  orderId: number
  createTime: string
  coupon: Coupon
}

// 订单商品信息
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

// 订单信息
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

// 订单列表响应
export interface OrderListResponse {
  code: number
  message: string
  data: OrderInfo[]
}

// 商品评价类型定义
export interface ProductReview {
  id: number
  userId: number
  productId: number
  orderId: number
  rating: number
  content: string
  images: string
  status: number
  createTime: string
  updateTime: string
}

// 商品评价分页响应
export interface PageProductReview {
  records: ProductReview[]
  pageNumber: number
  pageSize: number
  totalPage: number
  totalRow: number
  optimizeCountQuery: boolean
}

// 商品评价列表响应
export interface ProductReviewListResponse {
  code: number
  message: string
  data: PageProductReview
} 