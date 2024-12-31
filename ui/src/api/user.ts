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
  },

  getAvatar() {
    return request.get(`/file/avatar?t=${Date.now()}`, {
      responseType: 'blob'
    })
  },

  uploadAvatar(file: File) {
    // 检查文件大小（限制为2MB）
    if (file.size > 2 * 1024 * 1024) {
      return Promise.reject(new Error('头像文件大小不能超过2MB'))
    }

    // 检查文件类型
    if (!file.type.startsWith('image/')) {
      return Promise.reject(new Error('请上传图片文件'))
    }

    const formData = new FormData()
    formData.append('file', file)
    
    return request.post<Result<string>>('/file/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      timeout: 30000, // 增加上传超时时间到30秒
      onUploadProgress: (progressEvent) => {
        if (progressEvent.total) {
          const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total)
          console.log('上传进度:', percentCompleted + '%')
        }
      }
    })
  }
} 