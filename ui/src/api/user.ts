import request from '@/utils/request'
import type { UserLoginParams, UserRegisterParams, Result, LoginResponse, UpdatePasswordParams } from '@/types/api'

export const userApi = {
  login(data: UserLoginParams) {
    const formData = new FormData()
    formData.append('username', data.username)
    formData.append('password', data.password)
    formData.append('captcha', data.captcha)

    return request.post<Result<LoginResponse>>('/user/login', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  
  register(data: UserRegisterParams) {
    const formData = new FormData()
    formData.append('username', data.username)
    formData.append('password', data.password)
    formData.append('email', data.email)

    return request.post<Result>('/user/register', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  
  updatePassword(data: UpdatePasswordParams) {
    const formData = new FormData()
    formData.append('oldPassword', data.oldPassword)
    formData.append('newPassword', data.newPassword)
    formData.append('emailCode', data.emailCode)

    return request.post<Result>('/user/update_password', formData)
  },

  sendEmailCode(email: string) {
    const formData = new FormData()
    formData.append('email', email)
    
    return request.post<Result>('/email/captcha/send', formData)
  }
} 