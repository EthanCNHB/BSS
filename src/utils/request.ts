// 进行axios二次封装：使用请求与响应拦截器
import axios from 'axios'
import { ElMessage } from 'element-plus'
import useUserStore from '@/store/modules/user'

// 第一步：利用axios对象的create方法创建一个axios实例
let request = axios.create({
  baseURL: import.meta.env.VITE_SERVER, // 请求的基础路径
  // baseURL: 'http://localhost:8080/',
  timeout: 5000, // 请求超时的时间
})

// 第二步：request实例添加请求与响应拦截器
request.interceptors.request.use(
  (config) => {
    // 获取用户相关的小仓库：获取仓库内部token，登录成功后携带给服务器
    let userStore = useUserStore()
    if (userStore.state.token) {
      // 添加 Bearer 前缀
      config.headers.Authorization = `Bearer ${userStore.state.token}`
    }
    // 返回配置对象
    return config
  },
  (error) => {
    // 处理请求错误
    return Promise.reject(error)
  },
)

// 第三步：响应拦截器
request.interceptors.response.use(
  // 成功的回调函数
  (response) => {
    // 简化数据
    return response.data
  },
  (error) => {
    // 失败的回调函数:处理http网络错误
    let message = ''
    let status = error.response.status
    switch (status) {
      case 400:
        message = '请求错误(400)'
        break
      case 401:
        message = 'Token过期(401)'
        break
      case 403:
        message = '拒绝访问(403)'
        break
      case 404:
        message = '请求出错(404)'
        break
      case 408:
        message = '请求超时(408)'
        break
      case 500:
        message = '服务器错误(500)'
        break
      default:
        message = `网络出现问题`
        break
    }
    // 提示错误信息
    ElMessage({
      type: 'error',
      message,
    })
    // 返回一个Promise对象
    return Promise.reject(error)
  },
)

// 第四步：对外暴露
export default request
