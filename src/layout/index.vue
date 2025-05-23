<template>
  <div class="layout_container">
    <!-- 左侧菜单 -->
    <div class="layout_slider">
      <Logo />
      <el-scrollbar class="scrollbar">
        <el-menu :default-active="route.path" background-color="#ffffff" text-color="#2d2736" active-text-color="red">
          <Menu :menuList="filteredMenuList" />
        </el-menu>
      </el-scrollbar>
    </div>

    <!-- 顶部导航 -->
    <div class="layout_tabbar">
      <Tabbar />
    </div>

    <!-- 主体内容 -->
    <div class="layout_main">
      <Main />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import { computed } from 'vue'

// —— 一定要把这些子组件都 import 进来 —— //
import Logo from './logo/index.vue'
import Menu from './menu/index.vue'
import Main from './main/index.vue'
import Tabbar from './tabbar/index.vue'

// 路由表 & 用户角色
import { constantRoute } from '@/router/routes'
import { useUserStore } from '@/store/modules/user'

// 当前路由，用于 el-menu 高亮
const route = useRoute()

// 拿到当前用户角色
const userStore = useUserStore()
const role = computed(() => userStore.role)

// 递归过滤 + 拼接完整路径
function filterMenu(routes: any[], base = ''): any[] {
  return routes
    .filter((r) => {
      // 隐藏项或权限不符都过滤掉
      if (r.meta?.hidden) return false
      if (r.meta?.roles && !r.meta.roles.includes(role.value)) return false
      return true
    })
    .map((r) => {
      // 拼出像 '/course-management/list' 这样的完整路由
      const fullPath = r.path.startsWith('/') ? r.path : `${base}/${r.path}`
      const node: any = { ...r, path: fullPath, meta: r.meta || {} }
      if (r.children) {
        node.children = filterMenu(r.children, fullPath)
      }
      return node
    })
}

// 最终给菜单组件的数据
const filteredMenuList = computed(() => filterMenu(constantRoute))
</script>

<style scoped lang="scss">
.layout_container {
  display: flex;
  width: 100%;
  height: 100%;
  background-color: #f0f0f0;

  .layout_slider {
    width: $base-menu-width;
    background-color: $base-menu-bg-color;
    height: 100vh;

    .scrollbar {
      height: calc(100vh - $base-menu-logo-height);
      width: 100%;
    }
  }

  .layout_tabbar {
    position: fixed;
    left: $base-menu-width;
    top: 0;
    width: calc(100% - $base-menu-width);
    height: $base-tabbar-height;
    background-color: $base-tabbar-bg-color;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
  }

  .layout_main {
    position: absolute;
    left: $base-menu-width;
    top: $base-tabbar-height;
    width: calc(100% - $base-menu-width);
    height: calc(100vh - $base-tabbar-height);
    padding: 20px;
    overflow: auto;
  }
}
</style>
