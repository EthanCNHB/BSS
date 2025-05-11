// 创建用户相关的小仓库
import { defineStore } from 'pinia' // 引入 defineStore 用于定义 Pinia 的 store
import { computed, reactive } from 'vue' // 引入 Vue 的组合式 API 函数
import { reqLogin, reqStudentInfo, reqLogout } from '@/api/student' // 引入用于登录的 API 接口
// import type { loginForm, loginResponseData } from '@/api/user/type' // 引入登录请求和响应数据的类型定义
import type { UserState } from '@/store/modules/type' // 引入用户状态的类型定义
import { SET_TOKEN, GET_TOKEN, REMOVE_TOKEN } from '@/utils/token'
import { constantRoute } from '@/router/routes'
import { loginResponseData } from '@/api/student/type'
// 创建用户小仓库
const useUserStore = defineStore('User', () => {
  // 使用 reactive 创建响应式的 state
  const state = reactive<UserState>({
    token: GET_TOKEN(), // 从 localStorage 获取 token
    menuRoutes: constantRoute, //仓库存储生成菜单需要数组（路由）
    username: '',
    avatar: '',
  })

  // 计算属性：判断用户是否已登录
  const isLoggedIn = computed(() => !!state.token)
  // 用户登录的方法
  const userLogin = async (data: any): Promise<number> => {
    try {
      const result: any = await reqLogin(data)

      if (result.code === 0) {
        updateToken(result)
        return 200 // 登录成功
      } else {
        handleLoginError(result.message)
        return 201 // 登录失败
      }
    } catch (error) {
      handleApiError(error)
      return 500 // 处理异常情况
    }
  }
  //获取用户信息的方法
  const userInfo = async () => {
    let result = await reqStudentInfo()
    console.log(result)
    //如果获取用户信息成功，存储一下用户信息
    if (result.code === 0) {
      state.username = result.data.username
      return 'ok'
    } else {
      return Promise.reject('获取用户信息失败')
    }
  }

  //退出登录
  const userLogout = () => {
    state.token = ''
    state.username = ''
    state.avatar = ''
    REMOVE_TOKEN()
  }
  // 更新 token 并存储到 localStorage
  const updateToken = (result: any) => {
    if (result.code == 0 && result.data) {
      state.token = result.data // 更新响应式 token
      SET_TOKEN(result.data) // 将 token 存储到 localStorage
    } else {
      console.error('未能更新 token: 响应数据格式不正确', result)
    }
  }

  // 处理登录错误
  const handleLoginError = (message?: string) => {
    const errorMessage = message || '登录失败，未提供详细信息'
    console.error('登录错误:', errorMessage)
  }

  // 处理 API 错误
  const handleApiError = (error: unknown) => {
    const errorMessage = error instanceof Error ? error.message : '未知错误'
    console.error('登录请求发生错误:', errorMessage)
  }

  // 返回状态和方法
  return { state, isLoggedIn, userLogin, userInfo, userLogout }
})

export default useUserStore // 导出用户小仓库
