<template>
  <div class="home-container">
    <el-card>
      <template #header>
        <div class="header">教材列表</div>
      </template>

      <!-- 查询区域 -->
      <div class="search-bar">
        <el-input v-model="searchName" placeholder="请输入教材名称" clearable />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <!-- 教材表格 -->
      <el-table :data="paginatedData" style="width: 100%; margin-top: 20px" border>
        <el-table-column label="教材ID" prop="textbookId" width="100" />
        <el-table-column label="教材名称" prop="name" width="180" />
        <el-table-column label="教材编码" prop="code" width="150" />
        <el-table-column label="出版社" prop="publisher" width="180" />
        <el-table-column label="作者" prop="author" width="150" />
        <el-table-column label="价格" prop="price" width="100" />
        <el-table-column label="状态" prop="status" width="120" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleSubscribe(row)">征订</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        class="pagination"
        background
        layout="prev, pager, next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="filteredTextbooks.length"
        @current-change="handlePageChange"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useTextbookStore } from '@/store/modules/textbook'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useReservationStore } from '@/store/modules/textbookReservation'

const reservationStore = useReservationStore()

const handleSubscribe = async (book: any) => {
  try {
    await ElMessageBox.confirm(`确定将《${book.name}》添加到征订单中吗？`, '确认征订', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })

    await reservationStore.addReservation(book.textbookId)

    ElMessage.success(`《${book.name}》已成功添加到订单！`)
  } catch (err) {
    if (err !== 'cancel') {
      console.error('征订失败：', err)
      ElMessage.error('征订失败，请稍后再试')
    } else {
      ElMessage.info('已取消征订操作')
    }
  }
}

const textbookStore = useTextbookStore()
const searchName = ref('')
const currentPage = ref(1)
const pageSize = ref(5)

// 根据窗口高度动态设置每页显示条数
const calculatePageSize = () => {
  const windowHeight = window.innerHeight
  // 估算每行表格高度为50px，减去上部空白和分页高度
  const availableHeight = windowHeight - 300
  pageSize.value = Math.floor(availableHeight / 50)
}

onMounted(() => {
  textbookStore.fetchTextbooks()
  calculatePageSize()
  window.addEventListener('resize', calculatePageSize)
})

const handleSearch = () => {
  currentPage.value = 1
}

const resetSearch = () => {
  searchName.value = ''
  currentPage.value = 1
}

const handlePageChange = (newPage: number) => {
  currentPage.value = newPage
}

const filteredTextbooks = computed(() => {
  if (!searchName.value) return textbookStore.textbooks
  return textbookStore.textbooks.filter((book: any) => book.name.includes(searchName.value))
})

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredTextbooks.value.slice(start, end)
})
</script>

<style scoped>
.home-container {
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
  gap: 10px;
  align-items: center;
}
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
