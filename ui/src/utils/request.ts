import axios from 'axios'
import type { Result } from '@/types/api'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = token
    }
    return config
  },
  error => {
    return Promise.reject(new Error(error.message))
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data as Result
    if (res.code !== 200) {
      ElMessage.error(res.message)
      return Promise.reject(new Error(res.message))
    }
    return response
  },
  error => {
    if (error.response) {
      const { status } = error.response
      if (status === 401) {
        ElMessage.error('登录已过期，请重新登录')
        const userStore = useUserStore()
        userStore.logout()
        router.push('/login')
      } else {
        ElMessage.error(error.response.data?.message || '请求失败')
      }
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request 