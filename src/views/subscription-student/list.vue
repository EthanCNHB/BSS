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
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="primary" size="small" :disabled="isAlreadyReserved(row.textbookId)" @click="handleSubscribe(row)">
              <span v-if="isAlreadyReserved(row.textbookId)">已征订</span>
              <span v-else>征订</span>
            </el-button>
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
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useTextbookStore } from '@/store/modules/textbook'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useReservationStore } from '@/store/modules/textbookReservation'
import type { Textbook } from '@/store/modules/type'

// 引入 Pinia 中的教材数据和征订数据
const textbookStore = useTextbookStore()
const reservationStore = useReservationStore()

// 搜索关键字与分页状态
const searchName = ref('')
const currentPage = ref(1)
const pageSize = ref(5)

// 计算用户已经征订的教材 ID 集合，用于在表格中禁用“征订”按钮
const reservedIds = computed<number[]>(() => reservationStore.reservations.map((r) => r.textbookId))

// 判断单条教材是否已被当前用户征订
const isAlreadyReserved = (textbookId: number): boolean => {
  return reservedIds.value.includes(textbookId)
}

// 订阅（征订）按钮逻辑
const handleSubscribe = async (book: Textbook) => {
  try {
    // 二次校验：如果已征订则不重复发起请求
    if (isAlreadyReserved(book.textbookId)) {
      ElMessage.info(`您已征订过《${book.name}》`)
      return
    }

    await ElMessageBox.confirm(`确定将《${book.name}》添加到征订单中吗？`, '确认征订', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })

    // 调用 Pinia action，使用 textbookId 即可
    await reservationStore.addReservation(book.textbookId)

    ElMessage.success(`《${book.name}》已成功添加到订单！`)
  } catch (err: any) {
    // 如果用户点击取消，ElMessageBox 会抛出 'cancel'，不视为错误
    if (err === 'cancel') {
      ElMessage.info('已取消征订操作')
    } else {
      console.error('征订失败：', err)
      ElMessage.error('征订失败，请稍后再试')
    }
  }
}

// 监听页面高度变化，动态计算每页显示条数
const calculatePageSize = () => {
  const windowHeight = window.innerHeight
  // 估算每行表格高度为 50px，减去上部空白和分页高度约 300px
  const availableHeight = windowHeight - 300
  pageSize.value = Math.max(1, Math.floor(availableHeight / 50))
}

// 分页与搜索逻辑
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

// 过滤教材列表
const filteredTextbooks = computed(() => {
  if (!searchName.value) return textbookStore.textbooks
  return textbookStore.textbooks.filter((book) => book.name.includes(searchName.value))
})

// 当前页要显示的教材
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredTextbooks.value.slice(start, end)
})

// 初始化：拉取教材与征订记录，并设置分页
onMounted(() => {
  textbookStore.fetchTextbooks()
  reservationStore.fetchReservations()
  calculatePageSize()
  window.addEventListener('resize', calculatePageSize)
})

// 卸载前移除监听
onBeforeUnmount(() => {
  window.removeEventListener('resize', calculatePageSize)
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
  margin-bottom: 20px;
}
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
