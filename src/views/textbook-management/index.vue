<template>
  <div>
    <h1>教材管理</h1>
    <ul>
      <li v-for="textbook in textbooks" :key="textbook.textbookId">
        {{ textbook.name }} - {{ textbook.author }}
        <button @click="deleteTextbook(textbook.textbookId)">删除</button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { useTextbookStore } from '@/store/modules/textbook' // 导入 textbook store
import { onMounted } from 'vue'

const textbookStore = useTextbookStore()

// 获取教材列表
onMounted(() => {
  textbookStore.fetchTextbooks()
})

// 删除教材
const deleteTextbook = (textbookId: number) => {
  textbookStore.deleteTextbook(textbookId)
}

// 获取教材数据
const textbooks = textbookStore.textbooks
</script>
