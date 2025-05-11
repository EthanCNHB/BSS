import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//@ts-ignore忽略当前文件ts类型的检测否则有红色提示(打包会失败)
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'virtual:svg-icons-register'
//引入自定义插件
import gloalComponent from '@/components'
import '@/styles/index.scss'
//引入路由
import router from './router'
//引入仓库pinia
import pinia from './store'

const app = createApp(App)

app.use(ElementPlus, {
  locale: zhCn,
})

//安装自定义插件
app.use(gloalComponent)

//安装pinia
app.use(pinia)

//注册模板路由
app.use(router)
//引入路由鉴权文件
import './permisstion'

import * as ElementPlusIconsVue from '@element-plus/icons-vue'

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.mount('#app')
