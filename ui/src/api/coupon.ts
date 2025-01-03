import request from '@/utils/request'
import type { Result } from '@/types/api'
import type { Coupon, UserCoupon } from '@/types/api'

// 获取所有优惠券列表
export const getAllCoupons = () => {
  const token = localStorage.getItem('token')
  return request<Result<Coupon[]>>({
    url: '/coupons',
    method: 'get',
    headers: {
      'Authorization': token || '',
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}

// 获取可用优惠券列表
export const getCoupons = () => {
  const token = localStorage.getItem('token')
  return request<Result<Coupon[]>>({
    url: '/coupon/available',
    method: 'get',
    headers: {
      'Authorization': token || ''
    }
  })
}

// 获取用户优惠券列表
export const getUserCoupons = () => {
  const token = localStorage.getItem('token')
  return request<Result<UserCoupon[]>>({
    url: '/user-coupons',
    method: 'get',
    headers: {
      'Authorization': token || ''
    }
  })
}

// 领取优惠券
export const receiveCoupon = (couponId: number) => {
  const token = localStorage.getItem('token')
  return request<Result<object>>({
    url: `/user-coupons/${couponId}`,
    method: 'post',
    headers: {
      'Authorization': token || '',
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
} 