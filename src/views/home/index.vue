<template>
  <div>
    <h1>教材列表</h1>
    <el-table :data="textbooks" border>
      <el-table-column label="教材名称" prop="name"></el-table-column>
      <el-table-column label="作者" prop="author"></el-table-column>
      <el-table-column label="价格" prop="price"></el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button @click="deleteTextbook(row.textbookId)" type="danger" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useTextbookStore } from '@/store/modules/textbook' // 导入 Pinia Store

// 创建 Pinia store 实例
const textbookStore = useTextbookStore()

// 获取教材列表
onMounted(() => {
  textbookStore.fetchTextbooks() // 获取教材列表
})

// 删除教材
const deleteTextbook = (textbookId: number) => {
  textbookStore.deleteTextbook(textbookId) // 调用 Pinia Store 中的 deleteTextbook 方法
}

// 获取教材数据
const textbooks = textbookStore.textbooks
</script>

<style scoped>
/* 样式 */
</style>
