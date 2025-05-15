import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from '@/utils/request'
import { SET_TOKEN, GET_TOKEN, REMOVE_TOKEN } from '@/utils/token'
import { SET_ROLE, GET_ROLE, REMOVE_ROLE } from '@/utils/role'
import { getTime } from '@/utils/time'
import { constantRoute } from '@/router/routes'
// 定义不同角色的路由

const filterRoutesByRole = (role: string) => {
  return constantRoute.filter((route) => {
    return !route.meta?.roles || route.meta.roles.includes(role)
  })
}

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string | null>(GET_TOKEN())
  const role = ref<string>(GET_ROLE() || '')
  const menuRoutes = ref<any[]>([])
  const username = ref<string>(localStorage.getItem('USERNAME') || '')
  const avatar = ref<string>('https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif')
  const greeting = computed(() => `${getTime()}好，${username.value}`)

  const userRole = computed(() => role.value || '')

  /**
   * 专门负责设置状态（不与后端交互）
   */
  const login = (newToken: string, newRole: string) => {
    token.value = newToken || ''
    role.value = newRole || ''

    // 本地持久化
    SET_TOKEN(newToken)
    SET_ROLE(newRole)

    switch (newRole) {
      case 'admin':
        menuRoutes.value = filterRoutesByRole(newRole)
        break
      case 'student':
        menuRoutes.value = filterRoutesByRole(newRole)
        break
      case 'teacher':
        menuRoutes.value = filterRoutesByRole(newRole)
        break
      default:
        menuRoutes.value = filterRoutesByRole(newRole)
        return false
    }
    return true
  }

  /**
   * 调用后端接口登录 + 设置状态
   */
  const doLogin = async (usernameInput: string, password: string) => {
    try {
      const res = await axios.post<{ token: string; role: string }>('/user/login', {
        username: usernameInput,
        password,
      })

      const { token: newToken, role: newRole } = res.data

      username.value = usernameInput
      const success = login(newToken, newRole)
      username.value = usernameInput
      localStorage.setItem('USERNAME', usernameInput)

      return success
    } catch (error) {
      console.error('登录异常:', error)
      return false
    }
  }

  /**
   * 登出
   */
  const logout = () => {
    token.value = null
    role.value = ''
    username.value = ''
    menuRoutes.value = []
    REMOVE_TOKEN()
    REMOVE_ROLE()
    localStorage.removeItem('USERNAME')
  }

  /**
   * 页面权限判断
   */
  const hasPermission = (route: any) => {
    return route.meta.roles?.includes(role.value || '') || false
  }

  /**
   * 页面刷新时恢复登录状态（如果未恢复则尝试）
   */
  const getUserInfo = () => {
    const savedToken = GET_TOKEN()
    const savedRole = GET_ROLE()
    const savedUsername = localStorage.getItem('USERNAME') || ''
    if (savedToken && savedRole) {
      token.value = savedToken
      role.value = savedRole
      username.value = savedUsername
      login(savedToken, savedRole)
    }
  }

  // 启动时尝试恢复
  if (token.value && role.value) {
    login(token.value, role.value)
  }

  return {
    token,
    role,
    username,
    avatar,
    greeting,
    menuRoutes,
    userRole,
    login,
    doLogin,
    logout,
    hasPermission,
    getUserInfo,
  }
})
