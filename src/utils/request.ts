// src/utils/request.ts
import axios, { AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { GET_TOKEN } from '@/utils/token'

/** 通用响应体接口 */
interface ApiResponse<T = any> {
  code: number
  data: T
  message: string
}

// 1. 创建 axios 实例
const request = axios.create({
  baseURL: import.meta.env.VITE_SERVER as string,
  timeout: 5000,
  withCredentials: true,
})

// 2. 请求拦截器：自动带上 Token
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

// 3. 响应拦截器：统一处理 code !== 0 的错误
request.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const res = response.data
    if (res.code !== 0) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(res)
    }
    // 只返回 data 部分
    return res.data
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

/**
 * 发起 GET 请求，并直接返回 T
 */
export function get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
  return request.get<T, T>(url, config)
}

/**
 * 发起 POST 请求，并直接返回 T
 */
export function post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
  return request.post<any, T>(url, data, config)
}

/**
 * 发起 PUT 请求，并直接返回 T
 */
export function put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
  return request.put<any, T>(url, data, config)
}

/**
 * 发起 PATCH 请求，并直接返回 T
 */
export function patch<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
  return request.patch<any, T>(url, data, config)
}
/**
 * 发起 DELETE 请求，并直接返回 T
 */
export function del<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
  return request.delete<any, T>(url, config)
}
export default request
