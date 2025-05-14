import router from './router'
import nprogress from 'nprogress' // 导入进度条库
import 'nprogress/nprogress.css' // 导入进度条样式
import pinia from './store' // 导入状态管理库
import { useUserStore } from './store/modules/user' // 导入用户状态模块
import setting from './setting' // 导入应用设置

let userStore = useUserStore(pinia) // 创建用户状态实例

// 定义无需登录即可访问的白名单页面
const whiteList = ['/login', '/register']

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
  document.title = to.meta.title + ' | ' + setting.title
  nprogress.start()

  const token = userStore.token // 获取当前的token
  const role = userStore.role // 获取当前用户的角色

  // 判断是否需要登录
  if (token) {
    // 已登录
    if (to.path === '/login' || to.path === '/register') {
      // 如果登录后尝试访问登录页或注册页，跳首页
      next({ path: '/' })
    } else {
      // 已登录并访问其他页面
      if (role) {
        // 如果用户信息存在，检查是否有访问权限
        if (hasPermission(role, to)) {
          next()
        } else {
          next({ path: '/403' }) // 如果没有权限，跳转到403页面
        }
      } else {
        try {
          await userStore.getUserInfo() // 获取用户信息
          if (hasPermission(role, to)) {
            next()
          } else {
            next({ path: '/403' }) // 如果没有权限，跳转到403页面
          }
        } catch (error) {
          userStore.logout() // 获取失败则登出
          next({ path: '/login', query: { redirect: to.path } })
        }
      }
    }
  } else {
    // 未登录
    if (whiteList.includes(to.path)) {
      next() // 白名单直接放行
    } else {
      next({ path: '/login', query: { redirect: to.path } }) // 跳转登录页
    }
  }
})

// 判断用户角色是否有权限访问目标路由
const hasPermission = (role: string, to: any) => {
  const routeRoles = to.meta.roles || []
  return routeRoles.includes(role) || routeRoles.length === 0 // 如果没有角色限制，默认可以访问
}

// 全局后置守卫
router.afterEach(() => {
  nprogress.done()
})
