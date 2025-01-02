import request from '@/utils/request'
import type { Result } from '@/types/api'
import type { Coupon, UserCoupon } from '@/types/api'

// 获取可用优惠券列表
export const getCoupons = () => {
  return request<Result<Coupon[]>>({
    url: '/coupon/available',
    method: 'get'
  })
}

// 获取用户优惠券列表
export const getUserCoupons = () => {
  return request<Result<UserCoupon[]>>({
    url: '/user-coupons',
    method: 'get'
  })
} 