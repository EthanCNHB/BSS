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

  const token = userStore.token
  const role = userStore.role

  if (token) {
    if (to.path === '/login' || to.path === '/register') {
      next({ path: '/' })
      return
    }

    // 如果 role 为空，就调用 restoreSession（它会从本地读 token/role 并拉 profile）
    if (!role) {
      try {
        await userStore.restoreSession()
      } catch {
        userStore.logout()
        next({ path: '/login', query: { redirect: to.path } })
        return
      }
    }

    // 拿到最新的 role
    const currentRole = userStore.role

    // 权限校验
    if (hasPermission(currentRole, to)) {
      next()
    } else {
      next({ path: '/403' })
    }
  } else {
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next({ path: '/login', query: { redirect: to.path } })
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
