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

export interface UpdatePasswordParams {
  oldPassword: string
  newPassword: string
  emailCode: string
  confirmPassword: string
  email: string
} 