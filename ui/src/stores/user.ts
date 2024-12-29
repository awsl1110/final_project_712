import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userApi } from '@/api/user'
import type { UserLoginParams, UserRegisterParams, UpdatePasswordParams } from '@/types/api'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') ?? '')
  const username = ref(localStorage.getItem('username') ?? '')

  const login = async (loginForm: UserLoginParams) => {
    const res = await userApi.login(loginForm)
    const loginResult = res.data.data
    
    if (!loginResult.success) {
      throw new Error(loginResult.message)
    }
    
    if (loginResult.token) {
      token.value = loginResult.token
      username.value = loginForm.username
      localStorage.setItem('token', loginResult.token)
      localStorage.setItem('username', loginForm.username)
    }
  }

  const logout = () => {
    token.value = ''
    username.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('username')
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
    login,
    logout,
    register,
    updatePassword
  }
}) 