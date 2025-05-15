<template>
  <div>
    <template v-for="item in menuList" :key="item.path">
      <!-- 没有子路由 -->
      <el-menu-item v-if="!item.children && !item.meta.hidden" :index="item.path" @click="goRoute(item)">
        <template #title>
          <el-icon><component :is="item.meta.icon"></component></el-icon>
          <span>{{ item.meta.title }}</span>
        </template>
      </el-menu-item>

      <!-- 有子路由但只有一个 -->
      <el-menu-item
        v-if="item.children && item.children.length === 1 && !item.children[0].meta.hidden"
        :index="item.children[0].path"
        @click="goRoute(item.children[0])"
      >
        <template #title>
          <el-icon><component :is="item.children[0].meta.icon"></component></el-icon>
          <span>{{ item.children[0].meta.title }}</span>
        </template>
      </el-menu-item>

      <!-- 有多个子路由 -->
      <el-sub-menu v-if="item.children && item.children.length > 1" :index="item.path">
        <template #title>
          <el-icon><component :is="item.meta.icon"></component></el-icon>
          <span>{{ item.meta.title }}</span>
        </template>
        <Menu :menuList="item.children" />
      </el-sub-menu>
    </template>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'
import { useRouter } from 'vue-router'

// 获取父组件传递过来的路由数组
const props = defineProps(['menuList'])
const $router = useRouter()

const goRoute = (item: { path: string }) => {
  // 确保 item 具有一个 path 属性，并进行路由跳转
  console.log(item)
  if (item?.path) {
    $router.push(item.path) // 路由跳转
  }
}
</script>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'Menu',
})
</script>

<style scoped>
/* 在这里添加菜单样式 */
</style>
