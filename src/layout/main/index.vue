<template>
  <transition name="fade">
    <router-view v-slot="{ Component, route }">
      <component :is="Component" v-if="flag" :key="route.fullPath" />
    </router-view>
  </transition>
</template>

<script setup lang="ts">
import { watch, ref, nextTick } from 'vue'
import useLayOutSettingStore from '@/store/modules/setting'

const layOutSettingStore = useLayOutSettingStore()
const flag = ref(true)

watch(
  () => layOutSettingStore.refsh,
  async () => {
    flag.value = false
    await nextTick()
    flag.value = true
  },
)
</script>

<style scoped>
.fade-enter-from {
  opacity: 0;
  transform: scale(0);
}
.fade-enter-active {
  transition: all 0.5s;
}
.fade-enter-to {
  opacity: 1;
  transform: scale(1);
}
</style>
