<template>
  <el-button type="primary" size="small" icon="Refresh" @click="updateRefsh" />
  <el-button type="primary" size="small" icon="Setting" />
  <img :src="userStore.avatar" alt="avatar" class="user-avatar" />
  <!-- 下拉菜单 -->
  <el-dropdown>
    <span class="el-dropdown-link">
      {{ userStore.username }}
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup lang="ts">
import useLayOutSettingStore from '@/store/modules/setting'
import { useUserStore } from '@/store/modules/user'
import { useRouter } from 'vue-router'

const layOutSettingStore = useLayOutSettingStore()
const userStore = useUserStore()
const router = useRouter()

// 刷新按钮回调
const updateRefsh = () => {
  layOutSettingStore.refsh = !layOutSettingStore.refsh
}

// 退出登录回调
const logout = () => {
  userStore.logout()
  router.push({ path: '/login' })
}
</script>

<style scoped lang="scss">
.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin: 0 10px;
  object-fit: cover;
}
.el-dropdown-link {
  cursor: pointer;
  color: #333;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 6px;
}
</style>
