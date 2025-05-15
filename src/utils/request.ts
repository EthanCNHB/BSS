import { computed } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { GET_TOKEN } from '@/utils/token'

// 创建 axios 实例
const request = axios.create({
  baseURL: import.meta.env.VITE_SERVER,
  timeout: 5000,
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    const token = GET_TOKEN()
    if (token && config.headers) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error),
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    // 兼容结构：{ code, data, msg }
    const res = response.data
    // 成功状态判断
    if (res.code !== 0) {
      ElMessage.error(res.msg || '请求失败')
      return Promise.reject(res)
    }
    return res
  },
  (error) => {
    const status = error.response?.status
    let message = '未知错误'

    switch (status) {
      case 400:
        message = '请求错误(400)'
        break
      case 401:
        message = '未授权，请重新登录(401)'
        break
      case 403:
        message = '拒绝访问(403)'
        break
      case 404:
        message = '请求地址不存在(404)'
        break
      case 408:
        message = '请求超时(408)'
        break
      case 500:
        message = '服务器错误(500)'
        break
    }

    ElMessage.error(message)
    return Promise.reject(error)
  },
)

export default request
