<template>
  <el-button type="primary" size="small" icon="Refresh" @click="updateRefsh"></el-button>
  <el-button type="primary" size="small" icon="Setting" @click=""></el-button>
  <img :src="userStore.state.avatar" alt="" style="width: 24px; height: 24px; margin: 10px; border-radius: 50%" />
  <!-- 下拉菜单 -->
  <el-dropdown>
    <span class="el-dropdown-link">
      {{ userStore.state.username }}
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
import useUserStore from '@/store/modules/user'
import { useRouter } from 'vue-router'
let layOutSettingStore = useLayOutSettingStore()
//获取用户相关小仓库
let userStore = useUserStore()
//刷新按钮回调
const updateRefsh = () => {
  layOutSettingStore.refsh = !layOutSettingStore.refsh
}
let $router = useRouter()
//退出登录的点击回调
const logout = () => {
  //第一件事情：向服务器发送请求
  userStore.userLogout()
  //第二件事情：仓库当中关于用于相关的数据清空
  //第三件事情: 跳转到登录界面
  $router.push({ path: '/login' })
}
</script>

<style scoped></style>
