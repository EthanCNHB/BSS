<template>
  <div class="layout_container">
    <!-- 左侧菜单 -->
    <div class="layout_slider">
      <Logo />
      <el-scrollbar class="scrollbar">
        <el-menu :default-active="$route.path" background-color="#ffffff" text-color="#2d2736" active-text-color="red">
          <Menu :menuList="filteredMenuList" />
        </el-menu>
      </el-scrollbar>
    </div>
    <!-- 顶部导航 -->
    <div class="layout_tabbar"><Tabbar></Tabbar></div>
    <!-- 主体内容 -->
    <div class="layout_main"><Main></Main></div>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import Logo from './logo/index.vue'
import Menu from './menu/index.vue'
import Main from './main/index.vue'
import Tabbar from './tabbar/index.vue'
import { useUserStore } from '@/store/modules/user' // 获取用户状态
import { computed } from 'vue'

// 获取用户状态小仓库
const userStore = useUserStore()
const $route = useRoute()

// 根据角色过滤菜单列表
const filteredMenuList = computed(() => {
  const role = userStore.role // 获取当前用户角色
  return userStore.menuRoutes.filter((item: any) => {
    if (!item.meta.roles || item.meta.roles.includes(role)) {
      return true
    }
    return false
  })
})
</script>

<style scoped lang="scss">
.layout_container {
  background-color: #f0f0f0;
  width: 100%;
  height: 100%;
  .layout_slider {
    width: $base-menu-width;
    height: 100vh;
    background-color: $base-menu-bg-color;
    .scrollbar {
      width: 100%;
      height: calc(100vh - $base-menu-logo-height);
    }
  }
  .layout_tabbar {
    position: fixed;
    width: calc(100% - $base-menu-width);
    height: $base-tabbar-height;
    background-color: $base-tabbar-bg-color;
    top: 0px;
    left: $base-menu-width;
  }
  .layout_main {
    position: absolute;
    width: calc(100% - $base-menu-width);
    height: calc(100vh - $base-tabbar-height);
    top: $base-tabbar-height;
    left: $base-menu-width;
    padding: 20px;
    overflow: auto;
  }
}
</style>
