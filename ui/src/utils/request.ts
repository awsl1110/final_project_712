import axios from 'axios'
import type { Result } from '@/types/api'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000,
  maxContentLength: Infinity,
  maxBodyLength: Infinity
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      // 确保headers对象存在
      config.headers = config.headers || {}
      // 直接使用token，不添加Bearer前缀
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
    // 如果是blob类型，检查content-type
    if (response.config.responseType === 'blob') {
      const contentType = response.headers['content-type']
      // 如果返回的不是图片，尝试解析错误信息
      if (!contentType?.startsWith('image/')) {
        return response.data.text().then((text: string) => {
          try {
            const error = JSON.parse(text)
            throw new Error(error.message || '获取头像失败')
          } catch {
            throw new Error(text || '获取头像失败')
          }
        })
      }
      return response
    }
    
    const res = response.data as Result<any>
    if (res.code !== 200) {
      const errorMsg = res.message || '操作失败'
      ElMessage.error(errorMsg)
      return Promise.reject(new Error(errorMsg))
    }
    return response
  },
  error => {
    if (error.response) {
      const { status, data } = error.response
      if (status === 401 || (data?.code === 500 && data?.message?.includes('token'))) {
        ElMessage.error('登录已过期，请重新登录')
        const userStore = useUserStore()
        userStore.logout()
        router.push('/login')
      } else if (status === 413) {
        ElMessage.error('文件太大，请选择小于2MB的文件')
      } else {
        // 尝试解析错误信息
        let errorMsg = '请求失败'
        if (typeof data === 'string') {
          errorMsg = data
        } else if (data?.message) {
          errorMsg = data.message
        } else if (data?.error) {
          errorMsg = data.error
        }
        ElMessage.error(errorMsg)
      }
    } else if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请重试')
    } else if (error.code === 'ERR_NETWORK') {
      ElMessage.error('网络连接失败，请检查网络设置')
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request 