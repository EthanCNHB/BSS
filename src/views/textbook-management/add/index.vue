<template>
  <div>
    <h1>添加教材</h1>
    <el-form :model="form" ref="formRef" label-width="100px">
      <el-form-item label="教材名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入教材名称" />
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input v-model="form.author" placeholder="请输入作者名称" />
      </el-form-item>
      <el-form-item label="价格" prop="price">
        <el-input v-model="form.price" placeholder="请输入教材价格" />
      </el-form-item>
      <el-button type="primary" @click="addTextbook">提交</el-button>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useTextbookStore } from '@/store/modules/textbook' // 导入 Pinia Store

// 创建 Pinia store 实例
const textbookStore = useTextbookStore()

// 定义表单数据模型
const form = ref({
  name: '',
  author: '',
  price: 0,
})

// 添加教材的操作
const addTextbook = async () => {
  const textbook = {
    ...form.value,
  }

  await textbookStore.addTextbook(textbook) // 调用 Pinia Store 中的 addTextbook 方法
  form.value = { name: '', author: '', price: 0 } // 清空表单
}
</script>

<style scoped>
/* 样式 */
</style>
