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
import Logo from './logo/index.vue'
import Menu from './menu/index.vue'
import Main from './main/index.vue'
import Tabbar from './tabbar/index.vue'
import { useUserStore } from '@/store/modules/user'

const userStore = useUserStore()
const $route = useRoute()

// 根据角色过滤菜单列表
const filteredMenuList = computed(() => {
  const role = userStore.role
  return userStore.menuRoutes.filter((item: any) => {
    return !item.meta.roles || item.meta.roles.includes(role)
  })
})
</script>

<style scoped lang="scss">
.layout_container {
  background-color: #f0f0f0;
  width: 100%;
  height: 100%;
  display: flex;

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
    top: 0;
    left: $base-menu-width;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
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
