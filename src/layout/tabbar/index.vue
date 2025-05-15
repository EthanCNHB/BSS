<template>
  <div class="tabbar">
    <div class="tabbar_left">
      <Breadcrumb />
    </div>

    <div class="tabbar_right">
      <Setting />
      <span class="greeting">{{ userStore.greeting }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import Breadcrumb from './breadcrumb/index.vue'
import Setting from './setting/index.vue'
import { useUserStore } from '@/store/modules/user'

const userStore = useUserStore()
</script>

<style scoped>
.tabbar {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;
}
.tabbar_left {
  display: flex;
  align-items: center;
  margin: 20px;
}
.tabbar_right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.greeting {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}
</style>
