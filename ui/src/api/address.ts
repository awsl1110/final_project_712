import request from '@/utils/request'
import type { Result, Address } from '@/types/api'

export const addressApi = {
  // 获取用户的所有收货地址
  getAddressList() {
    return request.get<Result<Address[]>>('/address/list')
  }
} 