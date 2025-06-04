<template>
  <div class="reservation-container">
    <el-card>
      <template #header>
        <div class="header">我的订单</div>
      </template>

      <!-- 订单表格 -->
      <el-table :data="paginatedReservations" border stripe style="width: 100%; margin-bottom: 20px">
        <el-table-column prop="reservationId" label="订单编号" />
        <el-table-column prop="textbookId" label="教材ID" />
        <el-table-column prop="textbookName" label="教材名称" />
        <el-table-column prop="textbookPrice" label="教材价格（元）" />
        <!-- 订购数量列，使用 el-input-number 支持加减按钮 -->
        <el-table-column label="订购数量" width="150">
          <template #default="{ row }">
            <el-input-number
              v-model="row.reservationQuantity"
              :min="1"
              size="small"
              controls-position="right"
              @change="onQuantityChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="下单时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.orderDate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" type="danger" @click="confirmDelete(row.reservationId, row.textbookName)">删除</el-button>
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
        :total="filteredReservations.length"
        @current-change="handlePageChange"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useReservationStore } from '@/store/modules/textbookReservation'
import { formatTime } from '@/utils/time'
import type { TextbookReservation } from '@/store/modules/type'

// 引入 Pinia 中的征订记录 Store
const reservationStore = useReservationStore()

// 分页状态
const currentPage = ref(1)
const pageSize = ref(5)

// 窗口大小改变时动态计算每页条数
const calculatePageSize = () => {
  const windowHeight = window.innerHeight
  // 估算表头 + 搜索栏 + 分页占用约 200px，高度留余后每行约 50px
  const available = windowHeight - 200
  pageSize.value = Math.max(1, Math.floor(available / 50))
}

// 分页后的订单列表
const filteredReservations = computed<TextbookReservation[]>(() => {
  return reservationStore.reservations
})

const paginatedReservations = computed<TextbookReservation[]>(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredReservations.value.slice(start, end)
})

const handlePageChange = (page: number) => {
  currentPage.value = page
}

// 拉取当前学生的所有征订记录
onMounted(() => {
  reservationStore.fetchReservations()
  calculatePageSize()
  window.addEventListener('resize', calculatePageSize)
})

// 组件销毁时移除监听
onBeforeUnmount(() => {
  window.removeEventListener('resize', calculatePageSize)
})

// 确认删除
const confirmDelete = async (id: number, name: string) => {
  try {
    await ElMessageBox.confirm(`确定要删除《${name}》的订单吗？`, '删除确认', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning',
    })
    // 调用 Store 中的 deleteReservation
    await reservationStore.deleteReservation(id)
    // 若删除后当前页没有数据，回退一页
    if (paginatedReservations.value.length === 0 && currentPage.value > 1) {
      currentPage.value--
    }
  } catch (err: any) {
    if (err === 'cancel') {
      ElMessage.info('已取消删除')
    } else {
      console.error('删除失败：', err)
      ElMessage.error('删除失败，请稍后重试')
    }
  }
}

// 当数量被修改时触发，调用后端更新接口
const onQuantityChange = async (row: TextbookReservation) => {
  const newQty = row.reservationQuantity
  if (newQty <= 0) {
    ElMessage.error('数量必须大于 0')
    return
  }
  try {
    // 调用 Store 中的 updateQuantity
    await reservationStore.updateQuantity(row.reservationId, newQty)
  } catch (err: any) {
    ElMessage.error('更新失败，请稍后重试')
    // 如果更新失败，可考虑回滚为之前的数量或重新拉取
    await reservationStore.fetchReservations()
  }
}
</script>

<style scoped>
.reservation-container {
  padding: 20px;
}
.header {
  font-size: 18px;
  font-weight: bold;
}
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
