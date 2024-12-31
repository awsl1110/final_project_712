import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { userApi } from '@/api/user'
import type { UserLoginParams, UserRegisterParams, UpdatePasswordParams } from '@/types/api'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') ?? '')
  const username = ref(localStorage.getItem('username') ?? '')
  const avatarUrl = ref('')
  const isLoggedIn = computed(() => !!token.value)

  const login = async (loginForm: UserLoginParams) => {
    const res = await userApi.login(loginForm)
    const { data: { token: tokenValue } } = res.data
    
    if (!tokenValue) {
      throw new Error('登录失败')
    }
    
    // 先清理旧的状态
    if (avatarUrl.value) {
      URL.revokeObjectURL(avatarUrl.value)
      avatarUrl.value = ''
    }
    
    // 设置新的token和用户名
    localStorage.setItem('token', tokenValue)
    localStorage.setItem('username', loginForm.username)
    token.value = tokenValue
    username.value = loginForm.username
  }

  const logout = () => {
    if (avatarUrl.value) {
      URL.revokeObjectURL(avatarUrl.value)
    }
    token.value = ''
    username.value = ''
    avatarUrl.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('username')
  }

  const fetchAvatar = async (retryCount = 3) => {
    // 如果没有token，不要尝试获取头像
    if (!isLoggedIn.value) {
      return
    }

    try {
      // 先清理旧的URL
      if (avatarUrl.value) {
        URL.revokeObjectURL(avatarUrl.value)
        avatarUrl.value = ''
      }

      const res = await userApi.getAvatar()
      const contentType = res.headers['content-type']
      
      // 检查响应类型
      if (!contentType?.startsWith('image/')) {
        throw new Error('获取头像失败：返回的不是图片文件')
      }
      
      // 创建新的URL
      avatarUrl.value = URL.createObjectURL(new Blob([res.data], { type: contentType }))
    } catch (error: any) {
      console.error('获取头像失败:', error)
      avatarUrl.value = ''
      
      // 如果是404错误，说明用户还没有上传头像，不需要显示错误提示
      if (error.response?.status === 404) {
        return
      }
      
      // 如果是token相关错误且还有重试次数，等待后重试
      if (error.response?.data?.message?.includes('token') && retryCount > 0) {
        await new Promise(resolve => setTimeout(resolve, 1000))
        return fetchAvatar(retryCount - 1)
      }
      
      // 如果重试次数用完或是其他错误，执行登出
      if (error.response?.data?.message?.includes('token')) {
        logout()
        return
      }
      
      throw error
    }
  }

  const uploadAvatar = async (file: File, retryCount = 3) => {
    if (!isLoggedIn.value) {
      throw new Error('请先登录')
    }

    try {
      // 尝试上传文件
      const res = await userApi.uploadAvatar(file)
      
      // 如果上传成功，等待一段时间后获取新头像
      if (res.data.code === 200) {
        // 清理旧的URL
        if (avatarUrl.value) {
          URL.revokeObjectURL(avatarUrl.value)
          avatarUrl.value = ''
        }
        
        await new Promise(resolve => setTimeout(resolve, 500))
        await fetchAvatar()
        return true
      }
      
      throw new Error(res.data.message || '上传失败')
    } catch (error: any) {
      console.error('上传头像失败:', error)
      
      // 如果是token相关错误且还有重试次数，等待后重试
      if (error.response?.data?.message?.includes('token') && retryCount > 0) {
        await new Promise(resolve => setTimeout(resolve, 1000))
        return uploadAvatar(file, retryCount - 1)
      }
      
      // 如果是token相关错误且重试次数用完，执行登出
      if (error.response?.data?.message?.includes('token')) {
        logout()
      }
      
      throw error
    }
  }

  const register = async (registerForm: UserRegisterParams) => {
    const res = await userApi.register(registerForm)
    if (res.data.code !== 200) {
      throw new Error(res.data.message)
    }
  }

  const updatePassword = async (data: UpdatePasswordParams) => {
    const res = await userApi.updatePassword(data)
    if (res.data.code !== 200) {
      throw new Error(res.data.message)
    }
  }

  return {
    token,
    username,
    avatarUrl,
    isLoggedIn,
    login,
    logout,
    register,
    updatePassword,
    uploadAvatar,
    fetchAvatar
  }
}) 