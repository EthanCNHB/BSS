import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

// 定义不同角色的路由
const adminRoutes = [{ path: '/admin', name: 'Admin', meta: { roles: ['admin'] }, children: [] }]
const studentRoutes = [{ path: '/student', name: 'Student', meta: { roles: ['student'] }, children: [] }]
const teacherRoutes = [{ path: '/teacher', name: 'Teacher', meta: { roles: ['teacher'] }, children: [] }]
const defaultRoutes = [{ path: '/login', name: 'Login', meta: { roles: ['admin', 'student', 'teacher'] }, children: [] }]

export const useUserStore = defineStore('user', () => {
  // 用户的状态
  const token = ref<string | null>(null) // 用户登录的 token
  const role = ref<string>('') // 用户角色（例如：'admin', 'student', 'teacher'）
  const menuRoutes = ref<any[]>([]) // 当前用户的菜单路由

  // 获取用户角色
  const userRole = computed(() => role.value || '') // 为空时返回空字符串

  // 登录
  const login = (newToken: string, newRole: string) => {
    token.value = newToken || '' // 如果token为null，则设置为空字符串
    role.value = newRole || '' // 如果role为null，则设置为空字符串
    // 登录后，根据角色来设置菜单路由
    switch (newRole) {
      case 'admin':
        menuRoutes.value = adminRoutes
        break
      case 'student':
        menuRoutes.value = studentRoutes
        break
      case 'teacher':
        menuRoutes.value = teacherRoutes
        break
      default:
        menuRoutes.value = defaultRoutes // 如果角色不明确，跳转到登录页面
        return false // 返回失败
    }
    return true // 返回成功
  }

  // 登出
  const logout = () => {
    token.value = null
    role.value = ''
    menuRoutes.value = []
  }

  // 获取用户信息
  const getUserInfo = () => {
    // 这里可以从后端获取用户信息，或从 token 中提取角色等信息
    // 此处假设用户信息已经通过登录请求获取
  }

  // 添加一个方法来验证是否有权限访问某个页面
  const hasPermission = (route: any) => {
    // 避免role为空时报错，做空值检查
    return route.meta.roles?.includes(role.value || '') || false
  }

  return {
    token,
    role,
    menuRoutes,
    userRole,
    login,
    logout,
    getUserInfo,
    hasPermission,
  }
})
