import router from './router'
import nprogress from 'nprogress' // 导入进度条库
import 'nprogress/nprogress.css' // 导入进度条样式
import pinia from './store' // 导入状态管理库
import useUserStore from './store/modules/user' // 导入用户状态模块
import setting from './setting' // 导入应用设置

let userStore = useUserStore(pinia) // 创建用户状态实例
let username = userStore.state.username // 获取当前用户名

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
  // 设置文档标题
  document.title = to.meta.title + ' | ' + setting.title
  nprogress.start() // 开始进度条

  let token = userStore.state.token // 获取用户令牌

  if (token) {
    // 如果用户已登录
    if (to.path === '/login') {
      // 如果要访问登录页
      next({ path: '/' }) // 重定向到首页
    } else {
      if (username) {
        // 如果用户信息存在
        next() // 继续导航
      } else {
        try {
          await userStore.userInfo() // 获取用户信息
          next() // 获取成功，继续导航
        } catch (error) {
          userStore.userLogout() // 获取失败，执行登出
          next({ path: '/login', query: { redirect: to.path } }) // 重定向到登录页
        }
      }
    }
  } else {
    // 如果用户未登录
    if (to.path === '/login') {
      next() // 继续访问登录页
    } else {
      next({ path: '/login', query: { redirect: to.path } }) // 重定向到登录页
    }
  }
})

// 全局后置守卫
router.afterEach((to, from) => {
  nprogress.done() // 结束进度条
})
