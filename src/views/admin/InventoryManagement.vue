<!-- src/views/inventory/InventoryManagement.vue -->
<template>
  <div class="inventory-management-container">
    <el-card>
      <template #header>
        <div class="header">库存管理</div>
      </template>

      <!-- 查询输入框 -->
      <div class="search-bar mb-4">
        <el-input v-model="keyword" placeholder="按名称/编码/作者查询" clearable @clear="onSearch" @keyup.enter.native="onSearch">
          <template #append>
            <el-button icon="Search" @click="onSearch">搜索</el-button>
          </template>
        </el-input>
      </div>

      <!-- 库存表格 -->
      <el-table :data="paginatedData" border style="width: 100%">
        <el-table-column label="序号" width="60">
          <template #default="{ $index }">
            {{ $index + 1 + (currentPage - 1) * pageSize }}
          </template>
        </el-table-column>
        <el-table-column prop="code" label="教材编码" />
        <el-table-column prop="name" label="教材名称" />
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="publisher" label="出版社" />
        <el-table-column prop="price" label="单价(¥)">
          <template #default="{ row }">
            {{ row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="stockQuantity" label="库存" width="200">
          <template #default="{ row }">
            <el-input-number v-model="row.stockQuantity" :min="0" @change="onStockChange(row)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
  <div class="pagination-container">
    <el-pagination
      class="pagination"
      background
      layout="prev, pager, next"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="filteredList.length"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed, onBeforeUnmount } from 'vue'
import { useTextbookStore } from '@/store/modules/textbook'
import { ElMessage } from 'element-plus'
import type { Textbook } from '@/store/modules/type'

const textbookStore = useTextbookStore()

// 本地关键字，与 store 同步
const keyword = ref(textbookStore.searchKeyword)

// 查询关键字（你已有的话可跳过）
const searchName = ref('')

// 分页相关状态
const currentPage = ref(1)
const pageSize = ref(15)

// 同步关键字
watch(keyword, (v) => {
  textbookStore.searchKeyword = v
})

onMounted(async () => {
  // 初始加载：全部教材
  await textbookStore.fetchTextbooks()
})

// 过滤后的列表
const filteredList = computed(() => {
  if (!searchName.value) return textbookStore.textbooks
  return textbookStore.textbooks.filter((book) => book.name.includes(searchName.value))
})

// 计算当前页要展示的数据
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredList.value.slice(start, start + pageSize.value)
})

// 分页切换
function handlePageChange(page: number) {
  currentPage.value = page
}

// 搜索和重置，记得重置页码
function handleSearch() {
  currentPage.value = 1
}
function resetSearch() {
  searchName.value = ''
  currentPage.value = 1
}

// 把 onSearch 改成调用后端查询
async function onSearch() {
  await textbookStore.doSearchByKeyword(keyword.value)
}

// 库存变化时，直接调用更新接口
async function onStockChange(tb: Textbook) {
  try {
    await textbookStore.updateTextbook(tb)
    ElMessage.success(`《${tb.name}》库存已更新为 ${tb.stockQuantity}`)
  } catch {
    // 错误在 store 中已提示
  }
}
</script>

<style scoped>
.inventory-management-container {
  padding: 20px;
  max-width: 1200px;
  margin: auto;
}
.header {
  font-size: 18px;
  font-weight: bold;
}
.search-bar {
  display: flex;
}
.mb-4 {
  margin-bottom: 16px;
}
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px; /* 根据需要调整上下间距 */
}
</style>
