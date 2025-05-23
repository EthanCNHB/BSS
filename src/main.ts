// src/main.ts
import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'virtual:svg-icons-register'

// 自定义全局组件
import globalComponent from '@/components'

// 样式
import '@/styles/index.scss'

// 路由
import router from './router'

// Pinia 状态管理
import pinia from '@/store'

// 路由鉴权（在路由挂载之后导入，确保拦截器已生效）
import './permisstion'

// Element Plus 图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 工具函数
import { formatTime } from '@/utils/time'

// 创建应用
const app = createApp(App)

// 安装 Element Plus，并设置中文语言
app.use(ElementPlus, {
  locale: zhCn,
})

// 安装全局组件
app.use(globalComponent)

// 安装 Pinia
app.use(pinia)

// 安装路由
app.use(router)

// 注册 Element Plus 图标组件
Object.entries(ElementPlusIconsVue).forEach(([key, component]) => {
  app.component(key, component)
})

// 挂载工具函数到全局属性
app.config.globalProperties.$formatTime = formatTime

// 挂载应用
app.mount('#app')
